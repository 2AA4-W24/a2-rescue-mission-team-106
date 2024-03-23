package ca.mcmaster.se2aa4.island.team106.Exploration;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team106.DroneTools.Direction;
import ca.mcmaster.se2aa4.island.team106.Locations.*;

public class MapArea {
    private final Logger logger = LogManager.getLogger();

    private final int INITIAL_X = 0;
    private final int INITIAL_Y = 0;

    private Point droneCoordinates = new Point(INITIAL_X, INITIAL_Y); // drone originally spawns at 0,0

    // Used for both the widthFinder and lengthFinder 
    private boolean isAbove; // not physically above the ground this just means we are perpendicular we to island we are still above water 

    private int widthStartX;
    private int widthEndX;

    private boolean obtainedWidth = false;
    private boolean groundStatus = false;

    private int lengthStartY;
    private int lengthEndY;

    private boolean obtainedLength = false;

    private Direction heading; // direction the drone is facing
    private Direction prevHeading; // the previous direction the drone was facing 
    private Direction newHeading; // this is the updated direction the drone needs to turn
    private Direction prevEchoDirection; // most recent echo direction
    private Direction groundEchoDirection; // direction ground is facing relative to the drone from last echo
    private Direction startDirection; // direction the drone is facing when it first spawns
    private Direction spiralTurnDirection;

    private Set<POI> creeks = new HashSet<>();
    private POI emergencySitePoint;

    private boolean emergencySiteFound = false;

    private String currentAction;

    private int lastDistance;

    private int northDistance = 0;
    private int eastDistance = 0;
    private int westDistance = 0;
    private int southDistance = 0;

    /**
    * Adds a creek to be stored in the map area.
    * 
    * @param creek the creek to be added.
    */
    public void addCreek(POI creek) {
        creeks.add(creek);
    }

    /**
     * Displays all creeks to the terminal
     */
    public void viewCreeks() {
        for (POI creek : this.creeks) {
            logger.info(creek);
        }
        logger.info("\n");
    }

    /**
     * Sets the emergency site of the island in the map area.
     * 
     * @param emergencySite the emergency site to be set.
     */
    public void setEmergencySite(POI emergencySite) {
        this.emergencySitePoint = emergencySite;
        this.emergencySiteFound = true;
    }

    /**
     * Converts a string representation of direction to a Direction enum value.
     * 
     * @param direction the string representation of direction (N, E, S, W).
     * @return the Direction enum value corresponding to the input string.
     */
    public Direction fromString(String direction) {
        String upperCaseDirection = direction.toUpperCase();
        switch (upperCaseDirection) {
            case "E":
                return Direction.E;
            case "W":
                return Direction.W;
            case "N":
                return Direction.N;
            case "S":
                return Direction.S;
            default:
                return Direction.FORWARD;
        }
    }

