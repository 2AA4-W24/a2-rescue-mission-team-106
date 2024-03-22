package ca.mcmaster.se2aa4.island.team106;

import org.json.JSONObject;


public class CenterStartHandlerState implements DroneFlightManager, State{
    private int counts = 1;

    private MapArea mapArea;
    private Point previousDroneCoordinate;

    public CenterStartHandlerState(MapArea mapArea) {
        this.mapArea = mapArea;
        this.previousDroneCoordinate = new Point(this.mapArea.getDroneX(), this.mapArea.getDroneY());
    }


    @Override
    public void handle(BaseDrone drone, JSONObject decision, JSONObject parameters){
        this.previousDroneCoordinate = new Point(this.mapArea.getDroneX(), this.mapArea.getDroneY());
        this.fly(drone, decision, parameters);
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
            Direction groundDirection = this.mapArea.getStartDirection();
            drone.updateHeading(parameters, decision, groundDirection);
            drone.setStatus(Status.GROUND_FINDER_STATE);
        }
    }
}
