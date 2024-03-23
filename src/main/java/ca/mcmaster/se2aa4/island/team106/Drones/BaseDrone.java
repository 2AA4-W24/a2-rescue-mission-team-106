package ca.mcmaster.se2aa4.island.team106.Drones;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team106.DroneTools.Actions;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Direction;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Status;
import ca.mcmaster.se2aa4.island.team106.Exploration.MapArea;





public abstract class BaseDrone{
    protected int minimumBatteryToOperate;
    protected int currentBatteryLevel;
    protected Status status;
    protected Actions action = new Actions();
    protected MapArea mapArea;


    public BaseDrone(int minimumBatteryToOperate,  MapArea mapArea) {
        this.mapArea = mapArea;
        this.minimumBatteryToOperate = minimumBatteryToOperate; 
        this.status = Status.GROUND_FINDER_STATE;
    }
    
    public abstract void updateDrone(int batteryLevel, Direction direction);

    public abstract Status getStatus();

    public abstract void setStatus(Status status);

    public abstract int getBatteryLevel();
    
    public abstract void stop(JSONObject parameter);

    public abstract void fly(JSONObject parameter);

    public abstract boolean canMakeDecision(int batteryUsage);

    public abstract void useBattery(int batteryUsage); 

    public abstract void echo(JSONObject parameter, JSONObject decision, Direction direction);

    public abstract void updateHeading(JSONObject parameter, JSONObject decision, Direction direction);

    public abstract void scan(JSONObject decision); 
}
