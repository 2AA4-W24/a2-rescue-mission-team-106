package ca.mcmaster.se2aa4.island.team106;
import org.json.JSONObject;

public class Actions {

    public void echo(JSONObject parameter, JSONObject decision, Direction direction)
    {
        parameter.put("direction", direction);
        decision.put("action", "echo");
        decision.put("parameters", parameter);
    }

    public void fly(JSONObject decision){
        decision.put("action", "fly");
    }

    public void stop(JSONObject decision){
        decision.put("action", "stop");
    }

    public void heading(JSONObject parameter, JSONObject decision, Direction direction){
        parameter.put("direction", direction);
        decision.put("action", "heading"); 
        decision.put("parameters", parameter); 
    }
    
}
