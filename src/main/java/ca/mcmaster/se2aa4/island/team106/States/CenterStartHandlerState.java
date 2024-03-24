package ca.mcmaster.se2aa4.island.team106.States;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team106.DroneTools.Direction;
import ca.mcmaster.se2aa4.island.team106.DroneTools.DroneFlightManager;
import ca.mcmaster.se2aa4.island.team106.DroneTools.State;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Status;
import ca.mcmaster.se2aa4.island.team106.Drones.BaseDrone;
import ca.mcmaster.se2aa4.island.team106.Exploration.MapArea;
import ca.mcmaster.se2aa4.island.team106.Locations.Point;


public class CenterStartHandlerState implements DroneFlightManager, State {

    private int counts = 1;

    private MapArea mapArea;
    private Point previousDroneCoordinate;


    /**************************************************************************
     * Constructs a CenterStartHandlerState object with the given map area.
     * 
     * @param mapArea The map area used to store the details found on the map.
     **************************************************************************/
    public CenterStartHandlerState(MapArea mapArea) {
        this.mapArea = mapArea;
        this.previousDroneCoordinate = new Point(this.mapArea.getDroneX(), this.mapArea.getDroneY());
    }


    /*************************************************************************
     * Handles the operations carried out by the CenterStartState using the
     * drone, and the specified decision and parameters JSONObjects.
     *
     * @param baseDrone the drone being used to carry out the various actions
     * @param decision the decision JSON object to be modified
     * @param parameters the parameter JSON object that stores the additional
     * parameters for the action
     *************************************************************************/
    @Override
    public void handle(BaseDrone drone, JSONObject decision, JSONObject parameters) {
        this.previousDroneCoordinate = new Point(this.mapArea.getDroneX(), this.mapArea.getDroneY());
        this.fly(drone, decision, parameters);
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
            // Continue flying forward and echoing in all directions as long as
            // we can no longer detect ground.
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
        } else {
            // If ground can no longer be detected, transition to the
            // GROUND_FINDER_STATE
            Direction groundDirection = this.mapArea.getStartDirection();
            drone.setStatus(Status.GROUND_FINDER_STATE);

            if (this.mapArea.getHeading() == groundDirection) {
                previousDroneCoordinate.setCoordinate(this.mapArea.getDroneX(), this.mapArea.getDroneY());
                drone.fly(decision);
            } else {
                drone.updateHeading(parameters, decision, groundDirection);
            }
        }
    }
}
