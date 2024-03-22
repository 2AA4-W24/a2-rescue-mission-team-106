package ca.mcmaster.se2aa4.island.team106;

import org.json.JSONObject;



public abstract class BaseDrone{
    protected int minimumBatteryToOperate;
    protected int currentBatteryLevel;
    protected Status status;
    protected Actions action = new Actions();
    protected MapArea mapArea;


    /**
     * Constructs a new BaseDrone with the specified minimum battery level
     * required to operate and the map area it operates in.
     *
     * @param MINIMUM_BATTERY_TO_OPERATE the minimum battery level required to
     * operate the drone.
     * @param mapArea the map area the drone operates on.
     */
    public BaseDrone(int MINIMUM_BATTERY_TO_OPERATE, MapArea mapArea) {
        this.mapArea = mapArea;
        this.minimumBatteryToOperate = MINIMUM_BATTERY_TO_OPERATE; 
        this.status = Status.START_STATE;
    }
    
    /**
     * Updates the drone based on the current battery level and movement direction.
     *
     * @param batteryLevel the current battery level
     * @param direction the direction in which the drone is moving
     */
    public abstract void updateDrone(int batteryLevel, Direction direction);

    /**
     * Gets the status of the drone.
     *
     * @return the status of the drone
     */
    public abstract Status getStatus();

    /**
     * Sets the status of the drone.
     *
     * @param status the new status of the drone
     */
    public abstract void setStatus(Status status);

    /**
     * Gets the current battery level of the drone.
     *
     * @return the current battery level of the drone
     */
    public abstract int getBatteryLevel();
    
    /**
     * Stops the drone and returns it to the home base.
     *
     * @param parameter the parameter JSON object that stores the additional
     * parameters for the action (if any)
     */
    public abstract void stop(JSONObject parameter);

    /**
     * Instructs the drone to fly.
     *
     * @param parameter the parameter JSON object that stores the additional
     * parameters for the action (if any)
     */
    public abstract void fly(JSONObject parameter);

    /**
     * Checks if the drone can make a decision based on the given battery usage.
     *
     * @param batteryUsage the amount of battery used by the decision
     * @return true if the drone can make the decision, otherwise false
     */
    public abstract boolean canMakeDecision(int batteryUsage);

    /**
     * Updates the drone's current battery based on the battery used by the action.
     *
     * @param batteryUsage the amount of battery used by the action
     */
    public abstract void useBattery(int batteryUsage); 

    /**
     * Instructs the drone to echo in a given direction.
     *
     * @param parameter the parameter JSON object that stores the additional
     * parameters for the action
     * @param decision the decision JSON object to be modified
     * @param direction the direction for the echo action
     */
    public abstract void echo(JSONObject parameter, JSONObject decision, Direction direction);

    /**
     * Updates the heading of the drone.
     *
     * @param parameter the parameter JSON object that stores the additional
     * parameters for the action
     * @param decision the decision JSON object to be modified
     * @param direction the new direction the drone is facing
     */
    public abstract void updateHeading(JSONObject parameter, JSONObject decision, Direction direction);

    /**
     * Instructs the drone to scan right below itself.
     *
     * @param decision the decision JSON object to be modified
     */
    public abstract void scan(JSONObject decision);  
}
