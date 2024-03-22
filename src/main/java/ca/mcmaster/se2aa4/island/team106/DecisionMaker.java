package ca.mcmaster.se2aa4.island.team106;


import org.json.JSONObject;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DecisionMaker {
    private final Logger logger = LogManager.getLogger();
    private BaseDrone drone; 

    private State currentState; 

    private State groundFinder;
    private State centerStartHandler;
    private State widthFinder;
    private State lengthFinder;
    private State reachCenter; 
    private State spiralSearch; 
    
    private MapArea mapArea;
    private FatalErrorHandler fatalErrorHandler;

    //! next test is to set it to groundFinder
    public DecisionMaker(BaseDrone drone, MapArea mapArea, FatalErrorHandler fatalErrorHandler){
        this.drone = drone; 
        this.groundFinder = new GroundFinderState(mapArea);
        this.centerStartHandler = new CenterStartHandler(mapArea);
        this.widthFinder = new WidthFinder(mapArea);
        this.lengthFinder = new LengthFinder(mapArea);
        this.reachCenter = new ReachCenter(mapArea);
        this.spiralSearch = new SpiralSearch(mapArea);
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
                    this.currentState = this.groundFinder; 
                  
                    break;
                case CENTER_START_STATE:
                    logger.info("STATE STATUS " + Status.CENTER_START_STATE);
                    logger.info("DRONE INFORMATION HEADING:  " + mapArea.getHeading());
                    this.currentState = this.centerStartHandler; 
                    break;
                case WIDTH_STATE:
                    logger.info("STATE STATUS " + Status.WIDTH_STATE);
                    this.currentState = this.widthFinder; 
                    break;
                case LENGTH_STATE:
                    logger.info("STATE STATUS " + Status.LENGTH_STATE);
                    this.currentState = this.lengthFinder; 
                    break;
                case MOVE_CENTER_STATE:
                    logger.info("STATE STATUS " + Status.MOVE_CENTER_STATE);
                    this.currentState = this.reachCenter; 
                    break;
                case CENTER_STATE:
                    logger.info("STATE STATUS " + Status.CENTER_STATE);
                    this.currentState = this.spiralSearch;  
                    break;
                default:
                    break;
            }
            this.currentState.handle(drone, decision, parameters);
        }
        
    }
}