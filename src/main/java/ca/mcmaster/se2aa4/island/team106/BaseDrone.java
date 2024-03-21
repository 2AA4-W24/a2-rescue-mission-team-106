package ca.mcmaster.se2aa4.island.team106;

import org.json.JSONObject;

public abstract class BaseDrone implements DroneActions {
    protected int minimumBatteryToOperate;
    protected int currentBatteryLevel;
    protected Status status;
    protected Actions action = new Actions();
    protected MapArea mapArea;


    public BaseDrone(int MINIMUM_BATTERY_TO_OPERATE, Direction heading, MapArea mapArea) {
        this.mapArea = mapArea;
        this.minimumBatteryToOperate = MINIMUM_BATTERY_TO_OPERATE; 
        this.mapArea.setHeading(heading);
        this.status = Status.START_STATE;
    }

    
    public abstract void updateDrone(int batteryLevel, Direction direction);


    @Override
    public abstract Status getStatus();

    @Override
    public abstract int getBatteryLevel();

    @Override
    public abstract void land(JSONObject parameter, JSONObject decision); 
    
    @Override
    public abstract void stop(JSONObject parameter);

    @Override
    public abstract void fly(JSONObject parameter);

    @Override
    public abstract boolean canMakeDecision(int batteryUsage);

    @Override
    public abstract void useBattery(int batteryUsage); 

    @Override
    public abstract void setStatus(Status status);
}
