package ca.mcmaster.se2aa4.island.team106;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class ReachCenter {

    private MapArea mapArea;
    private boolean reachedCenterLength = false;
    private boolean reachedCenterWidth = false;

    private final Logger logger = LogManager.getLogger();

    public ReachCenter(MapArea mapArea) {
        this.mapArea = mapArea;
    }
    
    private int tilesTraversed = 0;
    public void fly(Drone drone, JSONObject decision, JSONObject parameter) {
        logger.info("WIDTH = " + mapArea.getWidthOfIsland());
        logger.info("LENGTH = " + mapArea.getLengthOfIsland());
        Direction currentDirection = mapArea.getHeading();
        int tiles = determineTiles(currentDirection);
        if ((currentDirection == Direction.E || currentDirection == Direction.W)) {
            if (!reachedCenterWidth) {
                if (tilesTraversed < tiles) {
                    drone.fly(decision);
                    tilesTraversed++;
                    if (tilesTraversed == tiles) {
                        reachedCenterWidth = true;
                        tilesTraversed = 0;
                    }
                }
                logger.info("REACHING CENTER WIDTH. TILES TRAVERSED = " + tilesTraversed);
            } else if (!reachedCenterLength && reachedCenterWidth) {
                logger.info("I HAVE NOW REACHED THE CENTER WIDTH");
                Direction previousDirection = mapArea.getPrevHeading();
                logger.info("PREVIOUS DIRECTION: " + previousDirection);
                Direction newDirection = setHeading(previousDirection);
                drone.updateHeading(parameter, decision, newDirection);
            } else {
                logger.info("We have reached center position");
                logger.info("CURRENT POSITION: X = " + mapArea.getDroneX() + " Y = " + mapArea.getDroneY());
                drone.setStatus(Status.CENTER_STATE);
                drone.scan(decision);
            }
        } else if ((currentDirection == Direction.N || currentDirection == Direction.S)) {
            if (!reachedCenterLength) {
                if (tilesTraversed < tiles) {
                    drone.fly(decision);
                    tilesTraversed++;
                    if (tilesTraversed == tiles) {
                        reachedCenterLength = true;
                        tilesTraversed = 0;
                    }
                }
                logger.info("REACHING CENTER LENGTH. TILES TRAVERSED = " + tilesTraversed);
            } else if (reachedCenterLength && !reachedCenterWidth) {
                logger.info("I HAVE NOW REACHED THE CENTER LENGTH");
                Direction previousDirection = mapArea.getPrevHeading();
                Direction newDirection = setHeading(previousDirection);
                drone.updateHeading(parameter, decision, newDirection);
            }
            else {
                logger.info("We have reached center position");
                logger.info("CURRENT POSITION: X = " + mapArea.getDroneX() + " Y = " + mapArea.getDroneY());
                drone.setStatus(Status.CENTER_STATE);
                drone.scan(decision);
            }
        } 
    }
    
    private int determineTiles(Direction direction) {
        if (direction == Direction.E || direction == Direction.W) {
            return (mapArea.getWidthOfIsland() / 2);
        } else {
            return (mapArea.getLengthOfIsland() / 2);
        }
    }

    private Direction setHeading(Direction priorDirection) {
        if (priorDirection == Direction.W) {
            return Direction.E;
        } else if (priorDirection == Direction.E) {
            return Direction.W;
        } else if (priorDirection == Direction.N) {
            return Direction.S;
        } else {
            return Direction.N;
        }
    }

}