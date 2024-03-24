package ca.mcmaster.se2aa4.island.team106.DroneTools;
import org.json.JSONObject;


public class Actions {

    /*******************************************************************************
     * Interacts with JSONObjects to carry out the echo action.
     *
     * @param parameter  the parameter JSON object that stores the additional
     * parameters for the action
     * @param decision   the decision JSON object to be modified
     * @param direction  the direction in which the echo action must be carried out
     *******************************************************************************/
    public void echo(JSONObject parameter, JSONObject decision, Direction direction) {
        parameter.put("direction", direction);
        decision.put("action", "echo");
        decision.put("parameters", parameter);
    }


    /*******************************************************************************
     * Interacts with JSONObjects to carry out the fly action.
     *
     * @param parameter  the parameter JSON object that stores the additional
     * parameters for the action
     * @param decision   the decision JSON object to be modified
     * @param direction  the direction in which the fly action must be carried out
     *******************************************************************************/
    public void fly(JSONObject decision) {
        decision.put("action", "fly");
    }


    /************************************************************
     * Interacts with a JSONObject to carry out the stop action.
     *
     * @param decision  the decision JSON object to be modified
     ************************************************************/
    public void stop(JSONObject decision) {
        decision.put("action", "stop");
    }


    /*************************************************************************
     * Interacts with JSONObjects to carry out the heading action.
     *
     * @param parameter the parameter JSON object that stores the additional
     * parameters for the action
     * @param decision  the decision JSON object to be modified
     * @param direction the direction the drone wants to head towards.
     *************************************************************************/
    public void heading(JSONObject parameter, JSONObject decision, Direction direction) {
        parameter.put("direction", direction);
        decision.put("action", "heading");
        decision.put("parameters", parameter);
    }


    /************************************************************
     * Interacts with a JSONObject to carry out the scan action.
     *
     * @param decision  the decision JSON object to be modified
     ************************************************************/
    public void scan(JSONObject decision) {
        decision.put("action", "scan");
    }
}
