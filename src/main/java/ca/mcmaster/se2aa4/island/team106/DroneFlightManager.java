package ca.mcmaster.se2aa4.island.team106;

import org.json.JSONObject;

public interface DroneFlightManager {

    void fly(BaseDrone drone, JSONObject decision, JSONObject parameters);
    
}
