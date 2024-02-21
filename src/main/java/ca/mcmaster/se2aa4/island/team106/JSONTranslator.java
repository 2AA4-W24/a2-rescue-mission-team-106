package ca.mcmaster.se2aa4.island.team106;

import org.json.JSONObject;
import org.apache.logging.log4j.Logger;

public class JSONTranslator {

    Logger logger;
    Drone drone;
    MapArea mapArea;
    OutOfRangeHandler outOfRangeHandler;
    IslandReacher islandReacher;

    public JSONTranslator(Logger logger, Drone drone, MapArea mapArea, OutOfRangeHandler handler, IslandReacher islandReacher) {
        this.logger = logger;
        this.drone = drone;
        this.mapArea = mapArea;
        this.outOfRangeHandler = handler;
        this.islandReacher = islandReacher;
    }
    
    public void determineCost(JSONObject response) {

        logger.info("** Response received:\n" + response.toString(2));

        Integer cost = response.getInt("cost");
        logger.info("The cost of the action was {}", cost);

        if (drone.canMakeDecision(cost.intValue())) {
            drone.useBattery(cost.intValue());
        }
    }
    
    public void displayStatus(JSONObject response) {
        //get the status same idea as above
        String status = response.getString("status");
        logger.info("The status of the drone is {}", status);
    }

    public void parseRecord(JSONObject response) {
        // get the 'extras' value same idea as above
        JSONObject extraInfo = response.getJSONObject("extras");
        // String foundInfo = extraInfo.getString("found");
        // Integer range = extraInfo.getInt("range");
        logger.info("Additional information received: {}", extraInfo);

        if (extraInfo.has("found")) {
            String echoResult = extraInfo.getString("found");
            int echoInt = extraInfo.getInt("range");
            mapArea.setLastDistance(echoInt); // Sets the last distance echoed till either out of range or ground.

            if (echoResult.equals("OUT_OF_RANGE")) {
                outOfRangeAction(echoInt);
            } else if (drone.getStatus() == Status.START_STATE) {
                startStateHandler(echoResult);
            } else if (drone.getStatus() == Status.GROUND_STATE) {
                groundStateHandler(echoResult, echoInt);
            }
        }
    }
    
    public void displayBatteryHeading() {
        logger.info("Drone Battery:" + drone.getBatteryLevel() + " Heading: " + mapArea.getHeading());
        logger.info("\n");
    }

    private void outOfRangeAction(int echoInt) {
        outOfRangeHandler.setDanger(echoInt, mapArea);
        if (outOfRangeHandler.getDanger()) {
            logger.info("Approaching OUT OF RANGE area");
        }
    }

    private void startStateHandler(String echoResult) {
        logger.info("CURRENT STATE: " + Status.START_STATE);
        Direction groundDirection = mapArea.getPrevEchoDirection(); // store the ground direction we have ground

        if (echoResult.equals("GROUND")) {
            drone.setGroundStatus(true); // ground has been ground so notify drone that status of ground found is true
            logger.info("GROUND HAS BEEN FOUND AT " + groundDirection);

            //! map is updated to date with the heading the drone should be facing to go to ground
            if (groundDirection != mapArea.getHeading()) { 
                mapArea.setNewHeading(groundDirection);
                logger.info("SETTING DRONES DIRECTION TO " + groundDirection);
            }

            mapArea.setGroundEchoDirection(groundDirection); // sets the direction of where we have confirmed there is ground
        }
    }

    private void groundStateHandler(String echoResult, int echoInt) {
        logger.info("CURRENT STATE: " + Status.GROUND_STATE);
        if (echoResult.equals("GROUND")) { // these echo results right here are in front of our drone since we are verifying after our turn that the ground is still in front of us
            drone.setGroundStatus(true);
            logger.info("GROUND HAS BEEN FOUND IN FRONT CONFIRMED!");

            islandReacher.setTiles(echoInt);
            drone.setStatus(Status.GROUND_FOUND_STATE);
        } else {
            drone.setStatus(Status.START_STATE); // ground is no longer found need to go back to start state since we don't have that "concept" of found
        }
    }
}
