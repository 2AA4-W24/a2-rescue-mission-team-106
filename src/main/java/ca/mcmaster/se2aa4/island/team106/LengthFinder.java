package ca.mcmaster.se2aa4.island.team106;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class LengthFinder {
    private MapArea mapArea; 
    private int counts = 1; 



    private final Logger logger = LogManager.getLogger();

    public LengthFinder(MapArea mapArea){
        this.mapArea = mapArea; 
    }


    public void getLengthOfIsland(Drone drone, JSONObject decision, JSONObject parameters){
        Direction groundDirection = mapArea.getGroundEchoDirection(); // Guaranteed to be East or West

        if (mapArea.hasObtainedWidth() && !mapArea.getIsAbove()){
            logger.info("AT THIS POINT IVE OBTAINED MY WIDTH AND MY GET ISABOVE SHOULD BE FALSE: " + mapArea.hasObtainedWidth() +" " + mapArea.getIsAbove());
            if (mapArea.getHeading() == Direction.S && mapArea.getSouthDistance() > 0)
            {
                drone.fly(decision);
                int newSouthDistance = mapArea.getSouthDistance() -1;
                mapArea.setSouthDistance(newSouthDistance);
                logger.info("NEW SOUTH DISTANCE: " + newSouthDistance);

                if (mapArea.getSouthDistance() == 0){
                    mapArea.setIsAbove(true);
                } 
            }
            else if (mapArea.getHeading() == Direction.N && mapArea.getNorthDistance() > 0){
                drone.fly(decision);
                int newNorthDistance = mapArea.getNorthDistance() -1;
                mapArea.setNorthDistance(newNorthDistance);

                if (mapArea.getNorthDistance() == 0){
                    mapArea.setIsAbove(true);
                }
            }
            else{
                logger.info("i'm in the zoo with the lions and apes and bears !");
                drone.stop(decision);
            }
        }
        else if (this.mapArea.getIsAbove())
        {
            if (this.counts % 3 == 1){
                echo(drone, groundDirection, decision, parameters);
            }
            else if (this.counts % 3 == 2){
                drone.scan(decision);
            }
            else if (this.counts % 3 == 0){
                drone.fly(decision);
            }
            this.counts++;
        }
        else{
            logger.info("I have now obtained my LENGTH");
            mapArea.setLengthEndPoint(mapArea.getDroneY());
            
            logger.info("Length of island achieved which is now: " + mapArea.getLengthOfIsland());

            mapArea.getLengthOfIsland(); //internal mapArea memory we dont need to return this no relevance as its gonna be reffered to later via mapArea

            mapArea.setObtainedLength(true); // now we have obtained the length

            // if we havent obtained the width, we need to transition to width state, and update our heading 

            if (!mapArea.hasObtainedWidth())
            {
                logger.info("WE have not found the width yet so now we are transitioning into the width state");
                drone.updateHeading(parameters, decision, groundDirection);

                Direction previousDirection = mapArea.getPrevHeading(); 

                this.setNewEchoGroundDirection(previousDirection);

                drone.setStatus(Status.WIDTH_STATE);
            }
            else{
                //! in this scenario, we would have already obtained our width 
                //! we transition into a new state that makes our drone go to the middle of the island
                logger.info("Both length and width have been found terminating for now!");
                drone.stop(decision);
            }
        }
    }


    private void echo(Drone drone, Direction direction, JSONObject decision, JSONObject parameters){
        if (direction == Direction.W){
            drone.echoWest(parameters, decision);
        }
        else if(direction == Direction.E){
            drone.echoEast(parameters, decision);
        }
        else{
            logger.info("This was an invalid echo attempted: " + direction);
        }
    }


    private void setNewEchoGroundDirection(Direction priorDirection){
        if (priorDirection == Direction.N){
            mapArea.setGroundEchoDirection(Direction.S);
        }
        else if (priorDirection == Direction.S){
            mapArea.setGroundEchoDirection(Direction.N);
        }
        else{
            logger.info("Invalid echo direction, your prior direction should be E or W but it was: " + priorDirection);
        }
    }




}
