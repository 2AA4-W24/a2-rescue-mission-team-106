package ca.mcmaster.se2aa4.island.team106.Exploration;


import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team106.DroneTools.FatalErrorHandler;
import ca.mcmaster.se2aa4.island.team106.DroneTools.State;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Status;
import ca.mcmaster.se2aa4.island.team106.Drones.BaseDrone;
import ca.mcmaster.se2aa4.island.team106.States.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DecisionMaker {
    private final Logger logger = LogManager.getLogger();
    private BaseDrone drone; 

    private State currentState; 

    private State groundFinderState;
    private State centerStartHandlerState;
    private State widthFinderState;
    private State lengthFinderState;
    private State reachCenterState; 
    private State spiralSearchState; 
    
    private MapArea mapArea;
    private FatalErrorHandler fatalErrorHandler;

    //! next test is to set it to groundFinder
    public DecisionMaker(BaseDrone drone, MapArea mapArea, FatalErrorHandler fatalErrorHandler){
        this.drone = drone; 
        this.groundFinderState = new GroundFinderState(mapArea);
        this.centerStartHandlerState = new CenterStartHandlerState(mapArea);
        this.widthFinderState = new WidthFinderState(mapArea);
        this.lengthFinderState = new LengthFinderState(mapArea);
        this.reachCenterState = new ReachCenterState(mapArea);
        this.spiralSearchState = new SpiralSearchState(mapArea);
        this.mapArea = mapArea;
        this.fatalErrorHandler = fatalErrorHandler;
    }


    public void makeDecisions(JSONObject parameters, JSONObject decision) {
        if (this.fatalErrorHandler.getDanger()) {
            this.fatalErrorHandler.handleDanger(decision, parameters);
        } else {
            switch (drone.getStatus())
            {
                case GROUND_FINDER_STATE:
                    logger.info("STATE STATUS " + Status.GROUND_FINDER_STATE);
                    logger.info("DRONE INFORMATION HEADING:  " + mapArea.getHeading());
                    this.currentState = this.groundFinderState; 
                  
                    break;
                case CENTER_START_STATE:
                    logger.info("STATE STATUS " + Status.CENTER_START_STATE);
                    logger.info("DRONE INFORMATION HEADING:  " + mapArea.getHeading());
                    this.currentState = this.centerStartHandlerState; 
                    break;
                case WIDTH_STATE:
                    logger.info("STATE STATUS " + Status.WIDTH_STATE);
                    this.currentState = this.widthFinderState; 
                    break;
                case LENGTH_STATE:
                    logger.info("STATE STATUS " + Status.LENGTH_STATE);
                    this.currentState = this.lengthFinderState; 
                    break;
                case MOVE_CENTER_STATE:
                    logger.info("STATE STATUS " + Status.MOVE_CENTER_STATE);
                    this.currentState = this.reachCenterState; 
                    break;
                case CENTER_STATE:
                    logger.info("STATE STATUS " + Status.CENTER_STATE);
                    this.currentState = this.spiralSearchState;  
                    break;
                default:
                    break;
            }
            this.currentState.handle(drone, decision, parameters);
        }
        
    }
}