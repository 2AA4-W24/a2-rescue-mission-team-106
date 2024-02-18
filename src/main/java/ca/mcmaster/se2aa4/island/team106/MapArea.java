package ca.mcmaster.se2aa4.island.team106;
import java.util.*;

public class MapArea {

    private Direction heading; // direction the drone is facing
    private Direction prevHeading; // the previous direction the drone was facing 
    private Direction prevEchoDirection;  // most recent echo direction
    private Direction groundEchoDirection; // direcction ground is facing relative to the drone from last echo
    private HashMap<Integer, Object> pointsOfInterestMap = new HashMap<>();


    // These will store if there is ground or no ground when we echo. These will tell is relative to our drones coordinates
    private EchoResponse northArea; // GROUND/ OUT_OF_RANGE
    private EchoResponse eastArea; 
    private EchoResponse westArea; 
    private EchoResponse southArea; 


    public Direction getPrevEchoDirection(){
        return this.prevEchoDirection;
    }

    public Direction getGroundEchoDirection(){
        return this.groundEchoDirection; 
    }

    public Direction getPrevHeading(){
        return this.prevHeading;
    }


    public Direction getHeading(){
        return this.heading; 
    }



    public void setGroundEchoDirection(Direction echoDirection){
        this.groundEchoDirection = echoDirection;  
    }


    public void setPrevEchoDirection(Direction echoDirection){
        this.prevEchoDirection = echoDirection; 
    }

    public void setHeading(Direction heading) {
        this.prevHeading = this.getHeading();
        this.heading = heading;
    }
}
