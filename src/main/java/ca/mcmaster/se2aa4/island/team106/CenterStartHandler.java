package ca.mcmaster.se2aa4.island.team106;

import org.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CenterStartHandler implements DroneFlightManager{
    private int counts = 1;
    private final Logger logger = LogManager.getLogger();

    private MapArea mapArea;
    private Point previousDroneCoordinate;

    public CenterStartHandler(MapArea mapArea) {
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
    public void fly(BaseDrone baseDrone, JSONObject decision, JSONObject parameters) {
        Drone drone = (Drone) baseDrone;

        if (drone.getGroundStatus()) {
            if (this.counts % 5 == 0) {
                previousDroneCoordinate.setCoordinate(mapArea.getDroneX(), mapArea.getDroneY());
                drone.fly(decision);
            } else if (this.counts % 5 == 1) {
                logger.info("ECHOING EAST");
                drone.echoEast(parameters, decision);
                this.mapArea.setWestDistance(this.mapArea.getLastDistance());
            } else if (this.counts % 5 == 2) {
                logger.info("ECHOING SOUTH");
                drone.echoSouth(parameters, decision);
                this.mapArea.setEastDistance(this.mapArea.getLastDistance());
            } else if (this.counts % 5 == 3) {
                logger.info("ECHOING NORTH");
                drone.echoNorth(parameters, decision);
                this.mapArea.setSouthDistance(this.mapArea.getLastDistance());
            } else if (this.counts % 5 == 4) {
                logger.info("ECHOING WEST");
                drone.echoWest(parameters, decision);
                this.mapArea.setNorthDistance(this.mapArea.getLastDistance());
            }

            this.counts++;
        } else {
            Direction groundDirection = mapArea.getStartDirection();
            drone.updateHeading(parameters, decision, groundDirection);
            drone.setStatus(Status.START_STATE);
        }
        logger.info("DRONE IS CURRENTLY FACING: " + mapArea.getHeading());

    }
}
