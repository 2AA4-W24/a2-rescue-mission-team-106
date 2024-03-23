package ca.mcmaster.se2aa4.island.team106;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.mcmaster.se2aa4.island.team106.DroneTools.Status;
import ca.mcmaster.se2aa4.island.team106.Drones.Drone;
import ca.mcmaster.se2aa4.island.team106.Exploration.MapArea;

public class DecisionMakerTest {
    private MapArea mapArea;
    private Drone drone;

    @BeforeEach
    public void initialize(){
        mapArea = new MapArea(); 
        drone = new Drone(1000, mapArea); 

    }
    @Test
    public void testMakeDecisionsGroundFinderState(){
        drone.setStatus(Status.GROUND_FINDER_STATE);
        assertEquals(Status.GROUND_FINDER_STATE, drone.getStatus());
    }

    @Test
    public void testMakeDecisionsCenterStartState(){
        drone.setStatus(Status.CENTER_START_STATE);
        assertEquals(Status.CENTER_START_STATE, drone.getStatus());
    }

}
