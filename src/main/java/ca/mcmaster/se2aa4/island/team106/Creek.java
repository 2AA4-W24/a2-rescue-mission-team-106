package ca.mcmaster.se2aa4.island.team106;

public class Creek {
    private String creekID;
    private Point coordinate;



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

    @Override
    public String toString(){
        return "ID: " + this.creekID + " " + this.getCreekPoint();
    }



}
