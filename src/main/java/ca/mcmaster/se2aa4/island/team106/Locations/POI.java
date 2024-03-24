package ca.mcmaster.se2aa4.island.team106.Locations;

import java.text.DecimalFormat;


public class POI {
    private String poiID;
    private Point coordinate;
    private DecimalFormat df = new DecimalFormat("#.#####"); // converter used to round decimals


    /**************************************************************************
     * Constructs a POI object with the given coordinates and UID (unique identifier).
     * 
     * @param coordinate the coordinates of the POI.
     * @param poiID the UID of the POI.
     **************************************************************************/
    public POI(Point coordinate, String poiID) {
        this.coordinate = coordinate;
        this.poiID = poiID;
    }


    /*************************************************************************
     * Obtains the coordinates of the POI.
     * 
     * @return the coordinates of the POI.
     *************************************************************************/
    public Point getPOIPoint() {
        return this.coordinate;
    }


    /**************************************************************************
     * Calculates the distance between this POI and another POI.
     * 
     * @param poi the other POI.
     * @return the distance between this POI and the other POI.
     *************************************************************************/
    public double getDistance(POI poi) {
        Point pointOne = this.getPOIPoint();
        Point pointTwo = poi.getPOIPoint();

        double distance = Math.sqrt(Math.pow(pointTwo.getXCoordinate() - pointOne.getXCoordinate(), 2)
                + Math.pow(pointTwo.getYCoordinate() - pointOne.getYCoordinate(), 2));
        return Double.parseDouble(df.format(distance)); // rounded version to 5 decimals
    }
    

    /*************************************************************************
     * Obtains the UID (unique identifier) of the POI.
     * 
     * @return the UID of the POI.
     *************************************************************************/
    public String getID() {
        return this.poiID;
    }


    /**************************************************************************
     * Returns a string representation of the POI, including its UID (unique
     * identifier) and coordinates.
     *
     * @return a string representation of the POI.
     *************************************************************************/
    @Override
    public String toString() {
        return "ID: " + this.poiID + " " + this.getPOIPoint();
    }
}
