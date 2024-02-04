package ca.mcmaster.se2aa4.island.team106;

public enum Direction {
    NORTH, 
    EAST, 
    SOUTH, 
    WEST;

    public static Direction fromString(String direction) {
        switch (direction.toUpperCase()) {
            case "E":
                return EAST;
            case "W":
                return WEST;
            case "N":
                return NORTH;
            case "S":
                return SOUTH;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }
    }
}