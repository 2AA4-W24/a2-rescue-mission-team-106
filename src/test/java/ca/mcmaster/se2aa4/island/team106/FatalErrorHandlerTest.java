package ca.mcmaster.se2aa4.island.team106;
import org.junit.jupiter.api.Test;
import org.json.JSONObject;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import ca.mcmaster.se2aa4.island.team106.Exploration.MapArea;
import ca.mcmaster.se2aa4.island.team106.Drones.Drone;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Direction;
import ca.mcmaster.se2aa4.island.team106.Drones.BaseDrone;
import ca.mcmaster.se2aa4.island.team106.DroneTools.FatalErrorHandler;
public class FatalErrorHandlerTest {
    private BaseDrone drone;
    private MapArea mapArea;
    private FatalErrorHandler fatalErrorHandler;
    private JSONObject decision;
    private JSONObject parameters;

    @BeforeEach
    public void setUp() {
        drone = new Drone(30, new MapArea());
        drone.updateDrone(2, Direction.N);
        mapArea = new MapArea();
        fatalErrorHandler = new FatalErrorHandler(drone, mapArea);
        decision = new JSONObject();
        parameters = new JSONObject();
        mapArea.setStartDirection(Direction.N);
        mapArea.setHeading(Direction.N);
        mapArea.setGroundEchoDirection(Direction.N);
    }

    /**
     * This test checks if the drone stops when at the battery danger level
     * Test contains {@link FatalErrorHandler}
     */
    @Test
    public void testBatteryCritical() {
        fatalErrorHandler.setBatteryDanger(true);
        assertTrue(fatalErrorHandler.getDanger());
        fatalErrorHandler.handleDanger(decision, parameters);
        assertTrue(decision.has("action"));
        assertEquals("stop", decision.getString("action"));
    }

    /**
     * This test checks how the drone handles reaching out of range 
     * Test contains {@link FatalErrorHandler}, {@link MapArea}, {@link Direction}
     */
    @Test
    public void testApproachingOutOfRange() {
        mapArea.setHeading(Direction.N);
        mapArea.setPrevEchoDirection(Direction.N);
        mapArea.setEastDistance(1);
        fatalErrorHandler.setRangeDanger(2);
        fatalErrorHandler.handleDanger(decision, parameters);
        assertTrue(decision.has("action"));
        assertEquals("heading", decision.getString("action"));
    }

    /**
     * This test checks if the drone turns in the correct direction when reaching out of range 
     * Test contains {@link FatalErrorHandler}, {@link MapArea}, {@link Direction}
     */
    @Test
    public void testChangeDirection() {
        mapArea.setHeading(Direction.N);
        mapArea.setEastDistance(1);
        mapArea.setWestDistance(2);
        assertEquals(Direction.W, fatalErrorHandler.changeDirection(mapArea));

        mapArea.setHeading(Direction.E);
        mapArea.setSouthDistance(1);
        mapArea.setNorthDistance(2);
        assertEquals(Direction.N, fatalErrorHandler.changeDirection(mapArea));

        mapArea.setHeading(Direction.W);
        mapArea.setNorthDistance(1);
        mapArea.setSouthDistance(2);
        assertEquals(Direction.S, fatalErrorHandler.changeDirection(mapArea));

        mapArea.setHeading(Direction.S);
        mapArea.setWestDistance(1);
        mapArea.setEastDistance(2);
        assertEquals(Direction.E, fatalErrorHandler.changeDirection(mapArea));
    }
}
