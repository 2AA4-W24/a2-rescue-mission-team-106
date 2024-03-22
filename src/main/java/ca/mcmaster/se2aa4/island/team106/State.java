package ca.mcmaster.se2aa4.island.team106;

import org.json.JSONObject;

public interface State {
    void handle(BaseDrone baseDrone, JSONObject decision, JSONObject parameters);
}
