package ca.mcmaster.se2aa4.island.team106.States;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team106.DroneTools.Direction;
import ca.mcmaster.se2aa4.island.team106.DroneTools.DroneFlightManager;
import ca.mcmaster.se2aa4.island.team106.DroneTools.State;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Status;
import ca.mcmaster.se2aa4.island.team106.Drones.BaseDrone;
import ca.mcmaster.se2aa4.island.team106.Exploration.MapArea;

public class ReachCenterState implements DroneFlightManager, State {

    private MapArea mapArea;
    private boolean reachedCenterLength = false;
    private boolean reachedCenterWidth = false;

    private int tilesTraversed = 0;

    /**
     * Constructs a ReachCenterState object with the given map area.
     * 
     * @param mapArea The map area used to store the details found on the map.
     */
    public ReachCenterState(MapArea mapArea) {
        this.mapArea = mapArea;
    }

    /**
     * Handles the operations carried out by the ReachCenterState using the
     * drone, and the specified decision and parameters JSONObjects.
     *
     * @param baseDrone the drone being used to carry out the various actions
     * @param decision the decision JSON object to be modified
     * @param parameters the parameter JSON object that stores the additional
     * parameters for the action
     */
    @Override
    public void handle(BaseDrone drone, JSONObject decision, JSONObject parameter) {
        this.fly(drone, decision, parameter);
    }

    /**
     * Execute the overall flight operation using the drone, and the specified
     * decision and parameters JSONObjects.
     *
     * @param drone the drone being used to carry out the various actions
     * @param decision the decision JSON object to be modified
     * @param parameters the parameter JSON object that stores the additional
     * parameters for the action
     */
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
                // If the center width and center length have been reached,
                // transition to the CENTER_STATE.
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
            } else {
                // If the center width and center length have been reached,
                // transition to the CENTER_STATE.
                determineTurnDirection(currentDirection, startHeading);
                drone.setStatus(Status.CENTER_STATE);
                drone.scan(decision);
            }
        }
    }

    /**
     * Determines the number of tiles the drone should move based on the given
     * direction.
     *
     * @param direction the current direction of the drone.
     * @return the number of tiles the drone should move.
     */
    private int determineTiles(Direction direction) {
        if (direction == Direction.E || direction == Direction.W) {
            return mapArea.getWidthOfIsland() / 2;
        } else {
            return mapArea.getLengthOfIsland() / 2;
        }
    }

    /**
     * Sets the heading of the drone based on the prior direction.
     *
     * @param priorDirection the previous direction of the drone.
     * @return the new heading of the drone.
     */
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

    /**
     * Determines the turn direction for the drone based on its current heading
     * and its start heading.
     *
     * @param currentHeading the current heading of the drone.
     * @param startHeading the initial heading of the drone.
     */
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
