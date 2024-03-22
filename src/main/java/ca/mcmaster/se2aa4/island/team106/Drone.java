package ca.mcmaster.se2aa4.island.team106;
import org.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Drone extends BaseDrone {
    private final Logger logger = LogManager.getLogger();


    public Drone(int minimumBatteryToOperate, MapArea mapArea){
        super(minimumBatteryToOperate, mapArea);
    }


    @Override
    public void updateDrone(int batteryLevel, Direction direction){
        this.currentBatteryLevel = batteryLevel; 
        this.mapArea.setHeading(direction);
    }

    
    @Override
    public int getBatteryLevel(){
        return this.currentBatteryLevel; 
    }


    @Override
    public Status getStatus(){
        return this.status; 
    }


    @Override
    public void setStatus(Status status){
        this.status = status; 
    }


    @Override
    public void fly(JSONObject decision){
        action.fly(decision);
        Direction currentHeading = mapArea.getHeading();
        this.mapArea.updateCoordinate(currentHeading);
    }


    @Override
    public void echo(JSONObject parameter, JSONObject decision, Direction direction){
        switch (direction)
        {
            case N: 
                this.echoNorth(parameter, decision);
                break; 
            case E: 
                this.echoEast(parameter, decision);
                break; 
            case S:
                this.echoSouth(parameter, decision);
                break; 
            case W: 
                this.echoWest(parameter, decision);
                break; 
            case FORWARD: 
                this.echoForwards(parameter, decision);
                break;
            default:
                break;
        }
    }


    @Override
    public void stop(JSONObject decision){
        this.action.stop(decision);
    }


    @Override
    public void scan(JSONObject decision){
        this.action.scan(decision);
    }


    @Override
    public void updateHeading(JSONObject parameter, JSONObject decision, Direction updatedHeading){
        logger.info("HERE IS SOME INFORMATION ABOUT WHATS HAPPENING: UPDATED HEADING: " + updatedHeading +" droneHEADING " + this.mapArea.getHeading());
        if (updatedHeading != this.mapArea.getHeading()){
            this.mapArea.setHeading(updatedHeading); // update the status of our drones heading
            this.action.heading(parameter, decision, updatedHeading); // physically update the drone on our map
            this.mapArea.updateCoordinate(updatedHeading); // update the coordinates of the drone
            logger.info("I HAVE OFFICIALLY TURNED THE DRONE! WITH STATUS OF " + mapArea.getHeading());
        }
    }


    @Override
    public boolean canMakeDecision(int batteryUsage){
        return (this.currentBatteryLevel - batteryUsage) >= this.minimumBatteryToOperate; 
    }


    @Override
    public void useBattery(int batteryUsage) {
        this.currentBatteryLevel -= batteryUsage;
    }


    private void echoEast(JSONObject parameter, JSONObject decision){
        if (this.mapArea.getHeading() != Direction.W){
            this.action.echo(parameter, decision, Direction.E);
            this.mapArea.setPrevEchoDirection(Direction.E);
        }
        else{
            this.action.echo(parameter, decision, Direction.W);
            this.mapArea.setPrevEchoDirection(Direction.W);
        }
    }


    private void echoWest(JSONObject parameter, JSONObject decision){
        if (this.mapArea.getHeading() != Direction.E){
            this.action.echo(parameter, decision, Direction.W);
            this.mapArea.setPrevEchoDirection(Direction.W);
        }
        else{
            this.action.echo(parameter, decision, Direction.E);
            this.mapArea.setPrevEchoDirection(Direction.E);
        }
    }


    private void echoNorth(JSONObject parameter, JSONObject decision){
        if (this.mapArea.getHeading() != Direction.S){
            this.action.echo(parameter, decision, Direction.N);
            this.mapArea.setPrevEchoDirection(Direction.N);
        }
        else{
            this.action.echo(parameter, decision, Direction.S);
            this.mapArea.setPrevEchoDirection(Direction.S);
        }
    }


    private void echoSouth(JSONObject parameter, JSONObject decision){
        if (this.mapArea.getHeading() != Direction.N){
            this.action.echo(parameter, decision, Direction.S);
            this.mapArea.setPrevEchoDirection(Direction.S);
        }
        else{
            this.action.echo(parameter, decision, Direction.N);
            this.mapArea.setPrevEchoDirection(Direction.N);
        }
    }


    private void echoForwards(JSONObject parameter, JSONObject decision){
        Direction currentHeading = mapArea.getHeading();
        logger.info("ECHOING DIRECTION : " + currentHeading);
        this.action.echo(parameter, decision, currentHeading);
        this.mapArea.setPrevEchoDirection(currentHeading);
    }

}