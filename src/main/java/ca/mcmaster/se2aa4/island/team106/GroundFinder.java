package ca.mcmaster.se2aa4.island.team106;

import org.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GroundFinder {
    private int counts = 1;
    private final Logger logger = LogManager.getLogger();

    private MapArea mapArea;

    public GroundFinder(MapArea mapArea) {
        this.mapArea = mapArea;
    }

    /*
     * The reason west distance is being set during east, or east distance is
     * being set during west is because of the asynchronous nature of the
     * operation. When u call the 'fly' method, it creates a record in the JSON
     * file. However, you can only read from the record during the acknowledge
     * results call. Since the 'fly' method does not get called until the next
     * call of take decision which happens after the acknowledge results method,
     * the moment during which the distance can be updated is when the next
     * record is being created.
     */

    public void fly(Drone drone, JSONObject decision, JSONObject parameters) {

        if (drone.getGroundStatus()){
            logger.info("GROUND FOUND MARKING THE INITAL POINT OF WIDTH X AT: " + mapArea.getDroneX());

            // the marking of the first 
            // drone.updateHeading(parameters, decision, mapArea.getNewHeading());

            mapArea.setWidthStartPoint(mapArea.getDroneX());
            mapArea.setIsAbove(true); 

            drone.fly(decision);

            //! for this we dont always just want to jump to the width state, we would jump to the length state if our inital heading is north or south
            if (mapArea.getHeading() == Direction.E || mapArea.getHeading() == Direction.W){
                drone.setStatus(Status.WIDTH_STATE); 
                logger.info("TRANSITIONING INTO WIDTH STATE");
            }
            else{
                drone.setStatus(Status.LENGTH_STATE);
                logger.info("TRANSITIONING INTO LENGTH STATE");
            }
        }
        else{ 
            if (this.counts % 6 == 0) {
                drone.fly(decision);
            } else if (this.counts % 6 == 1) {
                logger.info("ECHOING EAST");
                drone.echoEast(parameters, decision);
                this.mapArea.setWestDistance(this.mapArea.getLastDistance());
            } else if (this.counts % 6 == 2) {
                logger.info("ECHOING SOUTH");
                drone.echoSouth(parameters, decision);
                this.mapArea.setEastDistance(this.mapArea.getLastDistance());
            } else if (this.counts % 6 == 3) {
                logger.info("ECHOING NORTH");
                drone.echoNorth(parameters, decision);
                this.mapArea.setSouthDistance(this.mapArea.getLastDistance());
            } else if (this.counts % 6 == 4) {
                logger.info("ECHOING WEST");
                drone.echoWest(parameters, decision);
                this.mapArea.setNorthDistance(this.mapArea.getLastDistance());
            }
            else if (this.counts % 6 == 5 ){
                drone.scan(decision);
            }

            this.counts++;
        }
        logger.info("DRONE IS CURRENTLY FACING: " + mapArea.getHeading());
        
    }

}
