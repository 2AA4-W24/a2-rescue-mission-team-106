package ca.mcmaster.se2aa4.island.team106;

import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GridSearch {

    private MapArea mapArea; 
    // private boolean trial = false; 
    private boolean aboveGround = true; 
    private int counts = 1; 

    private int turnDownCounts = 0; 
    private int uTurnCounter = 0; 
    private boolean onDoubleEcho = true; 
    private boolean isOnUTurn = false; 

    private final Logger logger = LogManager.getLogger();


    public GridSearch(MapArea mapArea){
        this.mapArea = mapArea; 
    }

    public void gridSearchIsland(Drone drone, JSONObject decision, JSONObject parameters){

        if (mapArea.getIsAboveGround())
        {
            logger.info("ABOVE GROUND RIGHT NOW");

            if (this.counts % 3 == 0){
                logger.info("FLYING FORWARDS");
                drone.fly(decision);
            }
            else if (this.counts % 3 == 1){
                logger.info("ECHOING FORWARDS");
                drone.echoForwards(parameters, decision);
            }
            else if (this.counts % 3 == 2){
                logger.info("SCANNING");
                drone.scan(decision);
            }
            this.counts++;
        }
        else{
            logger.info("NO LONGER ABOVE GROUND");
            logger.info("KILLING PROCESS");
            drone.stop(decision);

            if (mapArea.getHeading() == Direction.S){
                
                // echo both sides to see where ground is
                if (this.onDoubleEcho)
                {
                    if (this.turnDownCounts % 2 == 0){
                        drone.echoEast(parameters, decision);
                    }
                    else if (this.turnDownCounts % 2 == 1){
                        drone.echoSouth(parameters, decision); // at this point if east didnt return ground this is guaranteed to return ground
                    }

                    

                    this.turnDownCounts++;
                }
                else
                {
                    if (this.isOnUTurn)
                    {
                        if (this.uTurnCounter % 2 == 0)
                        {
                            drone.updateHeading(parameters, decision, mapArea.getHeading());
                        }
                        else if(this.uTurnCounter % 2 == 1){
                            drone.updateHeading(parameters, decision, Direction.N);
                            this.isOnUTurn = false; 
                        }
                        this.uTurnCounter++; 
                
                    }
                    
                }



            }
        }
        
    }

}
