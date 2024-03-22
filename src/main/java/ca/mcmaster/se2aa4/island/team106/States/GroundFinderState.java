package ca.mcmaster.se2aa4.island.team106.States;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team106.DroneTools.Direction;
import ca.mcmaster.se2aa4.island.team106.DroneTools.DroneFlightManager;
import ca.mcmaster.se2aa4.island.team106.DroneTools.State;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Status;
import ca.mcmaster.se2aa4.island.team106.Drones.BaseDrone;
import ca.mcmaster.se2aa4.island.team106.Exploration.MapArea;
import ca.mcmaster.se2aa4.island.team106.Locations.Point;


public class GroundFinderState implements DroneFlightManager, State{
    private int counts = 1;

    private MapArea mapArea;
    private Point previousDroneCoordinate;

    public GroundFinderState(MapArea mapArea) {
        this.mapArea = mapArea;
        this.previousDroneCoordinate = new Point(this.mapArea.getDroneX(), this.mapArea.getDroneY());
    }


    @Override
    public void handle(BaseDrone baseDrone, JSONObject decision, JSONObject parameters){
        this.fly(baseDrone, decision, parameters);
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

        if (this.mapArea.getGroundStatus()) {
            if (this.mapArea.getHeading() == this.mapArea.getGroundEchoDirection()) {
                Direction nextHeading = turnDirection(this.mapArea.getHeading());
                drone.updateHeading(parameters, decision, nextHeading);
                drone.setStatus(Status.CENTER_START_STATE);
            } else {
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
