package ca.mcmaster.se2aa4.island.team106;

import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Explorer implements IExplorerRaid {

    private final Logger logger = LogManager.getLogger();
    private MapArea mapArea = new MapArea();
    private IslandReacher islandReacher = new IslandReacher(0, mapArea); 
    private Direction heading;
    private Drone drone = new Drone(0, Direction.N, mapArea);
    private OutOfRangeHandler outOfRangeHandler = new OutOfRangeHandler();
    private DecisionMaker decisionMaker = new DecisionMaker(drone, islandReacher, mapArea, outOfRangeHandler);

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
            int echoInt = extraInfo.getInt("range");
            mapArea.setLastDistance(echoInt);   // Sets the last distance echoed till either out of range or ground.

            
            if (echoResult.equals("OUT_OF_RANGE")) {
                // logger.info("TRACER BULLET ACTIAVTE!");
                outOfRangeHandler.setDanger(echoInt, mapArea);
                if (outOfRangeHandler.getDanger()) {
                    logger.info("Approaching OUT OF RANGE area");
                }
            }
            
            if (drone.getStatus() == Status.START_STATE)
            {
                logger.info("CURRENT STATE: " + Status.START_STATE);
                Direction groundDirection = mapArea.getPrevEchoDirection();  // store the ground direction we have ground

                if (echoResult.equals("GROUND")) { 
                    drone.setGroundStatus(true);  // ground has been ground so notify drone that status of ground found is true
                    logger.info("GROUND HAS BEEN FOUND AT " + groundDirection);


                    if (groundDirection != mapArea.getHeading()){ //! map is updated to date with the heading the drone should be facing to go to ground
                        mapArea.setNewHeading(groundDirection);
                        logger.info("SETTING DRONES DIRECTION TO " + groundDirection);
                    }

                    mapArea.setGroundEchoDirection(groundDirection); // sets the direction of where we have confirmed there is ground
                }
              
            }
            else if (drone.getStatus() == Status.GROUND_STATE)
            {
                logger.info("CURRENT STATE: " + Status.GROUND_STATE);
                if (echoResult.equals("GROUND")) { // these echo results right here are in front of our drone since we are verifying after our turn that the ground is still in front of us
                    
                    logger.info("GROUND HAS BEEN FOUND IN FRONT CONFIRMED!");
                    
                    islandReacher.setTiles(echoInt);
                    drone.setStatus(Status.GROUND_FOUND_STATE);
                }
                else{
                    drone.setStatus(Status.START_STATE); // ground is no longer found need to go back to start state since we don't have that "concept" of found
                    drone.setGroundStatus(false);
                }
            }
            else if (drone.getStatus() == Status.ISLAND_STATE){
                logger.info("@ ISLAND STATE WITH ECHO RESULT OF : " + echoResult);
            
                if (echoResult.equals("GROUND")){
                   logger.info("WE ARE STILL ABOVE GROUND ON THIS ECHO SO WE WILL MARK BY SCANNING ON NEXT MOVE,  THEN AFTER THAT MOVE WE WILL FLY");

                    if (!mapArea.getIsAboveGround()){
                        Direction groundDirection = mapArea.getPrevEchoDirection(); 
                        mapArea.setHeading(groundDirection);
                    }

                }
                else{
                    logger.info("NO LONGER ABOVE THE ISLAND! SETTING FLAG TO FALSE");
                    mapArea.setIsAboveGround(false);
                }
            }
        }



        logger.info("Drone Battery:" + drone.getBatteryLevel() + " Heading: " + mapArea.getHeading() + " X: " + mapArea.getDroneX() + " Y: " + mapArea.getDroneY());
        logger.info("\n");
    }

    @Override
    public String deliverFinalReport() {
        return "no creek found";
    }
}
