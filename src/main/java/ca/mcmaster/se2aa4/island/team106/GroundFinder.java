package ca.mcmaster.se2aa4.island.team106;

import org.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GroundFinder {
    private int counts = 1; 
    private boolean danger = false;

    private final Logger logger = LogManager.getLogger();


    public void setDanger(boolean newDanger){
        this.danger = newDanger; 
    }
    

    public void fly(Drone drone, JSONObject decision, JSONObject parameters)
    {
        if (!drone.getGroundStatus() && ! this.danger) // danger is still relevant but I do not think ground status has any relevance at this point
            {
                if (this.counts % 5  == 0){
                    drone.fly(decision);
                }
                else if (this.counts % 5 == 1){
                    logger.info("ECHOING EAST");
                    drone.echoEast(parameters, decision);
                }
                else if (this.counts % 5 == 2){
                    logger.info("ECHOING SOUTH");
                    drone.echoSouth(parameters, decision);
                }
                else if(this.counts % 5 == 3){
                    logger.info("ECHOING NORTH");
                    drone.echoNorth(parameters, decision);
                }
                else if(this.counts % 5 == 4){
                    logger.info("ECHOING WEST");
                    drone.echoWest(parameters, decision);
                }
            } 
            this.counts++;      
    }

}
