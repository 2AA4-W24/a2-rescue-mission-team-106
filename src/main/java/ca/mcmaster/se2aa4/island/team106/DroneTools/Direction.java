package ca.mcmaster.se2aa4.island.team106.DroneTools;

public enum Direction {
    N, 
    E, 
    S,
    W,
    FORWARD,
    LEFT,
    RIGHT; 


    public static Direction fromString(String direction) {
        switch (direction.toUpperCase()) {
            case "E":
                return E;
            case "W":
                return W;
            case "N":
                return N;
            case "S":
                return S;
            default:
                return FORWARD; 
        }
    }

}