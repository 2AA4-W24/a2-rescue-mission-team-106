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
    
    public void setRangeDanger(int limit) {
        if (limit <= RANGE_BORDER && mapArea.getHeading() == mapArea.getPrevEchoDirection()) {
            this.rangeDanger = true;
            logger.info("Approaching OUT OF RANGE area changing direction");
        } else {
            this.rangeDanger = false;
        }
    }

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

    public void setBatteryDanger(boolean danger) {
        if (danger) {
            logger.info("BATTERY LEVEL CRITICAL");
        }
        this.batteryDanger = danger;
    }
    
    public boolean getDanger() {
        return this.rangeDanger || this.batteryDanger;
    }
    
    /**
     * Changes the direction if too close to the border
     * 
     * @param mapArea to obtain the distance for each out of range call
     * @return Direction to head towards
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
