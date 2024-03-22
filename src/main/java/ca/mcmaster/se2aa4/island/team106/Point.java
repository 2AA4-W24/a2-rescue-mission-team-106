package ca.mcmaster.se2aa4.island.team106;

public class Point {
    private int x; 
    private int y; 

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

    public int getXCoordinate(){
        return this.x; 
    }

    public int getYCoordinate(){
        return this.y; 
    }

    @Override
    public String toString(){
        return "Coordinates: < " + this.x + " , " + this.y + " >";
    }
}
