package ca.mcmaster.se2aa4.island.team106;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MapArea {
    private final Logger logger = LogManager.getLogger();

    private boolean isAboveGround = false; //initally drone is not above the island
    private Point droneCoordinates = new Point(0, 0); // drone originally spawns at 0,0

    private Direction heading; // direction the drone is facing
    private Direction prevHeading; // the previous direction the drone was facing 
    private Direction newHeading; // this is the updated direction the drone needs to turn
    private Direction prevEchoDirection;  // most recent echo direction
    private Direction groundEchoDirection; // direction ground is facing relative to the drone from last echo

    private Set<Creek> creeks = new HashSet<>();

    private int lastDistance; //! no potential use for so far


    //? Maybe to rep the case of "OUT_OF_RANGE" we can put a negative distance because we know that value cannot be achieved
    //? So that way when we want to check different cardinal directions if we get a negative distance, that tells us there is no land in that cardinal direction
    //! Might remove later as no need as of right now and currently has a "primitive obssession"
    private int northDistance = 0; // GROUND/ OUT_OF_RANGE
    private int eastDistance = 0; 
    private int westDistance = 0; 
    private int southDistance = 0; 

    private EchoResponse northEchoResponse;
    private EchoResponse eastEchoResponse;
    private EchoResponse westEchoResponse;
    private EchoResponse southEchoResponse;


    public void addCreek(Creek creek){
        creeks.add(creek);
    }


    public void viewCreeks(){
        logger.info("CREEK INFO TIME!");
        for (Creek creek: this.creeks){
            logger.info(creek);
        }
    }



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

    public void setEchoReponseDirectionGround(){
        if (this.getPrevEchoDirection() == Direction.N){
            this.setNorthEchoResponse(EchoResponse.GROUND);
        }
        else if (this.getPrevEchoDirection() == Direction.E){
            this.setEastEchoResponse(EchoResponse.GROUND);
        }
        else if (this.getPrevEchoDirection() == Direction.W){
            this.setWestEchoResponse(EchoResponse.GROUND);
        }
        else if (this.getPrevEchoDirection() == Direction.S){
            this.setSouthEchoResponse(EchoResponse.GROUND);
        }

    }


    public void setEchoReponseDirectionOutOfRange(){
        if (this.getPrevEchoDirection() == Direction.N){
            logger.info("CALL TO FRIENDS HERE!");
            this.setNorthEchoResponse(EchoResponse.OUT_OF_RANGE);
        }
        else if (this.getPrevEchoDirection() == Direction.E){
            this.setEastEchoResponse(EchoResponse.OUT_OF_RANGE);
        }
        else if (this.getPrevEchoDirection() == Direction.W){
            this.setWestEchoResponse(EchoResponse.OUT_OF_RANGE);
        }
        else if (this.getPrevEchoDirection() == Direction.S){
            this.setSouthEchoResponse(EchoResponse.OUT_OF_RANGE);
        }

    }

    
    public EchoResponse getNorthEchoResponse(){
        return this.northEchoResponse;
    }


    public EchoResponse getEastEchoResponse(){
        return this.eastEchoResponse;
    }


    public EchoResponse getWestEchoResponse(){
        return this.westEchoResponse;
    }


    public EchoResponse getSouthEchoResponse(){
        return this.southEchoResponse;
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


    public void setNorthEchoResponse(EchoResponse response){
        this.northEchoResponse = response;
    }

    public void setEastEchoResponse(EchoResponse response){
        this.eastEchoResponse = response;
    }

    public void setWestEchoResponse(EchoResponse response){
        this.westEchoResponse = response;
    }

    public void setSouthEchoResponse(EchoResponse response){
        this.southEchoResponse = response;
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