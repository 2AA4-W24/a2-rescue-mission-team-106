package ca.mcmaster.se2aa4.island.team106;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class WidthFinder {

    private MapArea mapArea; 
    private int counts = 1; 
    private int width = 1;
    private boolean hasWidth = false; 

    private final Logger logger = LogManager.getLogger();


    public WidthFinder(MapArea mapArea){
        this.mapArea = mapArea; 
    }

    public int getWidth(Drone drone, JSONObject decision, JSONObject parameters)
    {

        // At this point, our drone has already made a turn. If we our previous heading was South, we need to verify that after we turned, ground is still above us (north)

        if (!hasWidth)
        {

            if (mapArea.getPrevHeading() == Direction.S)
            {
                logger.info("CURRENT HEADING: " + mapArea.getHeading());
    
                // we have a ground response meaning we may keep proceeding to obtaint he width of the island
                logger.info("CURRENT ECHO REPSONSE : " + mapArea.getNorthEchoResponse());
                if (mapArea.getNorthEchoResponse() == EchoResponse.GROUND || mapArea.getNorthEchoResponse() == null){
    
                    if (this.counts % 3 == 0){
                        drone.fly(decision);
                        this.width++; 
                    }
                    else if (this.counts % 3 == 1){
                        drone.scan(decision);
                    }
                    else if (this.counts % 3 == 2){
                        drone.echoNorth(parameters, decision);
                    }
    
                    this.counts++;
                } 
                // in the case of no longer a ground response we want to turn
                else{
                    logger.info("Current Width of Island: " + this.width);
                    logger.info("Current Direction:  " + mapArea.getHeading());
                    logger.info("Previous Direction:  " + mapArea.getPrevEchoDirection());
                    logger.info("NEXT DIRECTION " + mapArea.getNewHeading());
                    drone.updateHeading(parameters, decision, mapArea.getNewHeading());
                    this.hasWidth = true;
                    return this.width;
                }
            }
            else if (mapArea.getPrevHeading() == Direction.N)
            {
                logger.info("CURRENT HEADING: " + mapArea.getHeading());
    
                // we have a ground response meaning we may keep proceeding to obtaint he width of the island
                logger.info("CURRENT ECHO REPSONSE : " + mapArea.getSouthEchoResponse());
                if (mapArea.getSouthEchoResponse() == EchoResponse.GROUND || mapArea.getSouthEchoResponse() == null){
    
                    if (this.counts % 3 == 0){
                        drone.fly(decision);
                        this.width++; 
                    }
                    else if (this.counts % 3 == 1){
                        drone.scan(decision);
                    }
                    else if (this.counts % 3 == 2){
                        drone.echoSouth(parameters, decision);
                    }
    
                    this.counts++;
                } 
                // in the case of no longer a ground response we want to turn
                else{
                    logger.info("Current Width of Island: " + this.width);
                    logger.info("Current Direction:  " + mapArea.getHeading());
                    logger.info("Previous Direction:  " + mapArea.getPrevEchoDirection());
                    logger.info("NEXT DIRECTION " + mapArea.getNewHeading());
                    drone.updateHeading(parameters, decision, mapArea.getNewHeading());
                    this.hasWidth = true;
                    return this.width;
                }
            }
            else if (mapArea.getPrevHeading() == Direction.E)
            {
                if (mapArea.getWestEchoResponse() == EchoResponse.GROUND || mapArea.getWestEchoResponse() == null)
                {
                    if (this.counts % 3 == 0){
                        drone.fly(decision);
                        this.width++; 
                    }
                    else if (this.counts % 3 == 1){
                        drone.scan(decision);
                    }
                    else if (this.counts % 3 == 2){
                        drone.echoWest(parameters, decision);
                    }
    
                    this.counts++;
                }
                else{
                    logger.info("Current Width of Island: " + this.width);
                    logger.info("Current Direction:  " + mapArea.getHeading());
                    logger.info("Previous Direction:  " + mapArea.getPrevEchoDirection());
                    logger.info("NEXT DIRECTION " + mapArea.getNewHeading());
                    drone.updateHeading(parameters, decision, mapArea.getNewHeading());
                    this.hasWidth = true;
                    return this.width;
                }
            }
            else if (mapArea.getPrevHeading() == Direction.W)
            {
                if (mapArea.getEastEchoResponse() == EchoResponse.GROUND || mapArea.getEastEchoResponse() == null)
                {
                    
                    if (this.counts % 3 == 0){
                        drone.fly(decision);
                        this.width++; 
                    }
                    else if (this.counts % 3 == 1){
                        drone.scan(decision);
                    }
                    else if (this.counts % 3 == 2){
                        drone.echoEast(parameters, decision);
                    }
    
                    this.counts++;
                }
            }
        }
        return this.width;

    }

    public boolean getHasWidth(){
        return this.hasWidth; 
    }

}
