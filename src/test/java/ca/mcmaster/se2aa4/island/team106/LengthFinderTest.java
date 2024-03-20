package ca.mcmaster.se2aa4.island.team106;
import org.junit.jupiter.api.Test;
import org.json.JSONObject;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;

public class LengthFinderTest {
    private MapArea mapArea;
    private LengthFinder lengthFinder;
    private Drone drone; 
    private JSONObject decision;
    private JSONObject parameters;

    @BeforeEach
    public void initialize() {
        mapArea = new MapArea();
        drone = new Drone(1000, Direction.N, mapArea);
        lengthFinder = new LengthFinder(mapArea);
        decision = new JSONObject();
        parameters = new JSONObject();

    }

    @Test
    public void testLengthObtained() {
        mapArea.setHeading(Direction.S);
        mapArea.setObtainedWidth(true);
        mapArea.setIsAbove(true);
        mapArea.setSouthDistance(1);

        lengthFinder.getLengthOfIsland(drone, decision, parameters);

        assertTrue(mapArea.hasObtainedWidth());
        assertTrue(mapArea.getIsAbove());
        assertFalse(mapArea.hasObtainedLength());

        mapArea.setObtainedLength(true);
        assertTrue(mapArea.hasObtainedLength());

        assertEquals(1, mapArea.getSouthDistance());
        assertEquals(0, mapArea.getLengthOfIsland());
    }
}
