package ca.mcmaster.se2aa4.island.team106;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.JSONObject;

public class IslandReacher{
    private int tiles; 
    private MapArea mapArea;
    private final Logger logger = LogManager.getLogger();
    
    // private int command = 1; 
    //! You can uncomment the above line when you want to debug and use scan to physically see the trace of the drone on the map
    //! Currently this is commented to save on battery cost 



    public IslandReacher(int tiles, MapArea mapArea){
        this.tiles = tiles; 
        this.mapArea = mapArea; 
    }

    public void setTiles(int tiles){
        this.tiles = tiles; 
    }

    public void fly(Drone drone, JSONObject decision){
        logger.info("TILES REMAINING: " + this.tiles);

        drone.fly(decision);
        this.tiles--;

        //! bottom section commented out to save on battery. 
        //! When we need to test to see where drone physically is on map (scanning) simply un-comment out the bottom portion, and comment out the above 2 lines 

        // if (command % 2 == 1){
        //     drone.fly(decision);
        //     this.tiles--;
        // }
        // else{
        //     // logger.info(""); 
        //     drone.scan(decision);
        // }


        // this.command++;

        if (this.tiles <= 0){
            mapArea.setIsAboveGround(true);
            drone.setStatus(Status.ISLAND_STATE);
        }
    }
}