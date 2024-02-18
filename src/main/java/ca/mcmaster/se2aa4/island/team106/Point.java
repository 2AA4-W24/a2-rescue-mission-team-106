package ca.mcmaster.se2aa4.island.team106;

import java.text.DecimalFormat;


public class Point {
    private int x; 
    private int y; 
    private DecimalFormat df = new DecimalFormat("#.#####"); // converter used to round decimals


    public Point(int x, int y){
        this.x = x; 
        this.y = y; 
    }


    public void setXCoordinate(int x){
        this.x = x; 
    }


    public void setYCoordinate(int y){
        this.y = y; 
    }
    

    public void setCoordinate(int x, int y){
        this.x = x; 
        this.y = y;
    }

    
    public double getDistance(Point one, Point two){
        double distance = Math.sqrt(Math.pow(two.x - one.x, 2) + Math.pow(two.y - one.y, 2));
        return Double.parseDouble(df.format(distance)); // rounded verison to 5 decimals
    }

    public double getDistance(Point point){
        double distance =  Math.sqrt(Math.pow(this.x - point.x, 2) + Math.pow(this.y - point.y, 2));
        return Double.parseDouble(df.format(distance)); // rounded version to 5 decimals
    }


    public double distanceToOrigin() {
        return Math.sqrt( (this.x * this.x) + (this.y * this.y));
    }

    public int pointKey(){
        return this.x + this.y; 
    }

}
