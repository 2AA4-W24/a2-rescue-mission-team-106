package ca.mcmaster.se2aa4.island.team106;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class OutOfRangeHandler {
    private final Logger logger = LogManager.getLogger();

    private boolean danger;
    
    // The reason it is 5 is to allow us some buffer to turn as 2 is the minimum
    // number of blocks we need for it to successfully turn.
    private final int RANGE_BORDER = 5;
    
    
    public void setDanger(int limit, MapArea mapArea) {
        if (limit <= RANGE_BORDER && mapArea.getHeading() == mapArea.getPrevEchoDirection()) {
            this.danger = true;
        } else {
            this.danger = false;
        }
    }

    public void handleDanger(BaseDrone baseDrone, MapArea mapArea, JSONObject decision, JSONObject parameters){
        Drone drone = (Drone) baseDrone; 
        Direction nextDirection = this.changeDirection(mapArea);
        logger.info("CHANGING DIRECTION TO " + nextDirection);

        drone.updateHeading(parameters, decision, nextDirection);
        this.setDanger(false);

    }

    public void setDanger(boolean danger) {
        this.danger = danger;
    }
    
    public boolean getDanger() {
        return this.danger;
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
