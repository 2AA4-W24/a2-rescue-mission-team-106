package ca.mcmaster.se2aa4.island.team106.DroneTools;

public enum Status {
    /** State to find the ground. */
    GROUND_FINDER_STATE,
    /** State to handle the scenarios where we start in a position where ground
     * is in front of the drone at the beginning. */
    CENTER_START_STATE,
    /** State to determine the width of a given area. */
    WIDTH_STATE,
    /** State to determine the length of a given area. */
    LENGTH_STATE,
    /** State to move to the center of a given area. */
    MOVE_CENTER_STATE,
    /** State to carry out operations once at the center of a given area. */
    CENTER_STATE
}
