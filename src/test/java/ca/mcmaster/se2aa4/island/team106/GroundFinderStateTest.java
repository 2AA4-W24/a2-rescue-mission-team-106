package ca.mcmaster.se2aa4.island.team106;
import org.junit.jupiter.api.Test;
import org.json.JSONObject;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import ca.mcmaster.se2aa4.island.team106.Exploration.MapArea;
import ca.mcmaster.se2aa4.island.team106.Drones.*;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Direction;
import ca.mcmaster.se2aa4.island.team106.States.GroundFinderState;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Status;



public class GroundFinderStateTest {
    private MapArea mapArea;
    private BaseDrone drone;
    private GroundFinderState groundFinder;
    private JSONObject decision;
    private JSONObject parameters;

    @BeforeEach
    public void setUp() {
        mapArea = new MapArea();
        drone = new Drone(30, mapArea);
        groundFinder = new GroundFinderState(mapArea);
        decision = new JSONObject();
        parameters = new JSONObject();
        mapArea.setStartDirection(Direction.N);
        mapArea.setHeading(Direction.N);
        mapArea.setGroundEchoDirection(Direction.N);
    }

    /** 
     * Test contains {@link Drone}, {@link MapArea}, {@link Status}, {@link GroundFinderState}
     */
    @Test
    public void testFlyOverGround() {
        mapArea.setGroundStatus(true);
        drone.setStatus(Status.CENTER_START_STATE);
        
        groundFinder.fly(drone, decision, parameters);
        assertTrue(decision.has("action"));
        assertEquals("heading", decision.getString("action"));
    }

    /** 
     * Test contains {@link MapArea}, {@link Direction}, {@link GroundFinderState}
     */
    @Test
    public void testEchoOperations() {
        mapArea.setGroundEchoDirection(Direction.E);
        mapArea.setGroundStatus(false);

        groundFinder.fly(drone, decision, parameters);

        assertTrue(decision.has("action"));
        assertEquals("echo", decision.getString("action"));

        mapArea.setPrevEchoDirection(Direction.E);
        assertEquals(Direction.E, mapArea.getPrevEchoDirection());

        mapArea.setPrevEchoDirection(Direction.S);
        assertEquals(Direction.S, mapArea.getPrevEchoDirection());

        mapArea.setPrevEchoDirection(Direction.N);
        assertEquals(Direction.N, mapArea.getPrevEchoDirection());

        mapArea.setPrevEchoDirection(Direction.W);
        assertEquals(Direction.W, mapArea.getPrevEchoDirection());
    }

    /** 
     * Test contains {@link MapArea}, {@link Direction}, {@link Status}, {@link Drone}, {@link GroundFinderState}
     */
    @Test
    public void testTransitionToWidthState() {
        mapArea.setGroundStatus(false);
        mapArea.setHeading(Direction.E);
        drone.setStatus(Status.CENTER_START_STATE);

        groundFinder.fly(drone, decision, parameters);

        assertEquals(Status.CENTER_START_STATE, drone.getStatus());
        assertTrue(decision.has("action"));
        assertEquals("echo", decision.getString("action"));
    }

    /** 
     * Test contains {@link MapArea}, {@link Direction}, {@link Status}, {@link Drone}, {@link GroundFinderState}
     */
    @Test
    public void testTransitionToLengthState() {
        mapArea.setGroundStatus(false);
        mapArea.setHeading(Direction.N);
        drone.setStatus(Status.CENTER_START_STATE);

        groundFinder.fly(drone, decision, parameters);
        drone.setStatus(Status.LENGTH_STATE);
        assertEquals(Status.LENGTH_STATE, drone.getStatus());
        assertTrue(decision.has("action"));
        assertEquals("echo", decision.getString("action"));
    }  

}

