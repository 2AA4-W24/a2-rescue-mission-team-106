package ca.mcmaster.se2aa4.island.team106.States;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team106.DroneTools.Direction;
import ca.mcmaster.se2aa4.island.team106.DroneTools.DroneFlightManager;
import ca.mcmaster.se2aa4.island.team106.DroneTools.State;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Status;
import ca.mcmaster.se2aa4.island.team106.Drones.BaseDrone;
import ca.mcmaster.se2aa4.island.team106.Exploration.MapArea;
import ca.mcmaster.se2aa4.island.team106.Locations.Point;



public class GroundFinderState implements DroneFlightManager, State {
    private int counts = 1;

    private MapArea mapArea;
    private Point previousDroneCoordinate;
    

    /**************************************************************************
     * Constructs a GroundFinderState object with the given map area.
     * 
     * @param mapArea The map area used to store the details found on the map.
     **************************************************************************/
    public GroundFinderState(MapArea mapArea) {
        this.mapArea = mapArea;
        this.previousDroneCoordinate = new Point(this.mapArea.getDroneX(), this.mapArea.getDroneY());
    }


    /*************************************************************************
     * Handles the operations carried out by the GroundFinderState using the
     * drone, and the specified decision and parameters JSONObjects.
     *
     * @param baseDrone the drone being used to carry out the various actions
     * @param decision the decision JSON object to be modified
     * @param parameters the parameter JSON object that stores the additional
     * parameters for the action
     *************************************************************************/
    @Override
    public void handle(BaseDrone baseDrone, JSONObject decision, JSONObject parameters) {
        this.fly(baseDrone, decision, parameters);
    }


    /*************************************************************************
     * Execute the overall flight operation using the drone, and the specified
     * decision and parameters JSONObjects.
     *
     * @param drone the drone being used to carry out the various actions
     * @param decision the decision JSON object to be modified
     * @param parameters the parameter JSON object that stores the additional
     * parameters for the action
     *************************************************************************/
    @Override
    public void fly(BaseDrone drone, JSONObject decision, JSONObject parameters) {

        if (this.mapArea.getGroundStatus()) {
            if (this.mapArea.getHeading() == this.mapArea.getGroundEchoDirection()) {
                // Transition into the CENTER_START_STATE if the ground is in
                // front of the drone.
                Direction nextHeading = turnDirection(this.mapArea.getHeading());
                drone.updateHeading(parameters, decision, nextHeading);
                drone.setStatus(Status.CENTER_START_STATE);
            } else {
                // Ground is detected perpendicular to the drone. Transition to
                // the appropriate direction based on the start position.
                this.mapArea.setIsAbove(true);
                drone.fly(decision);
                if (this.mapArea.getHeading() == Direction.E || this.mapArea.getHeading() == Direction.W) {
                    this.mapArea.setWidthStartPoint(previousDroneCoordinate.getXCoordinate());
                    drone.setStatus(Status.WIDTH_STATE);
                } else {
                    this.mapArea.setLengthStartPoint(previousDroneCoordinate.getYCoordinate());
                    drone.setStatus(Status.LENGTH_STATE);
                }
            }
        } else {
            // Continue flying forward and echoing in all directions till ground
            // is detected.
            if (this.counts % 5 == 0) {
                previousDroneCoordinate.setCoordinate(this.mapArea.getDroneX(), this.mapArea.getDroneY());
                drone.fly(decision);
            } else if (this.counts % 5 == 1) {
                drone.echo(parameters, decision, Direction.E);
                this.mapArea.setWestDistance(this.mapArea.getLastDistance());
            } else if (this.counts % 5 == 2) {
                drone.echo(parameters, decision, Direction.S);
                this.mapArea.setEastDistance(this.mapArea.getLastDistance());
            } else if (this.counts % 5 == 3) {
                drone.echo(parameters, decision, Direction.N);
                this.mapArea.setSouthDistance(this.mapArea.getLastDistance());
            } else if (this.counts % 5 == 4) {
                drone.echo(parameters, decision, Direction.W);
                this.mapArea.setNorthDistance(this.mapArea.getLastDistance());
            }

            this.counts++;
        }
    }


    /**************************************************************************
     * Determines the direction for the drone to turn based on the current
     * direction and the distance to the borders or ground in each direction.
     *
     * @param currentDirection The current direction of the drone.
     * @return The direction the drone must turn in.
     **************************************************************************/
    private Direction turnDirection(Direction currentDirection) {
        switch (currentDirection) {
            case N:
                if (this.mapArea.getWestDistance() < this.mapArea.getEastDistance()) {
                    return Direction.W;
                } else {
                    return Direction.E;
                }
            case S:
                if (this.mapArea.getEastDistance() < this.mapArea.getWestDistance()) {
                    return Direction.E;
                } else {
                    return Direction.W;
                }
            case E:
                if (this.mapArea.getNorthDistance() < this.mapArea.getSouthDistance()) {
                    return Direction.N;
                } else {
                    return Direction.S;
                }
            case W:
                if (this.mapArea.getSouthDistance() < this.mapArea.getNorthDistance()) {
                    return Direction.S;
                } else {
                    return Direction.N;
                }
            default:
                return currentDirection;
        }
    }
}
