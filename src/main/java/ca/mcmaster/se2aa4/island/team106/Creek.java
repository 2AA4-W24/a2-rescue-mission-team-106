package ca.mcmaster.se2aa4.island.team106;

public class Creek {
    private String creekID;
    private Point p;
    private int key;



    public Creek(Point p, String creekID){
        this.p = p;
        this.creekID = creekID;
        this.key = p.pointKey();
    }

    public String getCreekID(){
        return this.creekID;
    }

    public Point getCreekPoint(){
        return this.p;
    }

    public Integer getKey(){
        return this.key;
    }


}
