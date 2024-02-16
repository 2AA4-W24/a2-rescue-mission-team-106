package ca.mcmaster.se2aa4.island.team106;

import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Explorer implements IExplorerRaid {

    private final Logger logger = LogManager.getLogger();
    private IslandReacher islandReacher = new IslandReacher(0); 
    private Direction heading;
    private MapArea mapArea = new MapArea(); 
    private GroundFinder groundFinder = new GroundFinder(); 
    private Drone drone = new Drone(0, Direction.N, mapArea);
    private DecisionMaker decisionMaker = new DecisionMaker(drone, groundFinder, islandReacher, mapArea);

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center"); 
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));  
        logger.info("** Initialization info:\n {}",info.toString(2));

        String direction = info.getString("heading");
        Integer batteryLevel = info.getInt("budget");

        heading = Direction.fromString(direction); 
        drone.updateDrone(batteryLevel.intValue(), heading);

        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
        logger.info("****************** ENDING OF INITIALIZATION*********************************** \n\n");
    }


    @Override
    public String takeDecision() {
        // create a brand new json object this is not initialized with any data from before hand
        JSONObject decision = new JSONObject(); 
        JSONObject parameters = new JSONObject();

        decisionMaker.makeDecisions(parameters, decision);

        //reading the json file as a string 
        logger.info("** Decision: {}",decision.toString());
        return decision.toString();
    }


    @Override
    public void acknowledgeResults(String s) {

        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));

        logger.info("** Response received:\n"+response.toString(2));

        Integer cost = response.getInt("cost");
        logger.info("The cost of the action was {}", cost);

        if (drone.canMakeDecision(cost.intValue())){
            drone.useBattery(cost.intValue());
        }

        //get the status same idea as above
        String status = response.getString("status");
        logger.info("The status of the drone is {}", status);

        // get the 'extras' value same idea as above
        JSONObject extraInfo = response.getJSONObject("extras");
        // String foundInfo = extraInfo.getString("found");
        // Integer range = extraInfo.getInt("range");
        logger.info("Additional information received: {}", extraInfo);

        if (extraInfo.has("found"))
        {
            String echoResult = extraInfo.getString("found");
            
            if (drone.getStatus() == Status.START_STATE)
            {
                logger.info("CURRENT STATE: " + Status.START_STATE);
                if (echoResult.equals("GROUND")) { 
                    drone.setGroundStatus(true); 
                    Direction groundDirection = mapArea.getPrevEchoDirection();  
                    logger.info("GROUND HAS BEEN FOUND!");
                    logger.info("SETTING DRONES DIRECTION TO " + groundDirection);

                    if (groundDirection != mapArea.getHeading()){ 
                        mapArea.setHeading(groundDirection); 
                    }
    
                    mapArea.setGroundEchoDirection(groundDirection); // sets the direction of where we have confirmed there is ground 
                    drone.setStatus(Status.GROUND_STATE); // transiiton into a new state of our algorithm ground_state

                }
            }
            else if (drone.getStatus() == Status.GROUND_STATE)
            {
                logger.info("CURRENT STATE: " + Status.GROUND_STATE);
                if (echoResult.equals("GROUND")) { // these echo results right here are infront of our drone since we are verifying after our turn that the ground is still in front of us
                    drone.setGroundStatus(true);
                    logger.info("GROUND HAS BEEN FOUND INFRONT CONFIRMED!");
                    int tiles = extraInfo.getInt("range");

                    islandReacher.setTiles(tiles);
                    drone.setStatus(Status.GROUND_FOUND_STATE);
                }
                else{
                    drone.setStatus(Status.START_STATE); // ground is no longer found need to go back to start state since we dont have that "concept" of found
                }
            }
            if (echoResult.equals("OUT_OF_RANGE")) {
                if (extraInfo.has("range")) {
                    int echoInt = extraInfo.getInt("range");
                    if (echoInt <= 2 && mapArea.getPrevEchoDirection() == mapArea.getHeading()) {
                        groundFinder.setDanger(true); 
                        logger.info("Approaching OUT OF RANGE area");
                    }
                }
            }
        }

        logger.info("Drone Battery:" + drone.getBatteryLevel() + " Heading: " + mapArea.getHeading());
        logger.info("\n");
    }

    @Override
    public String deliverFinalReport() {
        return "no creek found";
    }
}
