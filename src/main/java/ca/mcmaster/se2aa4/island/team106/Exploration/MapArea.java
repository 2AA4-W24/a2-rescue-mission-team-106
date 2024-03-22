package ca.mcmaster.se2aa4.island.team106.Exploration;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team106.DroneTools.Direction;
import ca.mcmaster.se2aa4.island.team106.Locations.*;


public class MapArea {
    private final Logger logger = LogManager.getLogger();

    private final int INITAL_X = 0;
    private final int INITAL_Y = 0;

    private boolean isAboveGround = false; //initally drone is not above the island
    private Point droneCoordinates = new Point(INITAL_X, INITAL_Y); // drone originally spawns at 0,0

    //! Used for both the widthFinder and lengthFinder 
    private boolean isAbove; // not physically above the ground this just means we are perpendicular we to island we are still above water 

    //! These variables bellow  will be used for the WidthFinder Later we could just make a class that stores these relevant attributes
    //! Save the coordinate point of starting width
    private int widthStartX; 
    private int widthEndX; 

    private boolean obtainedWidth = false; 
    private boolean groundStatus = false; 

    //! These variables above will be used for the WidthFinder Later we could just make a class that stores these relevant attributes


    //! These variables bellow  will be used for the LengthFinder Later we could just make a class that stores these relevant attributes
    private int lengthStartY;
    private int lengthEndY; 

    private boolean obtainedLength = false; 

    //! These variables above will be used for the WidthFinder Later we could just make a class that stores these relevant attributes


    private Direction heading; // direction the drone is facing
    private Direction prevHeading; // the previous direction the drone was facing 
    private Direction newHeading; // this is the updated direction the drone needs to turn
    private Direction prevEchoDirection;  // most recent echo direction
    private Direction groundEchoDirection; // direction ground is facing relative to the drone from last echo
    private Direction startDirection; // direction the drone is facing when it first spawns
    private Direction spiralTurnDirection;

    private Set<POI> creeks = new HashSet<>();
    private POI emergencySitePoint;
    
    private boolean emergencySiteFound = false;

    private int lastDistance; 

    private int northDistance = 0; // GROUND/ OUT_OF_RANGE
    private int eastDistance = 0; 
    private int westDistance = 0; 
    private int southDistance = 0; 


    public void addCreek(POI creek){
        creeks.add(creek);
    }


    public void viewCreeks(){
        for (POI creek: this.creeks){
            logger.info(creek);
        }
        logger.info("\n");
    }

    public void setEmergencySite(POI emergencySite){
        this.emergencySitePoint = emergencySite;
        this.emergencySiteFound = true;
    }

    
    public void updateCoordinate(Direction direction){
        int newX;
        int newY; 
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
            default:
                break;
        }
    }
  

    public void setObtainedWidth(boolean flag){
        this.obtainedWidth = flag; 
    }

    public void setObtainedLength(boolean flag){
        this.obtainedLength = flag; 
    }


    public void setWidthStartPoint(int xCoordinate){
        this.widthStartX = xCoordinate;
    }


    public void setWidthEndPoint(int xCoordinate){
        this.widthEndX = xCoordinate;
    }


    public void setLengthStartPoint(int yCoordinate){
        this.lengthStartY = yCoordinate;
    }


    public void setLengthEndPoint(int yCoordinate){
        this.lengthEndY = yCoordinate;
    }

    
    public void setIsAbove(boolean flag){
        this.isAbove = flag; 
    }


    public boolean hasObtainedWidth(){
        return this.obtainedWidth; 
    }

    public boolean hasObtainedLength(){
        return this.obtainedLength; 
    }

    
    public boolean getGroundStatus() {
        return this.groundStatus;
    }


    public void setGroundStatus(boolean status) {
        this.groundStatus = status;
    }


    public Set<POI> getCreeks(){
        return this.creeks;
    }

    public POI getEmergencySite() {
        return this.emergencySitePoint;
    }

    public boolean getEmergencySiteStatus() {
        return this.emergencySiteFound;
    }

    public boolean getIsAbove(){
        return this.isAbove;
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

    public int getWidthOfIsland(){
        return Math.abs(this.widthEndX - this.widthStartX);
    }


    public int getLengthOfIsland() {
        return Math.abs(this.lengthStartY - this.lengthEndY);
    }
    
    public Direction getStartDirection() {
        return this.startDirection;
    }

    public Direction getSpiralTurnDirection() {
        return this.spiralTurnDirection;
    }

    public void setSpiralTurnDirection(Direction direction) {
        this.spiralTurnDirection = direction;
    }

    public void setStartDirection(Direction direction) {
        this.startDirection = direction;
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