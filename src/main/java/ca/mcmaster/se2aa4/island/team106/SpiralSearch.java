package ca.mcmaster.se2aa4.island.team106;

import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class SpiralSearch implements Search{

    private final Logger logger = LogManager.getLogger(); 

    private MapArea mapArea; 
    private Compass compass = new Compass();
    private HashSet<Point> scannedTiles = new HashSet<>();

    private int maxLength; // the length we want to reach
    private int maxWidth; // the width we want to reach

    private int tilesTraversed  = 0;
    
    private int currentWidth = 1; 
    private int currentLength = 1; 

    private int counter = 0; 

    private boolean needToUpdateHeading = false;

    // String spiralDirection;

    public SpiralSearch(MapArea mapArea) {
        this.mapArea = mapArea;
        this.setDimensions(mapArea.getWidthOfIsland(), mapArea.getLengthOfIsland());

    }
    // private String spiralDirection = mapArea.getSpiralTurnDirection();

    private Direction turnDirection(Direction currentDirection) {
        RelativeDirection spiralDirection = this.mapArea.getSpiralTurnDirection();
        logger.info("MY SPIRAL CHANGE IS " + spiralDirection);
        if (spiralDirection.equals(RelativeDirection.LEFT)) {
            return compass.getLeftDirection(currentDirection);
        } else {
            return compass.getRightDirection(currentDirection);
        }
    }


    @Override
    public void search(BaseDrone baseDrone, JSONObject decision, JSONObject parameters){
        Drone drone = (Drone) baseDrone; 

        if (this.currentLength != this.maxLength || this.currentWidth != this.maxWidth)
        {
            logger.info("CURRENT LENGTH: " + this.currentLength + " Max Length: " + this.maxLength + " Current Width: " + this.currentWidth + " Max Width: " + this.maxWidth);
            if (this.needToUpdateHeading)
            {
                // gets the right cardinal direction of our current heading
                //! if start @ (1,1) facing South then turn Left (your middle cardinal direction will be West)
                //! Our first turn to begin in the spiral should be our very original starting direction ***

                Direction newDirection = turnDirection(mapArea.getHeading()); 
                logger.info("NOW WE ARE TURNINGGGGG SO OUR CURRENT HEADING IS: " + mapArea.getHeading()  + " FUCKING TURNED IS: " + newDirection);
                drone.updateHeading(parameters, decision, newDirection);
                this.needToUpdateHeading = false; 
                this.counter++; 
            
            }
            else{
                logger.info("Tiles Traversed: " + this.tilesTraversed + " CurrentLength: " + this.currentLength);
                logger.info("CurrentLength: " + this.currentLength + " Max Length: " + this.maxLength);
                if (this.counter % 2 == 0){
                    logger.info("Drone is flying");
                    drone.fly(decision);
                    this.tilesTraversed++; 
                }
                else{
                    Point currentCoordinates = new Point(mapArea.getDroneX(), mapArea.getDroneY());

                    if (this.scannedTiles.contains(currentCoordinates)){
                        logger.info("AHHHHHH");
                        drone.fly(decision);
                        this.tilesTraversed++; 
                    }
                    else{
                        logger.info("Drone is scanning");
                        drone.scan(decision);
                        this.scannedTiles.add(currentCoordinates);
                    }
                    this.updateSegment();
                }

                this.counter++; 
            }
        }
        else{
            logger.info("I AM IN THE ZOO WITH THE LIONS, APES AND BEARS!");
            drone.stop(decision);
            //! need to add one extra spiral to ensure the island area is covered
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
            logger.info("We have finished our width segment so updating it now!");
            this.tilesTraversed = 0; // we need to reset tilesTraversed because now we have completed our segment
            this.needToUpdateHeading = true; 
        }
        else if ((mapArea.getHeading() == Direction.N || mapArea.getHeading() == Direction.S) && 
        (this.tilesTraversed == this.currentLength ))
        {
            if (this.currentLength != this.maxLength) {
                this.currentLength++; 
            }
            logger.info("We have finished our length segment so updating it now!");
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

