package ca.mcmaster.se2aa4.island.team106.Drones;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team106.DroneTools.Actions;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Direction;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Status;
import ca.mcmaster.se2aa4.island.team106.Exploration.MapArea;


public abstract class BaseDrone {
    protected int minimumBatteryToOperate;
    protected int currentBatteryLevel;
    protected Status status;
    protected Actions action = new Actions();
    protected MapArea mapArea;


    /*******************************************************************************
     * Constructs a new BaseDrone with the specified minimum battery level
     * required to operate and the map area it operates on.
     *
     * @param minimumBatteryToOperate the minimum battery level required to operate
     * @param mapArea the map area the drone operates on
     *******************************************************************************/
    public BaseDrone(int minimumBatteryToOperate,  MapArea mapArea) {
        this.mapArea = mapArea;
        this.minimumBatteryToOperate = minimumBatteryToOperate; 
        this.status = Status.GROUND_FINDER_STATE;
    }

    
    /***********************************************************************************
     * Updates the drone based on the current battery level and the provided direction.
     *
     * @param batteryLevel the current battery level
     * @param direction the current direction of the drone
     ************************************************************************************/
    public abstract void updateDrone(int batteryLevel, Direction direction);


    /**********************************
     * Gets the status of the drone.
     *
     * @return the status of the drone
     **********************************/
    public abstract Status getStatus();


    /********************************************
     * Sets the status of the drone.
     *
     * @param status the new status of the drone
     ********************************************/
    public abstract void setStatus(Status status);


    /*************************************************
     * Gets the current battery level of the drone.
     *
     * @return the current battery level of the drone
     *************************************************/
    public abstract int getBatteryLevel();

    
    /*************************************************************************
     * Carries out the stop action for the drone.
     *
     * @param parameter the parameter JSON object that stores the additional
     * parameters for the action
     *************************************************************************/
    public abstract void stop(JSONObject parameter);


    /************************************************************
     * Carries out the fly action for the drone.
     *
     * @param decision  the decision JSON object to be modified
     ************************************************************/
    public abstract void fly(JSONObject decision);


    /***************************************************************************
     * Checks if the drone can make a decision based on the given battery usage
     * of an action.
     *
     * @param batteryUsage the amount of battery used by the decision
     * @return true if the drone can make the decision, otherwise false
     ***************************************************************************/
    public abstract boolean canMakeDecision(int batteryUsage);


    /****************************************************************
     * Update battery level of drone after performing the action
     *
     * @param batteryUsage the amount of battery used by the action
     ****************************************************************/
    public abstract void useBattery(int batteryUsage); 


    /*************************************************************************
     * Carries out the echo action for the drone.
     *
     * @param parameter the parameter JSON object that stores the additional
     * parameters for the action
     * @param decision  the decision JSON object to be modified
     * @param direction the direction for the echo action
     *************************************************************************/
    public abstract void echo(JSONObject parameter, JSONObject decision, Direction direction);


    /************************************************************************
     * Carries out the heading action for the drone.
     *
     * @param parameter the parameter JSON object that stores the additional
     * parameters for the action
     * @param decision  the decision JSON object to be modified
     * @param direction the new direction the drone should face
     ************************************************************************/
    public abstract void updateHeading(JSONObject parameter, JSONObject decision, Direction direction);


    /***********************************************************
     * Carries out the scan action for the drone.
     *
     * @param decision  the decision JSON object to be modified
     ***********************************************************/
    public abstract void scan(JSONObject decision); 
}
