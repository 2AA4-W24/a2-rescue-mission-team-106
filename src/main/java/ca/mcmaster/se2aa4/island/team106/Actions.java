package ca.mcmaster.se2aa4.island.team106;
import org.json.JSONObject;

public class Actions implements DroneActions{

    @Override
    public void echo(JSONObject parameter, JSONObject decision, Direction direction)
    {
        parameter.put("direction", direction);
        decision.put("action", "echo");
        decision.put("parameters", parameter);
    }

    @Override
    public void fly(JSONObject decision){
        decision.put("action", "fly");
    }

    @Override
    public void stop(JSONObject decision){
        decision.put("action", "stop");
    }

    @Override
    public void heading(JSONObject parameter, JSONObject decision, Direction direction){
        parameter.put("direction", direction);
        decision.put("action", "heading");  
        decision.put("parameters", parameter); 
    }

    @Override
    public void scan(JSONObject decision){
        decision.put("action", "scan");
    }

    @Override
    public void land(JSONObject parameter, JSONObject decision){
        decision.put("action", "land");
        decision.put("parameters", parameter);
    }


    @Override
    public void explore(JSONObject decision){
        decision.put("action", "explore");
    }


    @Override
    public void scout(JSONObject parameter, JSONObject decision, Direction direction){
        parameter.put("direction", direction);
        decision.put("action", "scout");  
        decision.put("parameters", parameter); 
    }


    @Override
    public void moveTo(JSONObject parameter, JSONObject decision, Direction direction){
        parameter.put("direction", direction);
        decision.put("action", "move_to");  
        decision.put("parameters", parameter); 
    }
    
}
