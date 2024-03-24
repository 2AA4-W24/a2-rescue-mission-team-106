package ca.mcmaster.se2aa4.island.team106.Locations;


public class Point {
    private int x; 
    private int y; 


    /*************************************************************************
     * Constructs a Point object with the given x and y coordinates.
     * 
     * @param x the x-coordinate of the point.
     * @param y the y-coordinate of the point.
     *************************************************************************/
    public Point(int x, int y) {
        this.x = x; 
        this.y = y; 
    }


    /*************************************************************************
     * Sets the x-coordinate of the point.
     * 
     * @param x the new x-coordinate value.
     *************************************************************************/
    public void setXCoordinate(int x) {
        this.x = x; 
    }


    /*************************************************************************
     * Sets the y-coordinate of the point.
     * 
     * @param y the new y-coordinate value.
     *************************************************************************/
    public void setYCoordinate(int y) {
        this.y = y; 
    }
    
    
    /*************************************************************************
     * Sets both the x and y coordinates of the point.
     * 
     * @param x the new x-coordinate value.
     * @param y the new y-coordinate value.
     *************************************************************************/
    public void setCoordinate(int x, int y) {
        this.x = x; 
        this.y = y;
    }


    /*************************************************************************
     * Obtains the x-coordinate of the point.
     * 
     * @return the x-coordinate of the point.
     *************************************************************************/
    public int getXCoordinate() {
        return this.x; 
    }


    /*************************************************************************
     * Obtains the y-coordinate of the point.
     * 
     * @return the y-coordinate of the point.
     *************************************************************************/
    public int getYCoordinate() {
        return this.y; 
    }


    /************************************************************************************
     * Returns a string representation of the point in the format "Coordinates: <x, y>".
     * 
     * @return a string representation of the point.
     ************************************************************************************/
    @Override
    public String toString() {
        return "Coordinates: < " + this.x + " , " + this.y + " >";
    }
}
