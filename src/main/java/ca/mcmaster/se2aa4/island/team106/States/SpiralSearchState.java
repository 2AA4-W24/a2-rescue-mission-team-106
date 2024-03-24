package ca.mcmaster.se2aa4.island.team106.States;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team106.DroneTools.Compass;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Direction;
import ca.mcmaster.se2aa4.island.team106.DroneTools.SearchAlgorithm;
import ca.mcmaster.se2aa4.island.team106.DroneTools.State;
import ca.mcmaster.se2aa4.island.team106.Drones.BaseDrone;
import ca.mcmaster.se2aa4.island.team106.Exploration.MapArea;
import ca.mcmaster.se2aa4.island.team106.Locations.Point;

public class SpiralSearchState implements SearchAlgorithm, State {

    private MapArea mapArea;
    private Compass compass = new Compass();
    private Set<Point> scannedTiles = new HashSet<>();

    private int maxLength; // the length we want to reach
    private int maxWidth; // the width we want to reach

    private int tilesTraversed = 0;

    private int currentWidth = 1;
    private int currentLength = 1;

    private int counter = 0;

    private boolean needToUpdateHeading = false;

    /**
     * Constructs a SpiralSearchState object with the given map area.
     * 
     * @param mapArea The map area used to store the details found on the map.
     */
    public SpiralSearchState(MapArea mapArea) {
        this.mapArea = mapArea;
        this.setDimensions(mapArea.getWidthOfIsland(), mapArea.getLengthOfIsland());
    }

    /**
    * Determines the direction of the next turn based on the current direction
    * and the spiral turn direction determined in mapArea.
    *
    * @param currentDirection the current direction of the drone.
    * @return the direction of the next turn.
    */
    private Direction turnDirection(Direction currentDirection) {
        Direction spiralDirection = this.mapArea.getSpiralTurnDirection();
        if (spiralDirection.equals(Direction.LEFT)) {
            return compass.getLeftDirection(currentDirection);
        } else {
            return compass.getRightDirection(currentDirection);
        }
    }

    /**
     * Handles the operations carried out by the SpiralSearchState using the
     * drone, and the specified decision and parameters JSONObjects.
     *
     * @param baseDrone the drone being used to carry out the various actions
     * @param decision the decision JSON object to be modified
     * @param parameters the parameter JSON object that stores the additional
     * parameters for the action
     */
    @Override
    public void handle(BaseDrone baseDrone, JSONObject decision, JSONObject parameters) {
        this.search(baseDrone, decision, parameters);
    }

    /**
     * Performs a search operation on a given area using the drone, and the
     * specified decision and parameters JSONObjects.
     *
     * @param drone the drone being used to carry out the various actions
     * @param decision the decision JSON object to be modified
     * @param parameters the parameter JSON object that stores the additional
     * parameters for the action
     */
    @Override
    public void search(BaseDrone baseDrone, JSONObject decision, JSONObject parameters) {
        this.setDimensions(mapArea.getWidthOfIsland(), mapArea.getLengthOfIsland());

        if (this.currentLength != this.maxLength || this.currentWidth != this.maxWidth) {
            if (this.needToUpdateHeading) {
                // gets the right cardinal direction of our current heading

                Direction newDirection = turnDirection(mapArea.getHeading());
                baseDrone.updateHeading(parameters, decision, newDirection);
                this.needToUpdateHeading = false;
                this.counter++;

            } else {
                if (this.counter % 2 == 0) {
                    baseDrone.fly(decision);
                    this.tilesTraversed++;
                } else {
                    Point currentCoordinates = new Point(mapArea.getDroneX(), mapArea.getDroneY());
                    if (this.scannedTiles.contains(currentCoordinates)) {
                        baseDrone.fly(decision);
                        this.tilesTraversed++;
                    } else {
                        baseDrone.scan(decision);
                        this.scannedTiles.add(currentCoordinates);
                    }
                    this.updateSegment();
                }

                this.counter++;
            }
        } else {
            baseDrone.stop(decision);
        }
    }

    /**
     * Updates the current segment of the drone's spiral search movement.
     */
    private void updateSegment() {
        // Only increment currentWidth, once our tilesTraversed = currentWidth
        // -1, then we move on to the next segment where currentWidth increments
        // width is associated with E and W need to make sure that currentWidth
        // is NOT equal to width only then we can increment currentWidth to next
        // segment.
        if ((mapArea.getHeading() == Direction.E || mapArea.getHeading() == Direction.W) &&
                (this.tilesTraversed == this.currentWidth)) {

            if (this.currentWidth != this.maxWidth) {
                this.currentWidth++; // make our segment bigger for next run
            }
            this.tilesTraversed = 0; // we need to reset tilesTraversed because now we have completed our segment
            this.needToUpdateHeading = true;
        } else if ((mapArea.getHeading() == Direction.N || mapArea.getHeading() == Direction.S) &&
                (this.tilesTraversed == this.currentLength)) {
            if (this.currentLength != this.maxLength) {
                this.currentLength++;
            }
            this.tilesTraversed = 0;
            this.needToUpdateHeading = true;

        }
    }

    /**
     * Sets the dimensions for the search area.
     *
     * @param width the width of the search area
     * @param height the height of the search area
     */
    @Override
    public void setDimensions(int maxWidth, int maxLength) {
        this.maxWidth = maxWidth;
        this.maxLength = maxLength;
    }
}
