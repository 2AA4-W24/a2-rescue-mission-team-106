package ca.mcmaster.se2aa4.island.team106;

public enum Direction {
    N, 
    E, 
    S, 
    W,
    FORWARD;


    //! delete later
    public static  Direction fromString(String direction) {
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