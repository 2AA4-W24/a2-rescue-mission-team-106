package ca.mcmaster.se2aa4.island.team106.DroneTools;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team106.Drones.BaseDrone;

public interface DimensionFinder {
    void getDimension(BaseDrone drone, JSONObject decision, JSONObject parameters);
}
