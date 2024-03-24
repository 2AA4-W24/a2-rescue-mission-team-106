package ca.mcmaster.se2aa4.island.team106.States;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team106.DroneTools.DimensionFinder;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Direction;
import ca.mcmaster.se2aa4.island.team106.DroneTools.State;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Status;
import ca.mcmaster.se2aa4.island.team106.Drones.BaseDrone;
import ca.mcmaster.se2aa4.island.team106.Exploration.MapArea;

public class WidthFinderState implements DimensionFinder, State{

    private MapArea mapArea; 
    private int counts = 1; 

    private final Logger logger = LogManager.getLogger();

    /**
     * Constructs a WidthFinderState object with the given map area.
     * 
     * @param mapArea The map area used to store the details found on the map.
     */
    public WidthFinderState(MapArea mapArea) {
        this.mapArea = mapArea;
    }

    /**
     * Handles the operations carried out by the WidthFInderState using the
     * drone, and the specified decision and parameters JSONObjects.
     *
     * @param baseDrone the drone being used to carry out the various actions
     * @param decision the decision JSON object to be modified
     * @param parameters the parameter JSON object that stores the additional
     * parameters for the action
     */
    @Override
    public void handle(BaseDrone drone, JSONObject decision, JSONObject parameters){
        this.getDimension(drone, decision, parameters);
    }

    /**
     * Gets the dimensions of a given area using the drone, and the specified
     * decision and parameters JSONObjects.
     * 
     * @param drone the drone being used to carry out the various actions
     * @param decision the decision JSON object to be modified
     * @param parameters the parameter JSON object that stores the additional
     * parameters for the action
     */
    @Override
    public void getDimension(BaseDrone drone, JSONObject decision, JSONObject parameters) {
        Direction groundDirection = mapArea.getGroundEchoDirection(); // Guaranteed to be North or South

        if (mapArea.hasObtainedLength() && !mapArea.getIsAbove() && !mapArea.hasObtainedWidth()) {
            // If length has been obtained, but the drone is not perpendicular
            // to the island and the width has not yet been obtained, execute
            // the appropriate commands based on the current direction of the
            // drone.
            if (mapArea.getHeading() == Direction.E && mapArea.getEastDistance() > 0) {
                drone.fly(decision);
                int newEastDistance = mapArea.getEastDistance() - 1;
                mapArea.setEastDistance(newEastDistance);

                if (mapArea.getEastDistance() == 0) {
                    mapArea.setIsAbove(true);
                    int startPoint = mapArea.getDroneX();
                    mapArea.setWidthStartPoint(startPoint);
                }
            } else if (mapArea.getHeading() == Direction.W && mapArea.getWestDistance() > 0) {
                drone.fly(decision);
                int newWestDistance = mapArea.getWestDistance() - 1;
                mapArea.setWestDistance(newWestDistance);

                if (mapArea.getWestDistance() == 0) {
                    mapArea.setIsAbove(true);
                    int startPoint = mapArea.getDroneX();
                    mapArea.setWidthStartPoint(startPoint);
                }
            } else {
                mapArea.setWidthEndPoint(mapArea.getDroneX());

                mapArea.getWidthOfIsland();

                mapArea.setObtainedWidth(true); // now we have obtained the length
                echo(drone, groundDirection, decision, parameters);
            }
        } else if (this.mapArea.getIsAbove()) {
            // Move the drone if the drone is perpendicular to the island
            moveDrone(drone, groundDirection, decision, parameters);
        } else {
            mapArea.setWidthEndPoint(mapArea.getDroneX());

            mapArea.setObtainedWidth(true); // now we have obtained the width

            if (!mapArea.hasObtainedLength()) {
                // Transition into LENGTH_STATE if width has not yet been obtained.
                drone.updateHeading(parameters, decision, groundDirection);

                Direction previousDirection = mapArea.getPrevHeading();

                this.setNewEchoGroundDirection(previousDirection);

                drone.setStatus(Status.LENGTH_STATE);
            } else {
                // If we have found both the width and the length, we need to
                // transition into the MOVE_CENTER_STATE where we will move
                // to the center point of the island. We will now update our
                // heading to turn into the direction of the last echo.
                drone.setStatus(Status.MOVE_CENTER_STATE);
                logger.info("State Changed to:" + Status.MOVE_CENTER_STATE);
                drone.updateHeading(parameters, decision, groundDirection);
            }

        }
    }

    /**
     * Fly the drone and echo in the current direction of the drone.
     *
     * @param drone the drone being used to carry out the various actions
     * @param direction the current direction of the drone.
     * @param decision the decision JSON object to be modified
     * @param parameters the parameter JSON object that stores the additional
     * parameters for the action
     */
    private void moveDrone(BaseDrone drone, Direction direction, JSONObject decision, JSONObject parameters) {
        switch (this.counts % 2) {
            case 1:
                echo(drone, direction, decision, parameters);
                break;
            case 0:
                drone.fly(decision);
                break;
            default:
                break;
        }

        this.counts++;

    }

    /**
     * Echo in the current direction of the drone.
     *
     * @param drone the drone being used to carry out the various actions
     * @param direction the current direction of the drone.
     * @param decision the decision JSON object to be modified
     * @param parameters the parameter JSON object that stores the additional
     * parameters for the action
     */
    private void echo(BaseDrone drone, Direction direction, JSONObject decision, JSONObject parameters){
        switch (direction) {
            case N:
            case S:
                drone.echo(parameters, decision, direction);
                break;
            default:
                logger.info("This was an invalid echo attempted: " + direction);
                break;
        }        
    }

    /**
     * Set the direction where the ground will be located based on the original
     * prior direction of the drone.
     * 
     * @param priorDirection the previous direction of the drone.
     */
    private void setNewEchoGroundDirection(Direction priorDirection){
        switch (priorDirection) {
            case E:
                mapArea.setGroundEchoDirection(Direction.W);
                break;
            case W:
                mapArea.setGroundEchoDirection(Direction.E);
                break;
            default:
                logger.info("Invalid echo direction, your prior direction should be E or W but it was: " + priorDirection);
                break;
        }
    }

}
