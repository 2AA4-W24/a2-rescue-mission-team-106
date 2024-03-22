package ca.mcmaster.se2aa4.island.team106.States;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team106.DroneTools.DimensionFinder;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Direction;
import ca.mcmaster.se2aa4.island.team106.DroneTools.State;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Status;
import ca.mcmaster.se2aa4.island.team106.Drones.BaseDrone;
import ca.mcmaster.se2aa4.island.team106.Exploration.MapArea;


public class WidthFinderState implements DimensionFinder, State{

    private MapArea mapArea; 
    private int counts = 1; 

    private final Logger logger = LogManager.getLogger();


    public WidthFinderState(MapArea mapArea){
        this.mapArea = mapArea; 
    }

    @Override
    public void handle(BaseDrone drone, JSONObject decision, JSONObject parameters){
        this.getDimension(drone, decision, parameters);
    }


    @Override
    public void getDimension(BaseDrone drone, JSONObject decision, JSONObject parameters){
        Direction groundDirection = mapArea.getGroundEchoDirection(); 


        if (mapArea.hasObtainedLength() && !mapArea.getIsAbove()  && !mapArea.hasObtainedWidth()){

            if (mapArea.getHeading() == Direction.E && mapArea.getEastDistance() > 0)
            {
                drone.fly(decision);
                int newEastDistance = mapArea.getEastDistance() -1;
                mapArea.setEastDistance(newEastDistance);

                if (mapArea.getEastDistance() == 0){
                    mapArea.setIsAbove(true);
                    int startPoint = mapArea.getDroneX();
                    mapArea.setWidthStartPoint(startPoint);
                } 
            }
            else if (mapArea.getHeading() == Direction.W && mapArea.getWestDistance() > 0){
                drone.fly(decision);
                int newWestDistance = mapArea.getWestDistance() -1;
                mapArea.setWestDistance(newWestDistance);

                if (mapArea.getWestDistance() == 0){
                    mapArea.setIsAbove(true);
                    int startPoint = mapArea.getDroneX();
                    mapArea.setWidthStartPoint(startPoint);
                }
            }
            else {
                mapArea.setWidthEndPoint(mapArea.getDroneX());
                
                mapArea.getWidthOfIsland(); //internal mapArea memory we dont need to return this no relevance as its gonna be reffered to later via mapArea

                mapArea.setObtainedWidth(true); // now we have obtained the length
                echo(drone, groundDirection, decision, parameters);
            }
        }
        else if (this.mapArea.getIsAbove())
        {
            this.moveDrone(drone, groundDirection, decision, parameters);
        }
        else{
            mapArea.setWidthEndPoint(mapArea.getDroneX());
            
            mapArea.setObtainedWidth(true); // now we have obtained the width

            // if we havent obtained the length, we need to transition to length state, and update our heading 

            if (!mapArea.hasObtainedLength())
            {
                drone.updateHeading(parameters, decision, groundDirection);

                Direction previousDirection = mapArea.getPrevHeading(); 

                this.setNewEchoGroundDirection(previousDirection);

                drone.setStatus(Status.LENGTH_STATE);
            }
            else{
                //! in this scenario, we would have already obtained our width 
                //! we transition into a new state that makes our drone go to the middle of the island
                logger.info("Both length and width have been found terminating for now!");
                // drone.stop(decision);
                // If we have found both the width and the length, we need to
                // transition into the move to center state where we will move
                // to the center point of the island. We will now update our
                // heading to turn into the direction of the last echo.
                drone.setStatus(Status.MOVE_CENTER_STATE);
                logger.info("State Changed to:" + Status.MOVE_CENTER_STATE);
                drone.updateHeading(parameters, decision, groundDirection);
            }

        }
    }

    private void moveDrone(BaseDrone drone, Direction direction, JSONObject decision, JSONObject parameters){
        switch (this.counts % 3) {
            case 1:
                echo(drone, direction, decision, parameters);
                break;
            case 2:
                drone.scan(decision);
                break;
            case 0:
                drone.fly(decision);
                break;
            default: break; 
        }
        
        this.counts++;

    }

    private void echo(BaseDrone drone, Direction direction, JSONObject decision, JSONObject parameters){
        switch (direction) {
            case N:
            case S:
                drone.echo(parameters, decision, direction);
                break;
            default:
                logger.info("This was an invalid echo attempted: " + direction);
                break;
        }        
    }


    private void setNewEchoGroundDirection(Direction priorDirection){
        switch (priorDirection) {
            case E:
                mapArea.setGroundEchoDirection(Direction.W);
                break;
            case W:
                mapArea.setGroundEchoDirection(Direction.E);
                break;
            default:
                logger.info("Invalid echo direction, your prior direction should be E or W but it was: " + priorDirection);
                break;
        }
    }

}
