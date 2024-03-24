package ca.mcmaster.se2aa4.island.team106;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Direction;
import ca.mcmaster.se2aa4.island.team106.Drones.Drone;
import ca.mcmaster.se2aa4.island.team106.Exploration.MapArea;
import ca.mcmaster.se2aa4.island.team106.States.SpiralSearchState;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Status;

public class SpiralSearchStateTest {

    private SpiralSearchState state;
    private Drone drone;
    private MapArea mapArea;

    @BeforeEach
    public void setUp() {
        mapArea = new MapArea();
        drone = new Drone(30, mapArea);
        state = new SpiralSearchState(mapArea);
        drone.updateDrone(1000, Direction.E);
        mapArea.setHeading(Direction.E);
    }

    /** 
     * Test contains {@link SpiralSearchState}, {@link MapArea}, {@link Direction}
    */
    @Test
    public void testHandle() {
        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();
        state.handle(drone, decision, parameters);
        assertEquals(Direction.E, mapArea.getHeading());
    }

    /** 
     * Test contains {@link SpiralSearchState}, {@link MapArea}, {@link Status}, {@link Direction}, {@link Drone}
    */
    @Test
    public void testSearch() {
        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();
        drone.setStatus(Status.MOVE_CENTER_STATE);
        mapArea.setHeading(Direction.E);
        state.search(drone, decision, parameters);
        assertEquals(Direction.E, mapArea.getHeading());
    }

}
