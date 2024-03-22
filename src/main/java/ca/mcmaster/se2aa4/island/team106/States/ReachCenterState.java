package ca.mcmaster.se2aa4.island.team106.States;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team106.DroneTools.Direction;
import ca.mcmaster.se2aa4.island.team106.DroneTools.DroneFlightManager;
import ca.mcmaster.se2aa4.island.team106.DroneTools.State;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Status;
import ca.mcmaster.se2aa4.island.team106.Drones.BaseDrone;
import ca.mcmaster.se2aa4.island.team106.Exploration.MapArea;


public class ReachCenterState implements DroneFlightManager, State{

    private MapArea mapArea;
    private boolean reachedCenterLength = false;
    private boolean reachedCenterWidth = false;

    private int tilesTraversed = 0;


    public ReachCenterState(MapArea mapArea) {
        this.mapArea = mapArea;
    }
    

    @Override
    public void handle(BaseDrone drone, JSONObject decision, JSONObject parameter){
        this.fly(drone, decision, parameter);
    }


    @Override
    public void fly(BaseDrone drone, JSONObject decision, JSONObject parameter) {        
        Direction currentDirection = mapArea.getHeading();
        Direction startHeading = mapArea.getStartDirection();
        int tiles = determineTiles(currentDirection);
        if (currentDirection == Direction.E || currentDirection == Direction.W) {
            if (!reachedCenterWidth) {
                if (tilesTraversed < tiles) {
                    drone.fly(decision);
                    tilesTraversed++;
                    if (tilesTraversed == tiles) {
                        reachedCenterWidth = true;
                        tilesTraversed = 0;
                    }
                }
            } else if (!reachedCenterLength && reachedCenterWidth) {
                Direction previousDirection = mapArea.getPrevHeading();
                Direction newDirection = setHeading(previousDirection);
                drone.updateHeading(parameter, decision, newDirection);
            } else {
                determineTurnDirection(currentDirection, startHeading);
                drone.setStatus(Status.CENTER_STATE);
                drone.scan(decision);
            }
        } else if (currentDirection == Direction.N || currentDirection == Direction.S) {
            if (!reachedCenterLength) {
                if (tilesTraversed < tiles) {
                    drone.fly(decision);
                    tilesTraversed++;
                    if (tilesTraversed == tiles) {
                        reachedCenterLength = true;
                        tilesTraversed = 0;
                    }
                }
            } else if (reachedCenterLength && !reachedCenterWidth) {
                Direction previousDirection = mapArea.getPrevHeading();
                Direction newDirection = setHeading(previousDirection);
                drone.updateHeading(parameter, decision, newDirection);
            }
            else {
                determineTurnDirection(currentDirection, startHeading);
                drone.setStatus(Status.CENTER_STATE);
                drone.scan(decision);
            }
        } 
    }
    

    private int determineTiles(Direction direction) {
        if (direction == Direction.E || direction == Direction.W) {
            return mapArea.getWidthOfIsland() / 2;
        } else {
            return mapArea.getLengthOfIsland() / 2;
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


    private void determineTurnDirection(Direction currentHeading, Direction startHeading) {
        if (currentHeading == Direction.N) {
            if (startHeading == Direction.W) {
                this.mapArea.setSpiralTurnDirection(Direction.LEFT);
            } else if (startHeading == Direction.E) {
                this.mapArea.setSpiralTurnDirection(Direction.RIGHT);
            }
        } else if (currentHeading == Direction.S) {
            if (startHeading == Direction.E) {
                this.mapArea.setSpiralTurnDirection(Direction.LEFT);
            } else if (startHeading == Direction.W) {
                this.mapArea.setSpiralTurnDirection(Direction.RIGHT);
            }
        } else if (currentHeading == Direction.E) {
            if (startHeading == Direction.N) {
                this.mapArea.setSpiralTurnDirection(Direction.LEFT);
            } else if (startHeading == Direction.S) {
                this.mapArea.setSpiralTurnDirection(Direction.RIGHT);
            }
        } else if (currentHeading == Direction.W) {
            if (startHeading == Direction.S) {
                this.mapArea.setSpiralTurnDirection(Direction.LEFT);
            } else if (startHeading == Direction.N) {
                this.mapArea.setSpiralTurnDirection(Direction.RIGHT);
            }
        } else {
            mapArea.setSpiralTurnDirection(Direction.RIGHT);
        }
    }
}
