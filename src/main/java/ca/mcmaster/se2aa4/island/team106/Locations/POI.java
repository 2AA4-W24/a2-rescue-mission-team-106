package ca.mcmaster.se2aa4.island.team106.Locations;
import java.text.DecimalFormat;


public class POI {
    private String poiID;
    private Point coordinate;
    private DecimalFormat df = new DecimalFormat("#.#####"); // converter used to round decimals


    public POI(Point coordinate, String poiID){
        this.coordinate = coordinate;
        this.poiID = poiID;
    }

    public String getCreekID(){
        return this.poiID;
    }

    public Point getCreekPoint(){
        return this.coordinate;
    }

        
    public double getDistance(POI poi){
        Point pointOne = this.getCreekPoint();
        Point pointTwo = poi.getCreekPoint();


        double distance = Math.sqrt(Math.pow(pointTwo.getXCoordinate()- pointOne.getXCoordinate(), 2) 
        + Math.pow(pointTwo.getYCoordinate() - pointOne.getYCoordinate(), 2));
        return Double.parseDouble(df.format(distance)); // rounded verison to 5 decimals
    }


    public String getID(){
        return this.poiID; 
    }


    @Override
    public String toString(){
        return "ID: " + this.poiID + " " + this.getCreekPoint();
    }



}
