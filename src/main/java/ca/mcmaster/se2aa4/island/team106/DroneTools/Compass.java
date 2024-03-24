package ca.mcmaster.se2aa4.island.team106.DroneTools;


public class Compass {

    /***********************************************************************
     * Returns the direction to the left of the current heading.
     *
     * @param currentHeading the current heading
     * @return the direction to the left of the current heading
     ***********************************************************************/
    public Direction getLeftDirection(Direction currentHeading) {
        switch (currentHeading) {
            case N:
                return Direction.W;
            case E:
                return Direction.N;
            case S:
                return Direction.E;
            case W:
                return Direction.S;
            default:
                return currentHeading;
        }
    }


    /***********************************************************************
     * Returns the direction to the right of the current heading.
     *
     * @param currentHeading the current heading
     * @return the direction to the right of the current heading
     ***********************************************************************/
    public Direction getRightDirection(Direction currentHeading) {
        switch (currentHeading) {
            case N:
                return Direction.E;
            case E:
                return Direction.S;
            case S:
                return Direction.W;
            case W:
                return Direction.N;
            default:
                return currentHeading;
        }
    }
    
}
