package ca.mcmaster.se2aa4.island.team106;
import org.junit.jupiter.api.Test;
import org.json.JSONObject;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;


public class GroundFinderTest {
    private MapArea mapArea;
    private BaseDrone drone;
    private GroundFinder groundFinder;
    private JSONObject decision;
    private JSONObject parameters;

    @BeforeEach
    public void setUp() {
        mapArea = new MapArea();
        drone = new Drone(100, mapArea);
        groundFinder = new GroundFinder(mapArea);
        decision = new JSONObject();
        parameters = new JSONObject();
        mapArea.setStartDirection(Direction.N);
        mapArea.setHeading(Direction.N);
        mapArea.setGroundEchoDirection(Direction.N);
    }

    @Test
    public void testFlyOverGround() {
        mapArea.setGroundStatus(true);
        drone.setStatus(Status.CENTER_START_STATE);
        
        groundFinder.fly(drone, decision, parameters);
        assertTrue(decision.has("action"));
        assertEquals("heading", decision.getString("action"));
    }

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

