package ca.mcmaster.se2aa4.island.team106;

public class Drone {
    private int batteryLevel; 
    Direction heading; 

    public Drone(int batteryLevel, Direction heading){
        this.batteryLevel = batteryLevel; 
        this.heading = heading; 
    }
    

    public int getBatteryLevel(){
        return this.batteryLevel; 
    }


    public boolean canMakeDecision(int batteryUsage){
        return (this.batteryLevel - batteryUsage) >= 1; 
    }


    public void useBattery(int batteryUsage){
        this.batteryLevel -= batteryUsage; 
    }
    

    public Direction getHeading(){
        return this.heading; 
    }


    
}
