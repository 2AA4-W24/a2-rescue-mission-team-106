package ca.mcmaster.se2aa4.island.team106;

public class OutOfRange {
    private MapArea mapArea;
    private Direction currentDirection;
    private boolean danger;
    
    public final int RANGE_BORDER = 40;
    
    public OutOfRange(MapArea mapArea) {
        this.mapArea = mapArea;
        this.currentDirection = mapArea.getHeading();
    }
    
    public void setDanger(int limit) {
        if (limit <= RANGE_BORDER && mapArea.getHeading() == mapArea.getPrevEchoDirection()) {
            danger = true;
        } else {
            danger = false;
        }
    }
    
    public boolean getDanger() {
        return danger;
    }
    
    /**
     * Changes the direction if too close to the border
     * 
     * @return Direction to head towards
     */
    public Direction changeDirection() {
        
        if (currentDirection == Direction.N) {
            if (mapArea.getEastDistance() < mapArea.getWestDistance()) {
                return Direction.W;
            } else {
                return Direction.E;
            }
        } else if (currentDirection == Direction.E){
            if (mapArea.getSouthDistance() < mapArea.getNorthDistance()) {
                return Direction.N;
            } else {
                return Direction.S;
            }
        } else if (currentDirection == Direction.W) {
            if (mapArea.getNorthDistance() < mapArea.getSouthDistance()) {
                return Direction.S;
            } else {
                return Direction.N;
            }
        } else {
            if (mapArea.getWestDistance() < mapArea.getEastDistance()) {
                return Direction.E;
            } else {
                return Direction.W;
            }
        }        
    }

    


}
