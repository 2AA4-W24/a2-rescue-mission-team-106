package ca.mcmaster.se2aa4.island.team106;
import org.junit.jupiter.api.Test;
import org.json.JSONObject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;

public class DroneTest {
    private MapArea mapArea;
    private Drone drone;

    @BeforeEach 
    public void initializeMap(){
        mapArea = new MapArea();
    }

    @Test
    public void testUpdateDrone() {
        drone = new Drone (1000, Direction.E, mapArea);
        drone.updateDrone(900, Direction.N);
        assertEquals(900, drone.getBatteryLevel());
        assertEquals(Direction.N, mapArea.getHeading());
    }

    @Test
    public void testFlyNorth(){
        int initialY = mapArea.getDroneY();
        mapArea.updateCoordinate(Direction.N);
        assertEquals(initialY + 1, mapArea.getDroneY());
    }

    @Test 
    public void testDroneBattery(){
        drone = new Drone (500, Direction.E, mapArea);
        drone.currentBatteryLevel = 1000;
        assertTrue(drone.canMakeDecision(100));
        drone.useBattery(600);
        assertFalse(drone.canMakeDecision(6));
        
    }

    @Test 
    public void testUpdateHeading(){
        JSONObject parameter = new JSONObject();
        JSONObject decision = new JSONObject();
        drone = new Drone(1000, Direction.E, mapArea);
        drone.updateHeading(parameter, decision, Direction.N);
        assertEquals(Direction.N, mapArea.getHeading());
    } 
}
