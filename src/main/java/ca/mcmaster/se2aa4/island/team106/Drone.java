package ca.mcmaster.se2aa4.island.team106;

public class Drone {
    private int batteryLevel; 
    private Direction heading; 
    private Direction prevHeading;
    private Status status; 

    public Drone(int batteryLevel, Direction heading){
        this.batteryLevel = batteryLevel; 
        this.heading = heading; 
        this.status = Status.ACTIVE; // drone is now in active status
        this.prevHeading = heading;
    }

    public Status getStatus(){
        return this.status; 
    }
    public Direction getPrevHeading(){
        return this.prevHeading;
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
        this.prevHeading = getHeading();
        this.heading = heading;
    }


    public boolean canMakeDecision(int batteryUsage){
        return (this.batteryLevel - batteryUsage) >= 1; 
    }


    public void useBattery(int batteryUsage){
        this.batteryLevel -= batteryUsage; 
    }
    



    
}
