package ca.mcmaster.se2aa4.island.team106;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.mcmaster.se2aa4.island.team106.DroneTools.Status;
import ca.mcmaster.se2aa4.island.team106.Drones.Drone;
import ca.mcmaster.se2aa4.island.team106.Exploration.MapArea;

public class StatusTest {
    private MapArea mapArea;
    private Drone drone;

    @BeforeEach
    public void initialize(){
        mapArea = new MapArea(); 
        drone = new Drone(30, mapArea); 

    }
    @Test
    public void testGroundFinderStatus(){
        drone.setStatus(Status.GROUND_FINDER_STATE);
        assertEquals(Status.GROUND_FINDER_STATE, drone.getStatus());
    }

    @Test
    public void testCenterStartStatus(){
        drone.setStatus(Status.CENTER_START_STATE);
        assertEquals(Status.CENTER_START_STATE, drone.getStatus());
    }

    @Test
    public void testCenterStatus(){
        drone.setStatus(Status.CENTER_STATE);
        assertEquals(Status.CENTER_STATE, drone.getStatus());
    }

    @Test
    public void testLengthStatus(){
        drone.setStatus(Status.LENGTH_STATE);
        assertEquals(Status.LENGTH_STATE, drone.getStatus());
    }

    @Test
    public void testWidthStatus(){
        drone.setStatus(Status.WIDTH_STATE);
        assertEquals(Status.WIDTH_STATE, drone.getStatus());
    }




}
