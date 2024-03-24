package ca.mcmaster.se2aa4.island.team106;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.mcmaster.se2aa4.island.team106.Exploration.MapArea;
import ca.mcmaster.se2aa4.island.team106.Exploration.Reporter;
import ca.mcmaster.se2aa4.island.team106.Locations.*;

public class ReporterTest {
    private MapArea mapArea;
    private Reporter reporter;
    private Set<POI> POI;

    @BeforeEach
    public void setUp() {
        mapArea = new MapArea();
        reporter = new Reporter(mapArea);
        POI = mapArea.getCreeks();

    }

    /** 
     * Test contains {@link POI}, {@link MapArea}, {@link Reporter}
    */
    @Test
    public void testNoCreeks() {
        POI = mapArea.getCreeks();
        String result = reporter.deliverReport();
        assertEquals("No creek found", result);
    }

    /** 
     * Test contains {@link POI}, {@link Reporter}, {@link Point}
    */
    @Test
    public void testEmergencySiteNotFound() {
        POI creek1 = new POI(new Point(10, 20), "Creek1");
        POI.add(creek1);
        String result = reporter.deliverReport();
        assertEquals("Creek1", result); 
    }

    /** 
     * Test contains {@link POI}, {@link Reporter}, {@link Point}
    */
    @Test
    public void testEmergencySiteFound() {
        POI creek1 = new POI(new Point(10, 20), "Creek1");
        POI emergency = new POI(new Point(15, 25), "emergency");
        POI.add(creek1);
        mapArea.setEmergencySite(emergency);
        String result = reporter.deliverReport();
        assertEquals("Creek1", result); 
    }
}
