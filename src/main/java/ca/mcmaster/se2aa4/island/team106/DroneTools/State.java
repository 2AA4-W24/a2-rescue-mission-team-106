package ca.mcmaster.se2aa4.island.team106.DroneTools;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team106.Drones.BaseDrone;

public interface State {
    void handle(BaseDrone baseDrone, JSONObject decision, JSONObject parameters);
}
