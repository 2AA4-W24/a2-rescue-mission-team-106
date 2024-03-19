package ca.mcmaster.se2aa4.island.team106;

import org.json.JSONObject;

public interface DroneActions {

    void echo(JSONObject parameter, JSONObject decision, Direction direction);

    void fly(JSONObject decision);

    void stop(JSONObject decision);

    void heading(JSONObject parameter, JSONObject decision, Direction direction);

    void scan(JSONObject decision);

    void land(JSONObject parameter, JSONObject decision);

    void explore(JSONObject decision);

    void scout(JSONObject parameter, JSONObject decision, Direction direction);

    void moveTo(JSONObject parameter, JSONObject decision, Direction direction);



}
