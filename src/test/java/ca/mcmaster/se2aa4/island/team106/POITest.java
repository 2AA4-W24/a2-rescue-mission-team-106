package ca.mcmaster.se2aa4.island.team106;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import ca.mcmaster.se2aa4.island.team106.Locations.*;


public class POITest {
    @Test
    public void testGetDistance() {
        Point point1 = new Point(0, 0);
        Point point2 = new Point(3, 4); 

        POI poi1 = new POI(point1, "POI1");
        POI poi2 = new POI(point2, "POI2");

        double distance = poi1.getDistance(poi2);

        assertEquals(5.0, distance); 
    }
}
