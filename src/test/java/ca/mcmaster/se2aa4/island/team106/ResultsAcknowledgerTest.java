package ca.mcmaster.se2aa4.island.team106;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ca.mcmaster.se2aa4.island.team106.Drones.Drone;
import ca.mcmaster.se2aa4.island.team106.Exploration.MapArea;
import ca.mcmaster.se2aa4.island.team106.Exploration.ResultsAcknowledger;
import ca.mcmaster.se2aa4.island.team106.DroneTools.FatalErrorHandler;



public class ResultsAcknowledgerTest {

    private Drone drone;
    private MapArea mapArea;
    private FatalErrorHandler handler;
    private ResultsAcknowledger acknowledger;

    @BeforeEach
    public void setUp() {
        drone = new Drone(30, mapArea);
        mapArea = new MapArea();
        handler = new FatalErrorHandler(drone, mapArea);
        acknowledger = new ResultsAcknowledger(drone, mapArea, handler);
    }


    @Test
    public void testDisplayStatus() {
        JSONObject response = new JSONObject("{\"status\": \"OK\"}");
        acknowledger.displayStatus(response);
    }

    @Test
    public void testParseRecordGroundFound() {
        JSONObject response = new JSONObject("{\"extras\": {\"found\": \"GROUND\", \"range\": 10}}");
        acknowledger.parseRecord(response);
        assertTrue(mapArea.getGroundStatus());
        assertEquals(10, mapArea.getLastDistance());
    }


    @Test
    public void testExtractCreeks() {
        JSONObject response = new JSONObject("{\"extras\": {\"creeks\": [\"creek1\", \"creek2\"]}}");
        acknowledger.parseRecord(response);
        assertEquals(2, mapArea.getCreeks().size());
    }

    @Test
    public void testExtractEmergencySite() {
        JSONObject response = new JSONObject("{\"extras\": {\"sites\": [\"emergencySite\"]}}");
        acknowledger.parseRecord(response);
        assertNotNull(mapArea.getEmergencySite());
    }

}
