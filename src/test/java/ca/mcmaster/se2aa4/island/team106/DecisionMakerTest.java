package ca.mcmaster.se2aa4.island.team106;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.mcmaster.se2aa4.island.team106.DroneTools.FatalErrorHandler;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Status;
import ca.mcmaster.se2aa4.island.team106.Drones.Drone;
import ca.mcmaster.se2aa4.island.team106.Exploration.MapArea;
import ca.mcmaster.se2aa4.island.team106.Exploration.DecisionMaker;

public class DecisionMakerTest {
    private MapArea mapArea;
    private Drone drone;
    private FatalErrorHandler fatalErrorHandler;
    private DecisionMaker decisionMaker;
    private JSONObject parameters;
    private JSONObject decision;

    @BeforeEach
    public void initialize(){
        mapArea = new MapArea(); // Create a new instance of MapArea
        drone = new Drone(1000, mapArea); 
        fatalErrorHandler = new FatalErrorHandler(50, drone, mapArea); // Create a new instance of FatalErrorHandler
        decisionMaker = new DecisionMaker(drone, mapArea, fatalErrorHandler); // Create a new instance of DecisionMaker
        parameters = new JSONObject();
        decision = new JSONObject();
    }
    @Test
    public void testMakeDecisionsGroundFinderState() {
        drone.setStatus(Status.GROUND_FINDER_STATE);
        assertEquals(Status.GROUND_FINDER_STATE, drone.getStatus());
    }

    @Test
    public void testMakeDecisionsCenterStartState() {
        drone.setStatus(Status.CENTER_START_STATE);
        assertEquals(Status.CENTER_START_STATE, drone.getStatus());
    }

}
