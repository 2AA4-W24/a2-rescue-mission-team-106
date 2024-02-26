package ca.mcmaster.se2aa4.island.team106;

import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IslandEndReacher {

    private MapArea mapArea; 
    private int counts = 1; 
    private boolean atEndOfIsland = false; 
    

    private int turnDownCounts = 0; 

    //! new code for trial
    private boolean firstTime = true; // our first time to verify where ground is so we echo in 2 directions above and bellow  wherever we face
    private boolean needToTurn = false; // this will be set to true when we need to turn after we have reached the end of the island (turn in the ground direction)
    private int internalCounter = 0; 
    private int regularCounter = 0; 

    private final Logger logger = LogManager.getLogger();


    public IslandEndReacher(MapArea mapArea){
        this.mapArea = mapArea; 
    }

    public void reachEndOfIsland(Drone drone, JSONObject decision, JSONObject parameters){
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
            if (mapArea.getHeading() == mapArea.getNewHeading())
            {
                if (mapArea.getHeading() == Direction.S || mapArea.getHeading() == Direction.N)
                {
                    logger.info("If this is being hit that means we are currently facing south and our new direction is " + mapArea.getNewHeading() );
                    
                    if (this.turnDownCounts % 2 == 0){
                        logger.info("ECHOING EAST");
                        drone.echoEast(parameters, decision);
                    }
                    else if (this.turnDownCounts % 2 == 1){
                        drone.echoWest(parameters, decision); // at this point if east didnt return ground this is guaranteed to return ground
                    }
                }
                else if (mapArea.getHeading() == Direction.E|| mapArea.getHeading() == Direction.W)
                {
                    if (this.turnDownCounts % 2 == 0){
                        logger.info("ECHOING NORTH");
                        drone.echoNorth(parameters, decision);
                    }
                    else if (this.turnDownCounts % 2 == 1){
                        drone.echoSouth(parameters, decision); // at this point if north didnt return ground this is guaranteed to return ground
                    }
                }
                this.turnDownCounts++;
            }
            else{
                logger.info("Well now we are able to stop the drone properly, theoretically later we will actually, physically update the drone to thew new heading");
                
                // here we have reached the end of the island so update our heading so now we can obtain the length dimensions
                drone.updateHeading(parameters, decision, mapArea.getNewHeading());
                this.atEndOfIsland = true; 
            }
        }
        

        // //! ALL NEW CODE FOR TRIAL
        // if (this.firstTime)
        // {
        //     // if we face south or north we want to BESIDE US...echo east and west
        //     logger.info("HEADING " + mapArea.getHeading());
        //     if (mapArea.getHeading() == Direction.S || mapArea.getHeading() == Direction.N)
        //     {
        //         if (this.internalCounter % 2 == 0){
        //             logger.info("ECHO BAD EAST " + this.firstTime);
        //             drone.echoEast(parameters, decision);
        //         }
        //         else if (this.internalCounter % 2 == 1){
        //             logger.info("ECHO BAD WEST WITH INFO OF " + this.firstTime);

        //             drone.echoWest(parameters, decision);
        //             this.firstTime = false; // AT THIS POINT IF EAST DIDNT WORK THEN ITS GUARNATEED THAT WEST WILL
        //         }
        //         this.internalCounter++; 

        //     }
        //     // if we face east or west we want to echo ABOVE OR VELLOW US... echo south and north
        //     else if (mapArea.getHeading() == Direction.W || mapArea.getHeading() == Direction.E)
        //     {
        //         if (this.internalCounter % 2 == 0){
        //             drone.echoNorth(parameters, decision);
        //         }
        //         else if (this.internalCounter % 2 == 1){
        //             drone.echoSouth(parameters, decision);
        //         }
        //         this.internalCounter++; 
        //     }

        // }
        // else if(!this.needToTurn)
        // {
        //     logger.info("THIS SHOULD BE CHANGED " + this.needToTurn);
        //     if (this.regularCounter % 2 == 0){
        //         drone.fly(decision);
        //     }
        //     else if (this.regularCounter % 2 == 1)
        //     {
        //         echoGroundDirection(drone, decision, parameters);
        //     }

        //     this.regularCounter++; 
        // }
        // else if (this.needToTurn && !this.firstTime){
        //     logger.info("Well now we are able to stop the drone properly, theoretically later we will actually, physically update the drone to thew new heading");
                
        //     // here we have reached the end of the island so update our heading so now we can obtain the length dimensions
        //     drone.updateHeading(parameters, decision, mapArea.getGroundEchoDirection());
        //     this.atEndOfIsland = true; 
        //     drone.stop(decision);
        // }
    }


    private void echoGroundDirection(Drone drone, JSONObject decision, JSONObject parameters)
    {
        Direction groundEchoDirection = mapArea.getGroundEchoDirection();

        if (groundEchoDirection == Direction.N){
            drone.echoNorth(parameters, decision);
        }
        else if (groundEchoDirection == Direction.S){
            drone.echoSouth(parameters, decision);
        }
        else if(groundEchoDirection == Direction.W){
            drone.echoWest(parameters, decision);
        }
        else if (groundEchoDirection == Direction. E){
            drone.echoEast(parameters, decision);
        }
    }


    // public void setVerifiedEchoGroundDirection(){

    // }


    public void setFirstTime(boolean flag){
        logger.info("REACHHHHHHHHHHHHHHHHHHHHHHHHHHH");
        this.firstTime = flag; 
        logger.info("VALUE IS " + this.firstTime);
    }

    public void setNeedToTurn(boolean flag){
        this.needToTurn = flag; 
    }

    public boolean isFirstTime() {
        return this.firstTime;
    }
    
    public boolean isNeedToTurn() {
        return this.needToTurn;
    }
    




    public boolean getAtEndOfIsland(){
        return this.atEndOfIsland;
    }
    
}
