package ca.mcmaster.se2aa4.island.team106;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Compass;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Direction;
public class CompassTest {
    
    /**
     * The following tests check if the relative left and right directions
     * Test contains {@link compass}
     */

    private Compass compass;
    
    @BeforeEach
    public void initialize() {
        compass = new Compass();
    }

    @Test
    public void testNorthRight(){
        assertEquals(Direction.E, compass.getRightDirection(Direction.N));
        assertNotEquals(Direction.W, compass.getRightDirection(Direction.N));
    }

    @Test
    public void testSouthLeft(){
        assertEquals(Direction.E, compass.getLeftDirection(Direction.S));
        assertNotEquals(Direction.W, compass.getLeftDirection(Direction.S));
    }


    @Test
    public void testSouthRight(){
        assertEquals(Direction.W, compass.getRightDirection(Direction.S));
        assertNotEquals(Direction.E, compass.getRightDirection(Direction.S));
    }

    @Test
    public void testNorthLeft(){
        assertEquals(Direction.W, compass.getLeftDirection(Direction.N));
        assertNotEquals(Direction.E, compass.getLeftDirection(Direction.N));
    }

    @Test
    public void testEastRight(){
        assertEquals(Direction.S, compass.getRightDirection(Direction.E));
        assertNotEquals(Direction.N, compass.getRightDirection(Direction.E));
    }

    @Test
    public void testEastLeft(){
        assertEquals(Direction.N, compass.getLeftDirection(Direction.E));
        assertNotEquals(Direction.S, compass.getLeftDirection(Direction.E));
    }

    @Test
    public void testWestRight(){
        assertEquals(Direction.N, compass.getRightDirection(Direction.W));
        assertNotEquals(Direction.S, compass.getRightDirection(Direction.W));
    }

    @Test
    public void testWestLeft(){
        assertEquals(Direction.S, compass.getLeftDirection(Direction.W));
        assertNotEquals(Direction.W, compass.getLeftDirection(Direction.W));
    }

}
