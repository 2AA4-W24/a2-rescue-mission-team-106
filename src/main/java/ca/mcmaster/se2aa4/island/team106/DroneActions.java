package ca.mcmaster.se2aa4.island.team106;

import org.json.JSONObject;

public interface DroneActions {

    void fly(JSONObject decision);

    void stop(JSONObject decision);

    void land(JSONObject parameter, JSONObject decision);

    Status getStatus();

    void setStatus(Status status);

    void useBattery(int batteryUsage);

    int getBatteryLevel();

    boolean canMakeDecision(int batteryUsage);

}
