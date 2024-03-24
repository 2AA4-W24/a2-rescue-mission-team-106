package ca.mcmaster.se2aa4.island.team106.Exploration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team106.DroneTools.FatalErrorHandler;
import ca.mcmaster.se2aa4.island.team106.DroneTools.State;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Status;
import ca.mcmaster.se2aa4.island.team106.Drones.BaseDrone;
import ca.mcmaster.se2aa4.island.team106.States.*;

public class DecisionMaker {
    private final Logger logger = LogManager.getLogger();
    
    private State currentState; 
    
    private State groundFinderState;
    private State centerStartHandlerState;
    private State widthFinderState;
    private State lengthFinderState;
    private State reachCenterState; 
    private State spiralSearchState; 
    
    private BaseDrone drone; 
    private MapArea mapArea;
    private FatalErrorHandler fatalErrorHandler;


    /**
     * Constructs a DecisionMaker with the specified drone, map area, and fatal error handler.
     *
     * @param drone the drone being used to carry out the various actions
     * @param mapArea the map area the drone operates in
     * @param fatalErrorHandler the fatal error handler for managing dangerous situations
     */
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


    /**
     * Makes decisions based on the current state of the drone. It first checks
     * if a danger exists, if it does then it handles the danger; otherwise, it
     * delegates the task to the current state handler.
     *
     * @param parameters the parameter JSON object that stores the additional
     * parameters for the action
     * @param decision the decision JSON object to be modified
     */
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