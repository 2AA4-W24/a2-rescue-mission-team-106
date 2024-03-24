package ca.mcmaster.se2aa4.island.team106.DroneTools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team106.Drones.BaseDrone;
import ca.mcmaster.se2aa4.island.team106.Exploration.MapArea;

public class FatalErrorHandler {
    private final Logger logger = LogManager.getLogger();

    private boolean rangeDanger;
    private boolean batteryDanger;

    private BaseDrone drone;
    private MapArea mapArea;
    
    private final int RANGE_BORDER = 1;
    
    public FatalErrorHandler(BaseDrone baseDrone, MapArea mapArea) {
        this.drone = baseDrone;
        this.mapArea = mapArea;
    }
    
    /**
     * Sets the range danger flag based on the specified OUT OF RANGE range.
     *
     * @param range the associated range for OUT OF RANGE
     */
    public void setRangeDanger(int range) {
        if (range <= RANGE_BORDER && mapArea.getHeading() == mapArea.getPrevEchoDirection()) {
            this.rangeDanger = true;
            logger.info("Approaching OUT OF RANGE area changing direction");
        } else {
            this.rangeDanger = false;
        }
    }

    /**
     * Handles dangerous situations by taking appropriate actions.
     *
     * @param decision the decision JSON object to be modified
     * @param parameters the parameter JSON object that stores the additional
     * parameters for the handling
     */
    public void handleDanger(JSONObject decision, JSONObject parameters) {
        if (this.batteryDanger) {
            drone.stop(decision);
            logger.info("STOPPING DRONE DUE TO BATTERY LEVEL");
        } else {
            Direction nextDirection = this.changeDirection(this.mapArea);
            logger.info("CHANGING DIRECTION TO " + nextDirection);
            drone.updateHeading(parameters, decision, nextDirection);
            this.rangeDanger = false;
        }
    }

    /**
     * Sets the battery danger flag based on the input danger value.
     *
     * @param danger the boolean value indicating critical battery danger
     */
    public void setBatteryDanger(boolean danger) {
        if (danger) {
            logger.info("BATTERY LEVEL CRITICAL");
        }
        this.batteryDanger = danger;
    }
    
    /**
     * Gets the danger status indicating range or battery danger.
     *
     * @return true if there is range or battery danger, otherwise false
     */
    public boolean getDanger() {
        return this.rangeDanger || this.batteryDanger;
    }
    
    /**
     * Changes the direction if the drone is too close to the border. The drone
     * naturally pivots to the direction that has a larger OUT OF RANGE range.
     * <p>
     * However, in the case that both perpendicular directions have the same
     * range, it will turn to the direction to the right of it.
     * </p>
     * 
     * @param mapArea the map area to obtain the range for each OUT OF RANGE finding
     * @return the direction to turn in
     */
    public Direction changeDirection(MapArea mapArea) {
        
        if (mapArea.getHeading() == Direction.N) {
            if (mapArea.getEastDistance() < mapArea.getWestDistance()) {
                return Direction.W;
            } else {
                return Direction.E;
            }
        } else if (mapArea.getHeading() == Direction.E){
            if (mapArea.getSouthDistance() < mapArea.getNorthDistance()) {
                return Direction.N;
            } else {
                return Direction.S;
            }
        } else if (mapArea.getHeading() == Direction.W) {
            if (mapArea.getNorthDistance() < mapArea.getSouthDistance()) {
                return Direction.S;
            } else {
                return Direction.N;
            }
        } else {
            if (mapArea.getWestDistance() < mapArea.getEastDistance()) {
                return Direction.E;
            } else {
                return Direction.W;
            }
        }        
    }
}
