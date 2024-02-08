package ca.mcmaster.se2aa4.island.team106;
import org.json.JSONObject;

public class Drone {
    private int batteryLevel; 
    private Direction heading; 
    private Status status; 

    public Drone(int batteryLevel, Direction heading){
        this.batteryLevel = batteryLevel; 
        this.heading = heading; 
        this.status = Status.ACTIVE; // drone is now in active status
    }

    public Status getStatus(){
        return this.status; 
    }


    // CAN ONLY ECHO EAST IF NOT HEADING EAST
    public void echoEast(JSONObject parameter, JSONObject decision){
        if (heading != Direction.W)
        {
            parameter.put("direction", "E");
            decision.put("action", "echo");
            decision.put("parameters", parameter);
        }
    }

    // CAN ONLY ECHO WEST IF NOT HEADING EAST
    public void echoWest(JSONObject parameter, JSONObject decision){
        if (heading != Direction.E)
        {
            parameter.put("direction", "W");
            decision.put("action", "echo");
            decision.put("parameters", parameter);
        }
    }

    // CAN ONLY ECHO NORTH IF ARE NOT HEADING SOUTH
    public void echoNorth(JSONObject parameter, JSONObject decision){
        if (heading != Direction.S)
        {
            parameter.put("direction", "N");
            decision.put("action", "echo");
            decision.put("parameters", parameter);
        }
    }

    // CAN ONLY ECHO SOUTH IF YOU ARE NOT HEADING NORTH
    public void echoSouth(JSONObject parameter, JSONObject decision){
        if (heading != Direction.N)
        {
            parameter.put("direction", "S");
            decision.put("action", "echo");
            decision.put("parameters", parameter);
        }
    }
    

    public int getBatteryLevel(){
        return this.batteryLevel; 
    }

    public Direction getHeading(){
        return this.heading; 
    }

    public void setStatus(Status status){
        this.status = status; 
    }

    public void setHeading(Direction heading){
        this.heading = heading;
    }


    public boolean canMakeDecision(int batteryUsage){
        return (this.batteryLevel - batteryUsage) >= 1; 
    }


    public void useBattery(int batteryUsage){
        this.batteryLevel -= batteryUsage; 
    }
    



    
}
