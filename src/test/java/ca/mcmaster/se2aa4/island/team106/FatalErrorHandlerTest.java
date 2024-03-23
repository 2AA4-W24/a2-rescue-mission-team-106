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
        drone = new Drone(100, new MapArea());
        drone.updateDrone(2, Direction.N);
        mapArea = new MapArea();
        fatalErrorHandler = new FatalErrorHandler(10, drone, mapArea);
        decision = new JSONObject();
        parameters = new JSONObject();
        mapArea.setStartDirection(Direction.N);
        mapArea.setHeading(Direction.N);
        mapArea.setGroundEchoDirection(Direction.N);
    }

    @Test
    public void testBatteryCritical() {
        fatalErrorHandler.setDanger(0);
        assertTrue(fatalErrorHandler.getDanger());
        fatalErrorHandler.handleDanger(decision, parameters);
        assertTrue(decision.has("action"));
        assertEquals("stop", decision.getString("action"));
    }

    @Test
    public void testApproachingOutOfRange() {
        mapArea.setHeading(Direction.N);
        mapArea.setPrevEchoDirection(Direction.N);
        mapArea.setEastDistance(1); // Assuming close to the border
        fatalErrorHandler.setDanger(1);
        assertTrue(fatalErrorHandler.getDanger());
        fatalErrorHandler.handleDanger(decision, parameters);
        assertTrue(decision.has("action"));
        assertEquals("stop", decision.getString("action"));
    }

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
