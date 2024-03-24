package ca.mcmaster.se2aa4.island.team106;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.mcmaster.se2aa4.island.team106.DroneTools.Direction;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Status;
import ca.mcmaster.se2aa4.island.team106.Drones.Drone;
import ca.mcmaster.se2aa4.island.team106.Exploration.MapArea;
import ca.mcmaster.se2aa4.island.team106.States.ReachCenterState;

public class ReachCenterStateTest {
    private Drone drone;
    private MapArea mapArea;
    private ReachCenterState reachCenterState;
    private JSONObject decision;
    private JSONObject parameter;

    @BeforeEach
    public void initialize(){
        mapArea = new MapArea();
        drone = new Drone(30, mapArea);
        drone.updateDrone(2, Direction.N);
        reachCenterState = new ReachCenterState(mapArea);
        decision = new JSONObject();
        parameter = new JSONObject();
    }

    /** 
     * Test contains {@link MapArea}, {@link Drone}, {@link ReachCenterState}, {@link Status}
    */
    @Test
    public void testFlyReachingCenterWidth() {
        mapArea.setWidthStartPoint(0);
        drone.setStatus(Status.MOVE_CENTER_STATE); 

        reachCenterState.fly(drone, decision, parameter);

        assertEquals(Status.MOVE_CENTER_STATE, drone.getStatus());
    }

    /** 
     * Test contains {@link MapArea}, {@link Drone}, {@link ReachCenterState}, {@link Status}
    */
    @Test
    public void testFlyReachingCenterLength() {
        mapArea.setLengthStartPoint(0);
        drone.setStatus(Status.MOVE_CENTER_STATE); 

        reachCenterState.fly(drone, decision, parameter);
        assertEquals(Status.MOVE_CENTER_STATE, drone.getStatus());
    }

}
