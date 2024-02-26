package ca.mcmaster.se2aa4.island.team106;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class LengthFinder {
    private MapArea mapArea; 
    private int counts = 1; 
    private int length = 0;

    private boolean verifiedGround = false;  // used to verify that signal is OUT_OF_RANGE incase it wasn't updated properly


    private boolean hasLength = false; 

    private final Logger logger = LogManager.getLogger();

    public LengthFinder(MapArea mapArea){
        this.mapArea = mapArea; 
    }


    public int getLength(Drone drone, JSONObject decision, JSONObject parameters){
        
        if (!this.hasLength)
        {
            // this is just one case, that if our rpevious direction was east we wanna echo west, but gotta account for other 3 cases 

            //if our previous heading was east, we have now turned the drone facing (north) now, so want to echo west to esnure ground is beside us 
            // while flying upwards obtaining out length
            if (mapArea.getPrevHeading() == Direction.E)
            {
                
                if (mapArea.getWestEchoResponse() == EchoResponse.GROUND || mapArea.getWestEchoResponse() == null)
                {
    
                    if (this.counts % 3 == 0){
                        drone.fly(decision);
                        this.length++; 
                    }
                    else if (this.counts % 3 == 1){
                        drone.scan(decision);
                    }
                    else if (this.counts % 3 == 2){
                        logger.info("ECHOING WEST");
                        drone.echoWest(parameters, decision);
                    }
    
                    this.counts++;
                }
                else{ // ground is not found so we need to turn our drone

                    if (!this.verifiedGround)
                    {
                        drone.echoWest(parameters, decision);
                        this.verifiedGround = true; 
                    }
                    else{
                        logger.info("Current length of Island: " + this.length);
                        logger.info("Current Direction:  " + mapArea.getHeading());
                        logger.info("Previous Direction:  " + mapArea.getPrevHeading());
                        logger.info("NEXT DIRECTION " + mapArea.getNewHeading());
                        drone.updateHeading(parameters, decision, mapArea.getNewHeading());
                        this.hasLength = true;
                        return this.length;
                    }
                }
            }
            else if (mapArea.getPrevHeading() == Direction.W)
            {
                if (mapArea.getEastEchoResponse() == EchoResponse.GROUND || mapArea.getEastEchoResponse() == null)
                {
    
                    if (this.counts % 3 == 0){
                        drone.fly(decision);
                        this.length++; 
                    }
                    else if (this.counts % 3 == 1){
                        drone.scan(decision);
                    }
                    else if (this.counts % 3 == 2){
                        logger.info("ECHOING EAST");
                        drone.echoEast(parameters, decision);
                    }
    
                    this.counts++;
                }
                else{ // ground is not found so we need to turn our drone

                    if (!this.verifiedGround)
                    {
                        drone.echoEast(parameters, decision);
                        this.verifiedGround = true; 
                    }
                    else{
                        logger.info("Current length of Island: " + this.length);
                        logger.info("Current Direction:  " + mapArea.getHeading());
                        logger.info("Previous Direction:  " + mapArea.getPrevHeading());
                        logger.info("NEXT DIRECTION " + mapArea.getNewHeading());
                        drone.updateHeading(parameters, decision, mapArea.getNewHeading());
                        this.hasLength = true;
                        return this.length;
                    }
                }
            }
            else if(mapArea.getPrevHeading() == Direction.N)
            {
                if (mapArea.getSouthEchoResponse() == EchoResponse.GROUND || mapArea.getSouthEchoResponse() == null)
                {
                    if (this.counts % 3 == 0){
                        drone.fly(decision);
                        this.length++; 
                    }
                    else if (this.counts % 3 == 1){
                        drone.scan(decision);
                    }
                    else if (this.counts % 3 == 2){
                        logger.info("ECHOING EAST");
                        drone.echoSouth(parameters, decision);
                    }
    
                    this.counts++;
                }
                else{ // ground is not found so we need to turn our drone

                    if (!this.verifiedGround)
                    {
                        drone.echoSouth(parameters, decision);
                        this.verifiedGround = true; 
                    }
                    else{
                        logger.info("Current length of Island: " + this.length);
                        logger.info("Current Direction:  " + mapArea.getHeading());
                        logger.info("Previous Direction:  " + mapArea.getPrevHeading());
                        logger.info("NEXT DIRECTION " + mapArea.getNewHeading());
                        drone.updateHeading(parameters, decision, mapArea.getNewHeading());
                        this.hasLength = true;
                        return this.length;
                    }
                }
            }
            else if (mapArea.getPrevHeading() == Direction.S)
            {
                logger.info("CASE WE WANNA COVER! BY THE WAY HERE IS SOME INFO " + mapArea.getNorthEchoResponse());
                if (mapArea.getNorthEchoResponse() == EchoResponse.GROUND || mapArea.getNorthEchoResponse() == null)
                {
                    if (this.counts % 3 == 0){
                        drone.fly(decision);
                        this.length++; 
                    }
                    else if (this.counts % 3 == 1){
                        drone.scan(decision);
                    }
                    else if (this.counts % 3 == 2){
                        logger.info("ECHOING NORTH");
                        drone.echoNorth(parameters, decision);
                    }
    
                    this.counts++;
                }
                else{ // ground is not found so we need to turn our drone
                    logger.info("THIS IS A BIG HIT FROM THE LENGTH GRABBER");

                    if (!this.verifiedGround)
                    {
                        drone.echoNorth(parameters, decision);
                        this.verifiedGround = true; 
                    }
                    else{
                        logger.info("Current length of Island: " + this.length);
                        logger.info("Current Direction:  " + mapArea.getHeading());
                        logger.info("Previous Direction:  " + mapArea.getPrevHeading());
                        logger.info("NEXT DIRECTION " + mapArea.getNewHeading());
                        drone.updateHeading(parameters, decision, mapArea.getNewHeading());
                        this.hasLength = true;
                        return this.length;
                    }
                }
            }
        }
        return this.length;

    }


    public boolean getHasLength(){
        return this.hasLength; 
    }



}
