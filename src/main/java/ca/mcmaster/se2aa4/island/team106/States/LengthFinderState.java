package ca.mcmaster.se2aa4.island.team106.States;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team106.DroneTools.DimensionFinder;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Direction;
import ca.mcmaster.se2aa4.island.team106.DroneTools.State;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Status;
import ca.mcmaster.se2aa4.island.team106.Drones.BaseDrone;
import ca.mcmaster.se2aa4.island.team106.Exploration.MapArea;


public class LengthFinderState implements DimensionFinder, State {
    private MapArea mapArea;
    private int counts = 1;


    /**************************************************************************
     * Constructs a LengthFinderState object with the given map area.
     * 
     * @param mapArea The map area used to store the details found on the map.
     **************************************************************************/
    public LengthFinderState(MapArea mapArea) {
        this.mapArea = mapArea;
    }


    /*************************************************************************
     * Handles the operations carried out by the LengthFinderState using the
     * drone, and the specified decision and parameters JSONObjects.
     *
     * @param baseDrone the drone being used to carry out the various actions
     * @param decision the decision JSON object to be modified
     * @param parameters the parameter JSON object that stores the additional
     * parameters for the action
     *************************************************************************/
    @Override
    public void handle(BaseDrone drone, JSONObject decision, JSONObject parameters) {
        this.getDimension(drone, decision, parameters);
    }


    /**************************************************************************
     * Gets the dimensions of a given area using the drone, and the specified
     * decision and parameters JSONObjects.
     * 
     * @param drone the drone being used to carry out the various actions
     * @param decision the decision JSON object to be modified
     * @param parameters the parameter JSON object that stores the additional
     * parameters for the action
     **************************************************************************/
    @Override
    public void getDimension(BaseDrone drone, JSONObject decision, JSONObject parameters) {
        Direction groundDirection = mapArea.getGroundEchoDirection(); // Guaranteed to be East or West

        if (mapArea.hasObtainedWidth() && !mapArea.getIsAbove() && !mapArea.hasObtainedLength()) {
            // If width has been obtained, but the drone is not perpendicular to
            // the island and the length has not yet been obtained, execute the
            // appropriate commands based on the current direction of the drone.
            if (mapArea.getHeading() == Direction.S && mapArea.getSouthDistance() > 0) {
                drone.fly(decision);
                int newSouthDistance = mapArea.getSouthDistance() - 1;
                mapArea.setSouthDistance(newSouthDistance);

                if (mapArea.getSouthDistance() == 0) {
                    mapArea.setIsAbove(true);
                    int startPoint = mapArea.getDroneY();
                    mapArea.setLengthStartPoint(startPoint);
                }
            } else if (mapArea.getHeading() == Direction.N && mapArea.getNorthDistance() > 0) {
                drone.fly(decision);
                int newNorthDistance = mapArea.getNorthDistance() - 1;
                mapArea.setNorthDistance(newNorthDistance);

                if (mapArea.getNorthDistance() == 0) {
                    mapArea.setIsAbove(true);
                    int startPoint = mapArea.getDroneY();
                    mapArea.setLengthStartPoint(startPoint);
                }
            } else {
                mapArea.setLengthEndPoint(mapArea.getDroneY());

                mapArea.getLengthOfIsland();

                mapArea.setObtainedLength(true); // now we have obtained the length
                echo(drone, groundDirection, decision, parameters);
            }
        } else if (this.mapArea.getIsAbove()) {
            // Move the drone if the drone is perpendicular to the island
            moveDrone(drone, groundDirection, decision, parameters);
        } else {
            mapArea.setLengthEndPoint(mapArea.getDroneY());

            mapArea.getLengthOfIsland();

            mapArea.setObtainedLength(true); // now we have obtained the length

            if (!mapArea.hasObtainedWidth()) {
                // Transition into WIDTH_STATE if width has not yet been obtained.
                drone.updateHeading(parameters, decision, groundDirection);

                Direction previousDirection = mapArea.getPrevHeading();
                this.setNewEchoGroundDirection(previousDirection);

                drone.setStatus(Status.WIDTH_STATE);
            } else {
                // If we have found both the width and the length, we need to
                // transition into the MOVE_CENTER_STATE where we will move
                // to the center point of the island. We will now update our
                // heading to turn into the direction of the last echo.
                drone.setStatus(Status.MOVE_CENTER_STATE);
                drone.updateHeading(parameters, decision, groundDirection);
            }
        }
    }


    /**************************************************************************
     * Fly the drone and echo in the current direction of the drone.
     *
     * @param drone the drone being used to carry out the various actions
     * @param direction the current direction of the drone.
     * @param decision the decision JSON object to be modified
     * @param parameters the parameter JSON object that stores the additional
     * parameters for the action
     **************************************************************************/
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


    /**************************************************************************
     * Echo in the current direction of the drone.
     *
     * @param drone the drone being used to carry out the various actions
     * @param direction the current direction of the drone.
     * @param decision the decision JSON object to be modified
     * @param parameters the parameter JSON object that stores the additional
     * parameters for the action
     **************************************************************************/
    private void echo(BaseDrone drone, Direction direction, JSONObject decision, JSONObject parameters) {
        switch (direction) {
            case W:
                drone.echo(parameters, decision, Direction.W);
                break;
            case E:
                drone.echo(parameters, decision, Direction.E);
                break;
            default:
                break;
        }
    }


    /*************************************************************************
     * Set the direction where the ground will be located based on the original
     * prior direction of the drone.
     * 
     * @param priorDirection the previous direction of the drone.
     *************************************************************************/
    private void setNewEchoGroundDirection(Direction priorDirection) {
        switch (priorDirection) {
            case N:
                mapArea.setGroundEchoDirection(Direction.S);
                break;
            case S:
                mapArea.setGroundEchoDirection(Direction.N);
                break;
            default:
                break;
        }
    }
}
