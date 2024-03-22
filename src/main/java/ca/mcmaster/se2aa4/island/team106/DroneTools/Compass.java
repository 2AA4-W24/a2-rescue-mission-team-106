package ca.mcmaster.se2aa4.island.team106.DroneTools;

public class Compass {

    public Direction getLeftDirection(Direction currentHeading){
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


    public Direction getRightDirection(Direction currentHeading){
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
