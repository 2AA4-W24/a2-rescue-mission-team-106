package ca.mcmaster.se2aa4.island.team106;
import java.text.DecimalFormat;


public class Creek {
    private String creekID;
    private Point coordinate;
    private DecimalFormat df = new DecimalFormat("#.#####"); // converter used to round decimals


    public Creek(Point coordinate, String creekID){
        this.coordinate = coordinate;
        this.creekID = creekID;
    }

    public String getCreekID(){
        return this.creekID;
    }

    public Point getCreekPoint(){
        return this.coordinate;
    }

        
    public double getDistance(Creek poi){
        Point pointOne = this.getCreekPoint();
        Point pointTwo = poi.getCreekPoint();


        double distance = Math.sqrt(Math.pow(pointTwo.getXCoordinate()- pointOne.getXCoordinate(), 2) 
        + Math.pow(pointTwo.getYCoordinate() - pointOne.getYCoordinate(), 2));
        return Double.parseDouble(df.format(distance)); // rounded verison to 5 decimals
    }


    public String getID(){
        return this.creekID; 
    }


    @Override
    public String toString(){
        return "ID: " + this.creekID + " " + this.getCreekPoint();
    }



}
