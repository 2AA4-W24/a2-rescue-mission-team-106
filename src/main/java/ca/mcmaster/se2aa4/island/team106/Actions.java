package ca.mcmaster.se2aa4.island.team106;
import org.json.JSONObject;

public class Actions {

    /**
     * Interacts with JSONObjects to carry out the echo action.
     *
     * @param parameter the parameter JSON object that stores the additional
     * parameters for the action
     * @param decision the decision JSON object to be modified
     * @param direction the direction in which the echo action must be carried out
     */
    public void echo(JSONObject parameter, JSONObject decision, Direction direction) {
        parameter.put("direction", direction);
        decision.put("action", "echo");
        decision.put("parameters", parameter);
    }

    /**
     * Interacts with a JSONObject to carry out the fly action.
     *
     * @param decision the decision JSON object to be modified
     */
    public void fly(JSONObject decision) {
        decision.put("action", "fly");
    }

    /**
     * Interacts with a JSONObject to carry out the echo action.
     *
     * @param decision  the decision JSON object to be modified
     */
    public void stop(JSONObject decision) {
        decision.put("action", "stop");
    }

    /**
     * Interacts with JSONObjects to carry out the echo action.
     *
     * @param parameter the parameter JSON object that stores the additional
     * parameters for the action
     * @param decision  the decision JSON object to be modified
     * @param direction the direction the drone wants to head towards.
     */
    public void heading(JSONObject parameter, JSONObject decision, Direction direction) {
        parameter.put("direction", direction);
        decision.put("action", "heading");
        decision.put("parameters", parameter);
    }

    /**
     * Interacts with a JSONObject to carry out the echo action.
     *
     * @param decision  the decision JSON object to be modified
     */
    public void scan(JSONObject decision) {
        decision.put("action", "scan");
    }

    /**
     * Interacts with JSONObjects to carry out the land action.
     *
     * @param parameter the parameter JSON object that stores the additional
     * parameters for the action
     * @param decision  the decision JSON object to be modified
     */
    public void land(JSONObject parameter, JSONObject decision) {
        decision.put("action", "land");
        decision.put("parameters", parameter);
    }

    /**
     * Interacts with a JSONObject to carry out the echo action.
     *
     * @param decision  the decision JSON object to be modified
     */
    public void explore(JSONObject decision) {
        decision.put("action", "explore");
    }

    /**
     * Interacts with JSONObjects to carry out the echo action.
     *
     * @param parameter the parameter JSON object that stores the additional
     * parameters for the action
     * @param decision  the decision JSON object to be modified
     * @param direction the direction in which the scout action must be
     * carried out
     */
    public void scout(JSONObject parameter, JSONObject decision, Direction direction) {
        parameter.put("direction", direction);
        decision.put("action", "scout");
        decision.put("parameters", parameter);
    }

    /**
     * Interacts with JSONObjects to carry out the echo action.
     *
     * @param parameter the parameter JSON object that stores the additional
     * parameters for the action
     * @param decision  the decision JSON object to be modified
     * @param direction the direction in which the moveTo action must be
     * carried out
     */
    public void moveTo(JSONObject parameter, JSONObject decision, Direction direction) {
        parameter.put("direction", direction);
        decision.put("action", "move_to");
        decision.put("parameters", parameter);
    }
}
