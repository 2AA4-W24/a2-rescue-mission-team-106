package ca.mcmaster.se2aa4.island.team106.Drones;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team106.DroneTools.Direction;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Status;
import ca.mcmaster.se2aa4.island.team106.Exploration.MapArea;

public class Drone extends BaseDrone {

    /**
     * Constructs a Drone with the specified minimum battery level required to
     * operate and map area.
     *
     * @param minimumBatteryToOperate the minimum battery level required to
     * operate
     * @param mapArea the map area on which the drone operates on
     */
    public Drone(int minimumBatteryToOperate, MapArea mapArea){
        super(minimumBatteryToOperate, mapArea);
    }

    /**
     * Updates the drone based on the current battery level and the provided direction.
     *
     * @param batteryLevel the current battery level
     * @param direction the current direction of the drone
     */
    @Override
    public void updateDrone(int batteryLevel, Direction direction){
        this.currentBatteryLevel = batteryLevel; 
        this.mapArea.setHeading(direction);
    }

    /**
     * Gets the current battery level of the drone.
     *
     * @return the current battery level of the drone
     */
    @Override
    public int getBatteryLevel(){
        return this.currentBatteryLevel; 
    }

    /**
     * Gets the status of the drone.
     *
     * @return the status of the drone
     */
    @Override
    public Status getStatus(){
        return this.status; 
    }

    /**
     * Sets the status of the drone.
     *
     * @param status the new status of the drone
     */
    @Override
    public void setStatus(Status status){
        this.status = status; 
    }

    /**
     * Carries out the fly action for the drone.
     *
     * @param decision  the decision JSON object to be modified
     */
    @Override
    public void fly(JSONObject decision){
        action.fly(decision);
        Direction currentHeading = mapArea.getHeading();
        this.mapArea.updateCoordinate(currentHeading);
        this.mapArea.setCurrentAction("fly");
    }

    /**
     * Carries out the echo action for the drone.
     *
     * @param parameter the parameter JSON object that stores the additional
     * parameters for the action
     * @param decision  the decision JSON object to be modified
     * @param direction the direction for the echo action
     */
    @Override
    public void echo(JSONObject parameter, JSONObject decision, Direction direction){
        switch (direction) {
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
        this.mapArea.setCurrentAction("echo");
    }

    /**
     * Carries out the stop action for the drone.
     *
     * @param parameter the parameter JSON object that stores the additional
     * parameters for the action
     */
    @Override
    public void stop(JSONObject decision){
        this.action.stop(decision);
        this.mapArea.setCurrentAction("stop");
    }

    /**
     * Carries out the scan action for the drone.
     *
     * @param decision  the decision JSON object to be modified
     */
    @Override
    public void scan(JSONObject decision){
        this.action.scan(decision);
        this.mapArea.setCurrentAction("scan");
    }

    /**
     * Carries out the heading action for the drone.
     *
     * @param parameter the parameter JSON object that stores the additional
     * parameters for the action
     * @param decision  the decision JSON object to be modified
     * @param direction the new direction the drone should face
     */
    @Override
    public void updateHeading(JSONObject parameter, JSONObject decision, Direction updatedHeading){
        if (updatedHeading != this.mapArea.getHeading()){
            this.mapArea.setHeading(updatedHeading); // update the status of our drones heading
            this.action.heading(parameter, decision, updatedHeading); // physically update the drone on our map
            this.mapArea.updateCoordinate(updatedHeading); // update the coordinates of the drone
            this.mapArea.setCurrentAction("heading");
        }
    }

    /**
     * Checks if the drone can make a decision based on the given battery usage
     * of an action.
     *
     * @param batteryUsage the amount of battery used by the decision
     * @return true if the drone can make the decision, otherwise false
     */
    @Override
    public boolean canMakeDecision(int batteryUsage) {
        return (this.currentBatteryLevel - batteryUsage) >= this.minimumBatteryToOperate;
    }

    /**
     * Update battery level of drone after performing the action
     *
     * @param batteryUsage the amount of battery used by the action
     */
    @Override
    public void useBattery(int batteryUsage) {
        this.currentBatteryLevel -= batteryUsage;
    }

    /**
     * Carries out the echo action in the East direction for the drone.
     *
     * @param parameter the parameter JSON object that stores the additional
     * parameters for the action
     * @param decision  the decision JSON object to be modified
     */
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

    /**
     * Carries out the echo action in the West direction for the drone.
     *
     * @param parameter the parameter JSON object that stores the additional
     * parameters for the action
     * @param decision  the decision JSON object to be modified
     */
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

    /**
     * Carries out the echo action in the North direction for the drone.
     *
     * @param parameter the parameter JSON object that stores the additional
     * parameters for the action
     * @param decision  the decision JSON object to be modified
     */
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

    /**
     * Carries out the echo action in the South direction for the drone.
     *
     * @param parameter the parameter JSON object that stores the additional
     * parameters for the action
     * @param decision  the decision JSON object to be modified
     */
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

    /**
     * Carries out the echo action in the Forwards direction for the drone.
     *
     * @param parameter the parameter JSON object that stores the additional
     * parameters for the action
     * @param decision  the decision JSON object to be modified
     */
    private void echoForwards(JSONObject parameter, JSONObject decision){
        Direction currentHeading = mapArea.getHeading();
        this.action.echo(parameter, decision, currentHeading);
        this.mapArea.setPrevEchoDirection(currentHeading);
    }

}