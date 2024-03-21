package ca.mcmaster.se2aa4.island.team106;


import org.json.JSONObject;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DecisionMaker {
    private final Logger logger = LogManager.getLogger();
    private BaseDrone drone; 

    private DroneFlightManager groundFinder; 
    private DroneFlightManager reachCenter;
    private DroneFlightManager centerStartHandler;

    private DimensionFinder widthFinder;
    private DimensionFinder lengthFinder;

    private Search spiralSearch; 
    private MapArea mapArea;
    private OutOfRangeHandler outOfRangeHandler;


    public DecisionMaker(BaseDrone drone, MapArea mapArea, OutOfRangeHandler outOfRangeHandler){
        this.drone = drone; 
        this.groundFinder = new GroundFinder(mapArea);
        this.centerStartHandler = new CenterStartHandler(mapArea);
        this.widthFinder = new WidthFinder(mapArea);
        this.lengthFinder = new LengthFinder(mapArea);
        this.reachCenter = new ReachCenter(mapArea);
        this.spiralSearch = new SpiralSearch(mapArea);
        this.mapArea = mapArea;
        this.outOfRangeHandler = outOfRangeHandler;
    }


    public void makeDecisions(JSONObject parameters, JSONObject decision) {
        if (this.outOfRangeHandler.getDanger()) {
            this.outOfRangeHandler.handleDanger(drone, mapArea, decision, parameters);
        } else {
            switch (drone.getStatus()) {
                case START_STATE:
                    logger.info("STATE STATUS " + Status.START_STATE);
                    logger.info("DRONE INFORMATION HEADING:  " + mapArea.getHeading());
                    this.groundFinder.fly(this.drone, decision, parameters);
                    break;
                case CENTER_START_STATE:
                    logger.info("STATE STATUS " + Status.CENTER_START_STATE);
                    logger.info("DRONE INFORMATION HEADING:  " + mapArea.getHeading());
                    this.centerStartHandler.fly(this.drone, decision, parameters);
                    break;
                case WIDTH_STATE:
                    logger.info("STATE STATUS " + Status.WIDTH_STATE);
                    this.widthFinder.getDimension(drone, decision, parameters);
                    break;
                case LENGTH_STATE:
                    logger.info("STATE STATUS " + Status.LENGTH_STATE);
                    this.lengthFinder.getDimension(drone, decision, parameters);
                    break;
                case MOVE_CENTER_STATE:
                    logger.info("STATE STATUS " + Status.MOVE_CENTER_STATE);
                    reachCenter.fly(drone, decision, parameters);
                    break;
                case CENTER_STATE:
                    logger.info("STATE STATUS " + Status.CENTER_STATE);
                    this.spiralSearch.setDimensions(mapArea.getWidthOfIsland(), mapArea.getLengthOfIsland());
                    this.spiralSearch.search(drone, decision, parameters);
                    break;
                default:
                    break;
            }
        }
        
    }
}