package ca.mcmaster.se2aa4.island.team106;


import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DecisionMaker {
    private final Logger logger = LogManager.getLogger();
    private Drone drone; 
    private GroundFinder groundFinder; 
    private IslandReacher islandReacher; 
    private MapArea mapArea;
    private OutOfRange outOfRange;

    public DecisionMaker(Drone drone, GroundFinder groundFinder, IslandReacher islandReacher, MapArea mapArea, OutOfRange outOfRange){
        this.drone = drone; 
        this.groundFinder = groundFinder; 
        this.islandReacher = islandReacher; 
        this.mapArea = mapArea;
        this.outOfRange = outOfRange;
    }


    public void makeDecisions(JSONObject parameters, JSONObject decision) {
        if (outOfRange.getDanger()) {
            Direction nextDirection = this.outOfRange.changeDirection();
            // this.mapArea.setHeading(nextDirection);
            // logger.info(nextDirection);
            this.drone.updateHeading(parameters, decision, nextDirection);
        }
        else if (drone.getStatus() == Status.START_STATE)
        {
            this.groundFinder.fly(this.drone, decision, parameters, mapArea);
        }
        else if (drone.getStatus() == Status.GROUND_STATE) // now we want to fly towards the ground, so a this point we have updated our current heading so it MUST me different than our previous heading
        { 
            if (this.mapArea.getHeading() == this.mapArea.getGroundEchoDirection()){
                this.drone.echoForwards(parameters, decision); // this is called to verify that after turning the ground is physically in front of the drone and not in the "general" direction
            }
            else{
                Direction echoGroundDirection = this.mapArea.getGroundEchoDirection(); 
                this.mapArea.setHeading(echoGroundDirection); // turn in the direction of where ground is verified
            }
        }
        else if (this.drone.getStatus() == Status.GROUND_FOUND_STATE)
        {
            logger.info("STATE STATUS " + Status.GROUND_FOUND_STATE);
            this.islandReacher.fly(this.drone, decision); // takes care of flying to the island
        }
        else if (drone.getStatus() == Status.ISLAND_STATE){
            logger.info("STATE STATUS " + Status.ISLAND_STATE);
            this.drone.stop(decision); // we stop the exploration immediately
        }
    }
}
