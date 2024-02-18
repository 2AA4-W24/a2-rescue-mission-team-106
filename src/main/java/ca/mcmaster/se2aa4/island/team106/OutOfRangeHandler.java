package ca.mcmaster.se2aa4.island.team106;

public class OutOfRangeHandler {
    private boolean danger;
    
    private final int RANGE_BORDER = 2;
    
    
    public void setDanger(int limit, MapArea mapArea) {
        if (limit <= RANGE_BORDER && mapArea.getHeading() == mapArea.getPrevEchoDirection()) {
            this.danger = true;
        } else {
            this.danger = false;
        }
    }

    public void setDanger(boolean danger) {
        this.danger = danger;
    }
    
    public boolean getDanger() {
        return this.danger;
    }
    
    /**
     * Changes the direction if too close to the border
     * 
     * @return Direction to head towards
     */
    public Direction changeDirection(MapArea mapArea) {
        
        if (mapArea.getHeading() == Direction.N) {
            if (mapArea.getEastDistance() < mapArea.getWestDistance()) {
                return Direction.W;
            } else {
                return Direction.E;
            }
        } else if (mapArea.getHeading() == Direction.E){
            if (mapArea.getSouthDistance() < mapArea.getNorthDistance()) {
                return Direction.N;
            } else {
                return Direction.S;
            }
        } else if (mapArea.getHeading() == Direction.W) {
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
