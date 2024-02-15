package ca.mcmaster.se2aa4.island.team106;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.JSONObject;

public class IslandReacher{
    private int tiles; 
    private final Logger logger = LogManager.getLogger();


    public IslandReacher(int tiles){
        this.tiles = tiles; 
    }

    public void fly(Drone drone, JSONObject decision){
        logger.info("TILES REMAINING: " + this.tiles);
        drone.fly(decision);
        this.tiles--;

        if (this.tiles <= 0){
            drone.setStatus(Status.ISLAND_STATE);
        }
    }
}