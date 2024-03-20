package ca.mcmaster.se2aa4.island.team106;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;


public class CompassTest {

    private Compass compass;
    
    @BeforeEach
    public void initialize() {
        compass = new Compass();
    }

    @Test
    public void testRight(){
        assertEquals(Direction.E, compass.getRightDirection(Direction.N));
        assertNotEquals(Direction.W, compass.getRightDirection(Direction.N));
    }

    @Test
    public void testLeft(){
        assertEquals(Direction.E, compass.getLeftDirection(Direction.S));
        assertNotEquals(Direction.W, compass.getLeftDirection(Direction.S));
    }
}
