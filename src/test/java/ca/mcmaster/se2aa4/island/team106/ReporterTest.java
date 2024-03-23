package ca.mcmaster.se2aa4.island.team106;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReporterTest {
    private MapArea mapArea;
    private Reporter reporter;
    private Set<Creek> creeks;

    @BeforeEach
    public void setUp() {
        mapArea = new MapArea();
        reporter = new Reporter(mapArea);
        creeks = mapArea.getCreeks();

    }

    @Test
    public void NoCreeks() {
        creeks = mapArea.getCreeks();
        String result = reporter.deliverReport();
        assertEquals("No creek found", result);
    }

    @Test
    public void EmergencySiteNotFound() {
        Creek creek1 = new Creek(new Point(10, 20), "Creek1");
        creeks.add(creek1);;
        String result = reporter.deliverReport();
        assertEquals("Creek1", result); 
    }

    @Test
    public void EmergencySiteFound() {
        Creek creek1 = new Creek(new Point(10, 20), "Creek1");
        Creek emergency = new Creek(new Point(15, 25), "emergency");
        creeks.add(creek1);
        mapArea.setEmergencySite(emergency);
        String result = reporter.deliverReport();
        assertEquals("Creek1", result); 
    }
}
