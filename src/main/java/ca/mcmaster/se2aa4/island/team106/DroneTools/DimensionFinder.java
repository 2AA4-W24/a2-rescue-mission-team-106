package ca.mcmaster.se2aa4.island.team106.DroneTools;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team106.Drones.BaseDrone;


public interface DimensionFinder {

    /*************************************************************************
     * Gets the dimensions of a given area using the drone, and the specified
     * decision and parameters JSONObjects.
     * 
     * @param drone the drone being used to carry out the various actions
     * @param decision the decision JSON object to be modified
     * @param parameters the parameter JSON object that stores the additional
     *parameters for the action
     *************************************************************************/
    void getDimension(BaseDrone drone, JSONObject decision, JSONObject parameters);
}
