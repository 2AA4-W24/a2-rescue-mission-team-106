package ca.mcmaster.se2aa4.island.team106;
import org.json.JSONObject;

public class Drone {
    private int batteryLevel;
    private boolean groundStatus;
    private Direction heading; // direction the drone is facing
    private Direction prevHeading; // the previous direction the drone was facing
    private Direction prevEchoDirection;  // the previous echo direction of the drone
    private Status status; 
    private Actions action = new Actions(); 


    public Drone(int batteryLevel, Direction heading){
        this.batteryLevel = batteryLevel; 
        this.heading = heading; 
        this.status = Status.ACTIVE; // drone is now in active status
        this.groundStatus = false;
        this.prevHeading = heading;
    }


    public Status getStatus() {
        return this.status;
    }
    

    public Direction getPrevHeading(){
        return this.prevHeading;
    }

    
    public int getBatteryLevel(){
        return this.batteryLevel; 
    }

    public Direction getPrevEchoDirection(){
        return this.prevEchoDirection;
    }


    public Direction getHeading(){
        return this.heading; 
    }


     // CAN ONLY ECHO EAST IF NOT HEADING EAST
    public void echoEast(JSONObject parameter, JSONObject decision){
        if (heading != Direction.W){
            action.echo(parameter, decision, Direction.E);
            this.prevEchoDirection = Direction.E; 
        }
        else{
            action.fly(decision);
        }
    }


    // CAN ONLY ECHO WEST IF NOT HEADING EAST
    public void echoWest(JSONObject parameter, JSONObject decision){
        if (heading != Direction.E){
            action.echo(parameter, decision, Direction.W);
            this.prevEchoDirection = Direction.W; 
        }
        else{
            action.fly(decision);
        }
    }


    // CAN ONLY ECHO NORTH IF ARE NOT HEADING SOUTH
    public void echoNorth(JSONObject parameter, JSONObject decision){
        if (heading != Direction.S){
            action.echo(parameter, decision, Direction.N);
            this.prevEchoDirection = Direction.N; 
        }
        else{
            action.fly(decision);
        }
    }


    // CAN ONLY ECHO SOUTH IF YOU ARE NOT HEADING NORTH
    public void echoSouth(JSONObject parameter, JSONObject decision){
        if (heading != Direction.N){
            action.echo(parameter, decision, Direction.S);
            this.prevEchoDirection = Direction.S; 
        }
        else{
            action.fly(decision);
        }
    }

    public void echoForwards(JSONObject parameter, JSONObject decision){
        action.echo(parameter, decision, this.heading);
        this.prevEchoDirection = this.heading; 
    }

    
    public void stop(JSONObject decision){
        action.stop(decision);
    }

    public void scan(JSONObject decision){
        action.scan(decision);
    }


    public void land(JSONObject parameter, JSONObject decision){
        action.land(parameter, decision);
    }

    public void explore(JSONObject decision){
        action.explore(decision);
    }

    public void scout(JSONObject parameter, JSONObject decision, Direction direction){
        action.scout(parameter, decision, direction);
        
    }

    public void moveTo(JSONObject parameter, JSONObject decision, Direction direction){
        action.moveTo(parameter, decision, direction);
    }

    
    public void updateHeading(JSONObject parameter, JSONObject decision, Direction updatedHeading){
        if (updatedHeading != this.heading){
            setHeading(updatedHeading); // update the status of our drones heading
            action.heading(parameter, decision, updatedHeading); // physically update the drone on our map
        }
    }


    public void setStatus(Status status){
        this.status = status; 
    }

    public void setHeading(Direction heading) {
        this.prevHeading = this.getHeading();
        this.heading = heading;
    }


    public boolean canMakeDecision(int batteryUsage){
        return (this.batteryLevel - batteryUsage) >= 1; 
    }

    public void useBattery(int batteryUsage) {
        this.batteryLevel -= batteryUsage;
    }
    
    public boolean getGroundStatus() {
        return this.groundStatus;
    }

    public void setGroundStatus(boolean status) {
        this.groundStatus = status;
    }
    
}
