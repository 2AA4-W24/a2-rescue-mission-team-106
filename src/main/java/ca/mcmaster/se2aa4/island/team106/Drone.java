package ca.mcmaster.se2aa4.island.team106;

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
    

    public int getBatteryLevel(){
        return this.batteryLevel; 
    }

    public Direction getHeading(){
        return this.heading; 
    }

    public void setStatus(Status status){
        this.status = status; 
    }


    public boolean canMakeDecision(int batteryUsage){
        return (this.batteryLevel - batteryUsage) >= 1; 
    }


    public void useBattery(int batteryUsage){
        this.batteryLevel -= batteryUsage; 
    }
    



    
}