    /**
     * Updates the drone's coordinates based on the given direction.
     * 
     * @param direction the direction of the drone that will affect the coordinates
     */
    public void updateCoordinate(Direction direction) {
        int newX;
        int newY;
        switch (direction) {
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

    /**
     * Sets the flag indicating whether the width has been obtained.
     * 
     * @param flag the flag value to be set.
     */
    public void setObtainedWidth(boolean flag) {
        this.obtainedWidth = flag;
    }

    /**
     * Sets the flag indicating whether the length has been obtained.
     * 
     * @param flag the flag value to be set.
     */
    public void setObtainedLength(boolean flag) {
        this.obtainedLength = flag;
    }

    /**
     * Sets the starting point of the width.
     * 
     * @param xCoordinate the x-coordinate of the starting point.
     */
    public void setWidthStartPoint(int xCoordinate) {
        this.widthStartX = xCoordinate;
    }

    /**
     * Sets the ending point of the width.
     * 
     * @param xCoordinate the x-coordinate of the ending point.
     */
    public void setWidthEndPoint(int xCoordinate) {
        this.widthEndX = xCoordinate;
    }

    /**
     * Sets the starting point of the length.
     * 
     * @param yCoordinate the y-coordinate of the starting point.
     */
    public void setLengthStartPoint(int yCoordinate) {
        this.lengthStartY = yCoordinate;
    }

    /**
     * Sets the ending point of the length.
     * 
     * @param yCoordinate the y-coordinate of the ending point.
     */
    public void setLengthEndPoint(int yCoordinate) {
        this.lengthEndY = yCoordinate;
    }

    /**
     * Sets the flag indicating whether the drone is perpendicular to the island.
     * 
     * @param flag the flag value to be set.
     */
    public void setIsAbove(boolean flag) {
        this.isAbove = flag;
    }

    /**
     * Checks if the width has been obtained.
     * 
     * @return true if the width has been obtained, false otherwise.
     */
    public boolean hasObtainedWidth() {
        return this.obtainedWidth;
    }

    /**
     * Checks if the length has been obtained.
     * 
     * @return true if the length has been obtained, false otherwise.
     */
    public boolean hasObtainedLength() {
        return this.obtainedLength;
    }

    /**
     * Obtains the ground status.
     * 
     * @return the ground status.
     */
    public boolean getGroundStatus() {
        return this.groundStatus;
    }

    /**
     * Sets the ground status.
     * 
     * @param status the ground status to be set.
     */
    public void setGroundStatus(boolean status) {
        this.groundStatus = status;
    }

    /**
     * Sets the current action.
     * 
     * @param action the string representing the current action that has been set.
     */
    public void setCurrentAction(String action) {
        this.currentAction = action;
    }

    /**
     * Obtains the current action.
     * 
     * @return the current action.
     */
    public String getCurrentAction() {
        return this.currentAction;
    }

    /**
     * Obtains the set of creeks.
     * 
     * @return the set of creeks.
     */
    public Set<POI> getCreeks() {
        return this.creeks;
    }

    /**
     * Obtains the emergency site.
     * 
     * @return the emergency site.
     */
    public POI getEmergencySite() {
        return this.emergencySitePoint;
    }

    /**
     * Obtains the emergency site status.
     * 
     * @return the emergency site status.
     */
    public boolean getEmergencySiteStatus() {
        return this.emergencySiteFound;
    }

    /**
     * Checks if the drone is perpendicular to the island.
     *
     * @return true if the drone is perpendicular to the island, false
     * otherwise.
     */
    public boolean getIsAbove() {
        return this.isAbove;
    }

    /**
     * Obtains the x-coordinate of the drone.
     * 
     * @return the x-coordinate of the drone.
     */
    public int getDroneX() {
        return this.droneCoordinates.getXCoordinate();
    }

    /**
     * Obtains the y-coordinate of the drone.
     * 
     * @return the y-coordinate of the drone.
     */
    public int getDroneY() {
        return this.droneCoordinates.getYCoordinate();
    }

    /**
     * Obtains the previous echo direction.
     * 
     * @return the previous echo direction.
     */
    public Direction getPrevEchoDirection() {
        return this.prevEchoDirection;
    }

    /**
     * Obtains the new heading.
     * 
     * @return the new heading.
     */
    public Direction getNewHeading() {
        return this.newHeading;
    }

    /**
     * Obtains the echo direction in which ground was detected.
     * 
     * @return the ground echo direction.
     */
    public Direction getGroundEchoDirection() {
        return this.groundEchoDirection;
    }

    /**
     * Obtains the previous heading.
     * 
     * @return the previous heading.
     */
    public Direction getPrevHeading() {
        return this.prevHeading;
    }

    /**
     * Obtains the current heading.
     * 
     * @return the current heading.
     */
    public Direction getHeading() {
        return this.heading;
    }

    /**
     * Obtains the width of the island.
     * 
     * @return the width of the island.
     */
    public int getWidthOfIsland() {
        return Math.abs(this.widthEndX - this.widthStartX);
    }

    /**
     * Obtains the length of the island.
     * 
     * @return the length of the island.
     */
    public int getLengthOfIsland() {
        return Math.abs(this.lengthStartY - this.lengthEndY);
    }

    /**
     * Obtains the starting direction of the drone.
     * 
     * @return the starting direction.
     */
    public Direction getStartDirection() {
        return this.startDirection;
    }

    /**
     * Obtains the direction in which the drone needs to carry out its spiral
     * in.
     *
     * @return the spiral turn direction.
     */
    public Direction getSpiralTurnDirection() {
        return this.spiralTurnDirection;
    }

    /**
     * Sets the direction in which the drone needs to carry out its spiral in.
     *
     * @param direction the spiral turn direction to be set.
     */
    public void setSpiralTurnDirection(Direction direction) {
        this.spiralTurnDirection = direction;
    }

    /**
     * Sets the starting direction of the drone.
     * 
     * @param direction the starting direction to be set.
     */
    public void setStartDirection(Direction direction) {
        this.startDirection = direction;
    }

    /**
     * Sets the echo direction in which ground was detected.
     * 
     * @param echoDirection the ground echo direction to be set.
     */
    public void setGroundEchoDirection(Direction echoDirection) {
        this.groundEchoDirection = echoDirection;
    }

    /**
     * Sets the new heading of the drone.
     * 
     * @param newHeading the new heading to be set.
     */
    public void setNewHeading(Direction newHeading) {
        this.newHeading = newHeading;
    }

    /**
    * Sets the previous echo direction.
    * 
    * @param echoDirection The previous echo direction to be set.
    */
    public void setPrevEchoDirection(Direction echoDirection) {
        this.prevEchoDirection = echoDirection;
    }

    /**
     * Sets the heading direction and updates the previous heading.
     * 
     * @param heading The heading direction to be set.
     */
    public void setHeading(Direction heading) {
        this.prevHeading = this.getHeading();
        this.heading = heading;
    }

    /**
     * Sets the last distance echoed till either out of range or ground is
     * detected
     *
     * @param distance The last distance traveled.
     */
    public void setLastDistance(int distance) {
        this.lastDistance = distance;
    }

    /**
     * Obtains the last distance traveled by the drone.
     * 
     * @return The last distance traveled.
     */
    public int getLastDistance() {
        return this.lastDistance;
    }

    /**
     * Sets the distance to either the north border or to the ground detected in
     * the northern direction.
     *
     * @param distance The distance to the north to be set.
     */
    public void setNorthDistance(int distance) {
        this.northDistance = distance;
    }

    /**
     * Sets the distance to either the east border or to the ground detected in
     * the eastern direction.
     * 
     * @param distance The distance to the east to be set.
     */
    public void setEastDistance(int distance) {
        this.eastDistance = distance;
    }

    /**
     * Sets the distance to either the west border or to the ground detected in
     * the western direction.
     * 
     * @param distance The distance to the west to be set.
     */
    public void setWestDistance(int distance) {
        this.westDistance = distance;
    }

    /**
     * Sets the distance to either the south border or to the ground detected in
     * the southern direction.
     * 
     * @param distance The distance to the south to be set.
     */
    public void setSouthDistance(int distance) {
        this.southDistance = distance;
    }

    /**
     * Obtains the distance to either the north border or to the ground detected
     * in the northern direction.
     *
     * @return The distance to either the north border or to the ground detected
     * in the northern direction..
     */
    public int getNorthDistance() {
        return this.northDistance;
    }

    /**
     * Obtains the distance to either the east border or to the ground detected
     * in the eastern direction.
     *
     * @return The distance to either the east border or to the ground detected
     * in the eastern direction..
     */
    public int getEastDistance() {
        return this.eastDistance;
    }

    /**
     * Obtains the distance to either the west border or to the ground detected
     * in the western direction.
     * 
     * @return The distance to either the west border or to the ground detected
     * in the western direction..
     */
    public int getWestDistance() {
        return this.westDistance;
    }

    /**
     * Obtains the distance to either the south border or to the ground detected
     * in the southern direction.
     *
     * @return The distance to either the south border or to the ground detected
     * in the southern direction.
     */
    public int getSouthDistance() {
        return this.southDistance;
    }
}