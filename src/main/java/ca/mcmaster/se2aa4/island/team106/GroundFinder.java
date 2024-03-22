package ca.mcmaster.se2aa4.island.team106;

import org.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GroundFinder implements DroneFlightManager{
    private int counts = 1;
    private final Logger logger = LogManager.getLogger();

    private MapArea mapArea;
    private Point previousDroneCoordinate;

    public GroundFinder(MapArea mapArea) {
        this.mapArea = mapArea;
        this.previousDroneCoordinate = new Point(mapArea.getDroneX(), mapArea.getDroneY());
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

     @Override
    public void fly(BaseDrone drone, JSONObject decision, JSONObject parameters) {

        if (mapArea.getGroundStatus()){
            logger.info("GROUND FOUND MARKING THE INITAL POINT OF WIDTH X AT: " + mapArea.getDroneX());
            mapArea.setIsAbove(true); 
            drone.fly(decision);
            
            //! for this we dont always just want to jump to the width state, we would jump to the length state if our inital heading is north or south
            if (mapArea.getHeading() == Direction.E || mapArea.getHeading() == Direction.W){
                mapArea.setWidthStartPoint(previousDroneCoordinate.getXCoordinate()); //! modified
                drone.setStatus(Status.WIDTH_STATE); 
                logger.info("TRANSITIONING INTO WIDTH STATE");
            }
            else {
                mapArea.setLengthStartPoint(previousDroneCoordinate.getYCoordinate()); //! modified 
                drone.setStatus(Status.LENGTH_STATE);
                logger.info("TRANSITIONING INTO LENGTH STATE");
            }
        }
        else{ 
            if (this.counts % 5 == 0) {
                previousDroneCoordinate.setCoordinate(mapArea.getDroneX(), mapArea.getDroneY());
                drone.fly(decision);
            } else if (this.counts % 5 == 1) {
                logger.info("ECHOING EAST");
                drone.echo(parameters, decision, Direction.E);
                this.mapArea.setWestDistance(this.mapArea.getLastDistance());
            } else if (this.counts % 5 == 2) {
                logger.info("ECHOING SOUTH");
                drone.echo(parameters, decision, Direction.S);
                this.mapArea.setEastDistance(this.mapArea.getLastDistance());
            } else if (this.counts % 5 == 3) {
                logger.info("ECHOING NORTH");
                drone.echo(parameters, decision, Direction.N);
                this.mapArea.setSouthDistance(this.mapArea.getLastDistance());
            } else if (this.counts % 5 == 4) {
                logger.info("ECHOING WEST");
                drone.echo(parameters, decision, Direction.W);
                this.mapArea.setNorthDistance(this.mapArea.getLastDistance());
            }
          
            this.counts++;
        }
        logger.info("DRONE IS CURRENTLY FACING: " + mapArea.getHeading());
    }


}
