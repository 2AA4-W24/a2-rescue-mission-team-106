package ca.mcmaster.se2aa4.island.team106;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class FatalErrorHandler {
    private final Logger logger = LogManager.getLogger();

    private boolean rangeDanger;
    private boolean batteryDanger;

    private Drone drone;
    private MapArea mapArea;
    private int minOperationalBattery;
    
    // The reason it is 5 is to allow us some buffer to turn as 2 is the minimum
    // number of blocks we need for it to successfully turn.
    private final int RANGE_BORDER = 5;
    
    public FatalErrorHandler(int MINIMUM_BATTERY_TO_OPERATE, BaseDrone baseDrone, MapArea mapArea) {
        this.minOperationalBattery = MINIMUM_BATTERY_TO_OPERATE;
        this.drone = (Drone) baseDrone;
        this.mapArea = mapArea;
    }
    
    public void setDanger(int limit) {
        if (this.drone.getBatteryLevel() <= this.minOperationalBattery) {
            this.batteryDanger = true;
            logger.info("BATTERY LEVEL CRITICAL");
        } else if (limit <= RANGE_BORDER && mapArea.getHeading() == mapArea.getPrevEchoDirection()) {
            this.rangeDanger = true;
            logger.info("Approaching OUT OF RANGE area");
        } else {
            this.rangeDanger = false;
            this.batteryDanger = false;
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
            this.setRangeDanger(false);
        }
    }

    public void setRangeDanger(boolean danger) {
        this.rangeDanger = danger;
    }

    public void setBatteryDanger(boolean danger) {
        this.batteryDanger = danger;
    }
    
    public boolean getDanger() {
        return (this.rangeDanger || this.batteryDanger);
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
