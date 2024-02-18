package ca.mcmaster.se2aa4.island.team106;

import org.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GroundFinder {
    private int counts = 1;

    private final Logger logger = LogManager.getLogger();

    /*
     * The reason west distance is being set during east, or east distance is
     * being set during west is because when u call the thing, it creates a
     * record. However, you can only read from the record during the acknowledge
     * results. Since this does not get called until after the acknowledge
     * results, the moment during which the distance can be updated is when the
     * next record is being created.
     */
    
    public void fly(Drone drone, JSONObject decision, JSONObject parameters, MapArea mapArea) {
        if (this.counts % 5 == 0) {
            drone.fly(decision);
        } else if (this.counts % 5 == 1) {
            logger.info("ECHOING EAST");
            drone.echoEast(parameters, decision);
            mapArea.setWestDistance(mapArea.getLastDistance());
        } else if (this.counts % 5 == 2) {
            logger.info("ECHOING SOUTH");
            drone.echoSouth(parameters, decision);
            mapArea.setEastDistance(mapArea.getLastDistance());
        } else if (this.counts % 5 == 3) {
            logger.info("ECHOING NORTH");
            drone.echoNorth(parameters, decision);
            mapArea.setSouthDistance(mapArea.getLastDistance());
        } else if (this.counts % 5 == 4) {
            logger.info("ECHOING WEST");
            drone.echoWest(parameters, decision);
            mapArea.setNorthDistance(mapArea.getLastDistance());
        }
        this.counts++;
    }

}
