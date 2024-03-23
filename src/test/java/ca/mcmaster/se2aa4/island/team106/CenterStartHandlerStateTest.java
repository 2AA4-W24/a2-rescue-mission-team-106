package ca.mcmaster.se2aa4.island.team106;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Direction;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Status;
import ca.mcmaster.se2aa4.island.team106.Drones.Drone;
import ca.mcmaster.se2aa4.island.team106.Exploration.MapArea;
import ca.mcmaster.se2aa4.island.team106.States.CenterStartHandlerState;

public class CenterStartHandlerStateTest {

    private CenterStartHandlerState state;
    private Drone drone;
    private MapArea mapArea;

    @BeforeEach
    public void setUp() {
        mapArea = new MapArea();
        drone = new Drone(30, mapArea);
        state = new CenterStartHandlerState(mapArea);
        drone.updateDrone(1000, Direction.N);
    }

    @Test
    public void testHandleWithGroundStatusTrue() {
        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();
        mapArea.setGroundStatus(true);
        state.handle(drone, decision, parameters);
        drone.setStatus(Status.CENTER_START_STATE);
        assertEquals(Status.CENTER_START_STATE, drone.getStatus());
    }


    @Test
    public void testFly() {
        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();
        mapArea.setGroundStatus(true);
        drone.setStatus(Status.CENTER_START_STATE);
        drone.updateDrone(100, Direction.N);
        state.fly(drone, decision, parameters);
        drone.useBattery(20);
        assertEquals(Status.CENTER_START_STATE, drone.getStatus());
        assertEquals(80, drone.getBatteryLevel());
    }
}
