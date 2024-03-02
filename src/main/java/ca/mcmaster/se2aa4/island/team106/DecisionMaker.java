package ca.mcmaster.se2aa4.island.team106;


import org.json.JSONObject;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DecisionMaker {
    private final Logger logger = LogManager.getLogger();
    private Drone drone; 
    private GroundFinder groundFinder; 
    // private GridSearch gridSearch;
    private WidthFinder widthFinder;
    private LengthFinder lengthFinder;
    private ReachCenter reachCenter;
    private IslandReacher islandReacher; 
    private MapArea mapArea;
    private OutOfRangeHandler outOfRangeHandler;


    public DecisionMaker(Drone drone, IslandReacher islandReacher, MapArea mapArea, OutOfRangeHandler outOfRangeHandler){
        this.drone = drone; 
        this.groundFinder = new GroundFinder(mapArea); 
        // this.gridSearch = new GridSearch(mapArea);
        this.widthFinder = new WidthFinder(mapArea);
        this.lengthFinder = new LengthFinder(mapArea);
        this.reachCenter = new ReachCenter(mapArea);
        this.islandReacher = islandReacher; 
        this.mapArea = mapArea;
        this.outOfRangeHandler = outOfRangeHandler;
    }


    public void makeDecisions(JSONObject parameters, JSONObject decision) {
        if (this.outOfRangeHandler.getDanger()) {
            Direction nextDirection = this.outOfRangeHandler.changeDirection(this.mapArea);
            logger.info("CHANGING DIRECTION TO " + nextDirection);
            this.drone.updateHeading(parameters, decision, nextDirection);
            this.outOfRangeHandler.setDanger(false); 
        }
        else if (drone.getStatus() == Status.START_STATE)
        {
            logger.info("STATE STATUS " + Status.START_STATE);
            logger.info("DRONE INFORMATION HEADING:  " + mapArea.getHeading());
            this.groundFinder.fly(this.drone, decision, parameters);
        }
        else if (drone.getStatus() == Status.WIDTH_STATE) // now we want to fly towards the ground, so a this point we have updated our current heading so it MUST me different than our previous heading
        { 
            logger.info("STATE STATUS " + Status.WIDTH_STATE);
            this.widthFinder.getWidthOfIsland(drone, decision, parameters);
        }
        else if(drone.getStatus() == Status.LENGTH_STATE)
        {
            logger.info("STATE STATUS " + Status.LENGTH_STATE);
            this.lengthFinder.getLengthOfIsland(drone, decision, parameters);
        } else if (drone.getStatus() == Status.MOVE_CENTER_STATE) {
            logger.info("STATE STATUS " + Status.MOVE_CENTER_STATE);
            reachCenter.fly(drone, decision, parameters);
        } else if (drone.getStatus() == Status.CENTER_STATE) {
            logger.info("STATE STATUS " + Status.CENTER_STATE);
            drone.stop(decision);
        }
        // else if (this.drone.getStatus() == Status.GROUND_FOUND_STATE)
        // {
        //     logger.info("STATE STATUS " + Status.GROUND_FOUND_STATE);
        //     logger.info("DRONE INFORMATION HEADING:  " + mapArea.getHeading());
        //     this.islandReacher.fly(this.drone, decision); // takes care of flying to the island
        // }
        // else if (drone.getStatus() == Status.ISLAND_STATE){
        //     logger.info("STATE STATUS " + Status.ISLAND_STATE);

        //     logger.info("DRONE INFORMATION HEADING:  " + mapArea.getHeading());
        //     // lengthFinder.getLength(drone, decision, parameters);
        //     // drone.stop(decision);
        //     gridSearch.gridSearchIsland(drone, decision, parameters);
        // }
    }
}