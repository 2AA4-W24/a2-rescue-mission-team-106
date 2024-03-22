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


public class SpiralSearchState implements SearchAlgorithm, State{

    private MapArea mapArea; 
    private Compass compass = new Compass();
    private Set<Point> scannedTiles = new HashSet<>();

    private int maxLength; // the length we want to reach
    private int maxWidth; // the width we want to reach

    private int tilesTraversed  = 0;
    
    private int currentWidth = 1; 
    private int currentLength = 1; 

    private int counter = 0; 

    private boolean needToUpdateHeading = false;


    public SpiralSearchState(MapArea mapArea) {
        this.mapArea = mapArea;
        this.setDimensions(mapArea.getWidthOfIsland(), mapArea.getLengthOfIsland());
    }

    
    private Direction turnDirection(Direction currentDirection) {
        Direction spiralDirection = this.mapArea.getSpiralTurnDirection();
        if (spiralDirection.equals(Direction.LEFT)) {
            return compass.getLeftDirection(currentDirection);
        } else {
            return compass.getRightDirection(currentDirection);
        }
    }


    @Override
    public void handle(BaseDrone baseDrone, JSONObject decision, JSONObject parameters){
        this.search(baseDrone, decision, parameters);
    }


    @Override
    public void search(BaseDrone baseDrone, JSONObject decision, JSONObject parameters){
        this.setDimensions(mapArea.getWidthOfIsland(), mapArea.getLengthOfIsland());

        if (this.currentLength != this.maxLength || this.currentWidth != this.maxWidth)
        {
            if (this.needToUpdateHeading)
            {
                // gets the right cardinal direction of our current heading

                Direction newDirection = turnDirection(mapArea.getHeading()); 
                baseDrone.updateHeading(parameters, decision, newDirection);
                this.needToUpdateHeading = false; 
                this.counter++; 
            
            }
            else{
                if (this.counter % 2 == 0){
                    baseDrone.fly(decision);
                    this.tilesTraversed++; 
                }
                else{
                    Point currentCoordinates = new Point(mapArea.getDroneX(), mapArea.getDroneY());
                    if (this.scannedTiles.contains(currentCoordinates)){
                        baseDrone.fly(decision);
                        this.tilesTraversed++; 
                    }
                    else{
                        baseDrone.scan(decision);
                        this.scannedTiles.add(currentCoordinates);
                    }
                    this.updateSegment();
                }

                this.counter++; 
            }
        }
        else{
            baseDrone.stop(decision);
        }
    }


    private void updateSegment(){
        // only increment currentWidth, once our tilesTraversed = currentWidth
        // -1, then we move on to the next segment where currentWidth increments
        // width is associated with E and W need to make sure that currentWidth
        // is NOT equal to width only then we can increment currentWidth to next
        // segment
        if ( (mapArea.getHeading() == Direction.E || mapArea.getHeading() == Direction.W) && 
        (this.tilesTraversed == this.currentWidth )){

            if (this.currentWidth != this.maxWidth) {
                this.currentWidth++; // make our segment bigger for next run
            }
            this.tilesTraversed = 0; // we need to reset tilesTraversed because now we have completed our segment
            this.needToUpdateHeading = true; 
        }
        else if ((mapArea.getHeading() == Direction.N || mapArea.getHeading() == Direction.S) && 
        (this.tilesTraversed == this.currentLength ))
        {
            if (this.currentLength != this.maxLength) {
                this.currentLength++; 
            }
            this.tilesTraversed = 0; 
            this.needToUpdateHeading = true; 
            
        }
    }
    
    
    @Override
    public void setDimensions(int maxWidth, int maxLength){
        this.maxWidth = maxWidth; 
        this.maxLength = maxLength;
    }


}

