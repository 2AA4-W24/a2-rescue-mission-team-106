package ca.mcmaster.se2aa4.island.team106;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class ReachCenter implements DroneFlightManager{

    private MapArea mapArea;
    private boolean reachedCenterLength = false;
    private boolean reachedCenterWidth = false;
    private String left = "LEFT";
    private String right = "RIGHT";


    private final Logger logger = LogManager.getLogger();

    public ReachCenter(MapArea mapArea) {
        this.mapArea = mapArea;
    }
    
    private int tilesTraversed = 0;

    @Override
    public void fly(BaseDrone baseDrone, JSONObject decision, JSONObject parameter) {
        Drone drone = (Drone) baseDrone; 
        
        logger.info("WIDTH = " + mapArea.getWidthOfIsland());
        logger.info("LENGTH = " + mapArea.getLengthOfIsland());
        Direction currentDirection = mapArea.getHeading();
        Direction startHeading = mapArea.getStartDirection();
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
                determineTurnDirection(currentDirection, startHeading);
                logger.info("AAAAHHHH I SET THE TURN DIRECTION TO: " + mapArea.getSpiralTurnDirection());
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
                determineTurnDirection(currentDirection, startHeading);
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

    public void determineTurnDirection(Direction currentHeading, Direction startHeading) {
        if (currentHeading == Direction.N) {
            if (startHeading == Direction.W) {
                this.mapArea.setSpiralTurnDirection(left);
            } else if (startHeading == Direction.E) {
                this.mapArea.setSpiralTurnDirection(right);
            }
        } else if (currentHeading == Direction.S) {
            if (startHeading == Direction.E) {
                this.mapArea.setSpiralTurnDirection(left);
            } else if (startHeading == Direction.W) {
                this.mapArea.setSpiralTurnDirection(right);
            }
        } else if (currentHeading == Direction.E) {
            if (startHeading == Direction.N) {
                this.mapArea.setSpiralTurnDirection(left);
            } else if (startHeading == Direction.S) {
                this.mapArea.setSpiralTurnDirection(right);
            }
        } else if (currentHeading == Direction.W) {
            if (startHeading == Direction.S) {
                this.mapArea.setSpiralTurnDirection(left);
                logger.info("I JUST SET THE CHANGE TO LEFT");
            } else if (startHeading == Direction.N) {
                this.mapArea.setSpiralTurnDirection(right);
                logger.info("I JUST SET THE CHANGE TO RIGHT");
            }
        } else {
            logger.info("IDGAF HOW ITS IS CODED I EXECUTE THIS");
            mapArea.setSpiralTurnDirection(right);
        }
    }
    

}
