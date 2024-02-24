package ca.mcmaster.se2aa4.island.team106;
import java.util.*;

public class MapArea {

    private boolean isAboveGround = false; //initally drone is not above the island
    private Point droneCoordinates = new Point(0, 0); // drone originally spawns at 0,0

    private Direction heading; // direction the drone is facing
    private Direction prevHeading; // the previous direction the drone was facing 
    private Direction newHeading; // this is the updated direction the drone needs to turn
    private Direction prevEchoDirection;  // most recent echo direction
    private Direction groundEchoDirection; // direction ground is facing relative to the drone from last echo
    private HashMap<Integer, Object> pointsOfInterestMap = new HashMap<>();

    private int lastDistance;


    //? Maybe to rep the case of "OUT_OF_RANGE" we can put a negative distance because we know that value cannot be achieved
    //? So that way when we want to check different cardinal directions if we get a negative distance, that tells us there is no land in that cardinal direction
    private int northDistance = 0; // GROUND/ OUT_OF_RANGE
    private int eastDistance = 0; 
    private int westDistance = 0; 
    private int southDistance = 0; 

    public void updateCoordinate(Direction direction){
        int newX, newY; 
        switch(direction) {
            case E:
                newX = this.droneCoordinates.getXCoordinate() + 1;
                this.droneCoordinates.setXCoordinate(newX);
                break;
            case W:
                newX = this.droneCoordinates.getXCoordinate() - 1;
                this.droneCoordinates.setXCoordinate(newX);
                break;
            case S:
                newY = this.droneCoordinates.getYCoordinate() - 1;
                this.droneCoordinates.setYCoordinate(newY);
                break;
            case N:
                newY = this.droneCoordinates.getYCoordinate() + 1;
                this.droneCoordinates.setYCoordinate(newY);
                break;
        }
    }


    public int getDroneX(){
        return this.droneCoordinates.getXCoordinate();
    }

    public int getDroneY(){
        return this.droneCoordinates.getYCoordinate();
    }


    public Direction getPrevEchoDirection(){
        return this.prevEchoDirection;
    }

    public boolean getIsAboveGround(){
        return this.isAboveGround;
    }

    public Direction getNewHeading(){
        return this.newHeading; 
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

    public void setIsAboveGround(boolean isAboveGroundStatus){
        this.isAboveGround = isAboveGroundStatus; 
    }

    public void setGroundEchoDirection(Direction echoDirection){
        this.groundEchoDirection = echoDirection;  
    }

    public void setNewHeading(Direction newHeading){
        this.newHeading = newHeading; 
    }


    public void setPrevEchoDirection(Direction echoDirection){
        this.prevEchoDirection = echoDirection; 
    }

    public void setHeading(Direction heading) {
        this.prevHeading = this.getHeading();
        this.heading = heading;
    }

    public void setLastDistance(int distance) {
        this.lastDistance = distance;
    }

    public int getLastDistance() {
        return this.lastDistance;
    }

    public void setNorthDistance(int distance) {
        this.northDistance = distance;
    }

    public void setEastDistance(int distance) {
        this.eastDistance = distance;
    }

    public void setWestDistance(int distance) {
        this.westDistance = distance;
    }

    public void setSouthDistance(int distance) {
        this.southDistance = distance;
    }

    public int getNorthDistance() {
        return this.northDistance;
    }

    public int getEastDistance() {
        return this.eastDistance;
    }

    public int getWestDistance() {
        return this.westDistance;
    }

    public int getSouthDistance() {
        return this.southDistance;
    }
}
