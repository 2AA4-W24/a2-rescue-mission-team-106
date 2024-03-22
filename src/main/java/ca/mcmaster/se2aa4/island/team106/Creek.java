package ca.mcmaster.se2aa4.island.team106;
import java.text.DecimalFormat;


public class Creek {
    private String creekID;
    private Point coordinate;
    private DecimalFormat df = new DecimalFormat("#.#####"); // converter used to round decimals


    /**
     * Constructs a Creek object with the specified coordinates and creek ID.
     *
     * @param coordinate the coordinates of the creek
     * @param creekID the unique identifier of the creek
     */
    public Creek(Point coordinate, String creekID){
        this.coordinate = coordinate;
        this.creekID = creekID;
    }

    /**
     * Gets the coordinates of the creek.
     *
     * @return the coordinates of the creek
     */
    public Point getCreekPoint(){
        return this.coordinate;
    }
        
    /**
     * Calculates the distance between this creek and another creek.
     *
     * @param poi the other creek with which the distance is to be calculated
     * @return the distance between this creek and the other creek
     */
    public double getDistance(Creek poi){
        Point pointOne = this.getCreekPoint();
        Point pointTwo = poi.getCreekPoint();

        double distance = Math.sqrt(Math.pow(pointTwo.getXCoordinate() - pointOne.getXCoordinate(), 2) 
        + Math.pow(pointTwo.getYCoordinate() - pointOne.getYCoordinate(), 2));
        return Double.parseDouble(df.format(distance)); // rounded version to 5 decimals
    }

    /**
     * Gets the ID of the creek.
     *
     * @return the creek ID
     */
    public String getID(){
        return this.creekID; 
    }

    /**
     * Returns a string representation of the creek.
     *
     * @return a string representation of the creek
     */
    @Override
    public String toString(){
        return "ID: " + this.creekID + " " + this.getCreekPoint();
    }
}
