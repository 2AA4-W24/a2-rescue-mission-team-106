package ca.mcmaster.se2aa4.island.team106;

import org.json.JSONArray;
import org.json.JSONObject;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResultsAcknowledger {

    private final Logger logger = LogManager.getLogger();
    Drone drone;
    MapArea mapArea;
    OutOfRangeHandler outOfRangeHandler;
    IslandReacher islandReacher;
    
    GridSearch gridSearcher;

    public ResultsAcknowledger(Drone drone, MapArea mapArea, OutOfRangeHandler handler, IslandReacher islandReacher) {
        this.drone = drone;
        this.mapArea = mapArea;
        this.outOfRangeHandler = handler;
        this.islandReacher = islandReacher;
        this.gridSearcher = new GridSearch(mapArea);
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

        // extract any creeks if a scan is done
        this.extractCreeks(extraInfo);
        
        if (extraInfo.has("found")) {
            String echoResult = extraInfo.getString("found");

            int echoInt = extraInfo.getInt("range");
            mapArea.setLastDistance(echoInt); // Sets the last distance echoed till either out of range or ground.

            if (echoResult.equals("OUT_OF_RANGE")) {
                this.outOfRangeAction(echoInt);
            }
            
            if (drone.getStatus() == Status.START_STATE) {
                this.startStateHandler(echoResult);
            } else if (drone.getStatus() == Status.GROUND_STATE) {
                this.groundStateHandler(echoResult, echoInt);
            } else if (drone.getStatus() == Status.ISLAND_STATE){
                this.islandStateHandler(echoResult);
            }
        }
    }

    
    public void displayBatteryHeading() {
        logger.info("Drone Battery:" + drone.getBatteryLevel() + " Heading: " + mapArea.getHeading() + " X: " + mapArea.getDroneX() + " Y:" + mapArea.getDroneY());
        mapArea.viewCreeks();
        logger.info("\n");
    }


    private void extractCreeks(JSONObject extraInfo){
        if (extraInfo.has("creeks")){
            JSONArray creeksArray = extraInfo.getJSONArray("creeks");
            if (creeksArray.length() != 0){
                String creekInfo = creeksArray.get(0).toString();
                Point creekPoint = new Point(mapArea.getDroneX(), mapArea.getDroneY());
                mapArea.addCreek(new Creek(creekPoint, creekInfo));
            }
        }
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
        } 
        else {
            drone.setGroundStatus(false);
            drone.setStatus(Status.START_STATE); // ground is no longer found need to go back to start state since we don't have that "concept" of found
        }
    }


    private void islandStateHandler(String echoResult){

        if (echoResult.equals("GROUND"))
        {
            mapArea.setEchoReponseDirectionGround();
            // Direction groundDirection = mapArea.getPrevEchoDirection(); 
            // mapArea.setGroundEchoDirection(groundDirection);

            // if (gridSearcher.isFirstTime()){
            //     logger.info("ARGHGHGHHGHGHGHHGHGHGH");
            //     gridSearcher.setFirstTime(false);

            // }

            if (!gridSearcher.getHasReachedEndOfIsland())
            {
                if (!mapArea.getIsAboveGround()){
                    logger.info("Hey we are not above groumd");
                    Direction groundDirection = mapArea.getPrevEchoDirection(); 
                    logger.info("Ground Direction/New Direction:\t" + groundDirection + " Current Direction:\t" + mapArea.getHeading());
                    mapArea.setNewHeading(groundDirection); // update our new heading as now we want to turn in direction of ground after echoing
                 }
            }
         }
         else{

            // if (!gridSearcher.isFirstTime() && !gridSearcher.isNeedToTurn()){
            //     gridSearcher.setNeedToTurn(true);
            //     logger.info("WAS THIS  OR WHAT! " + gridSearcher.isNeedToTurn());

            // }
            
             logger.info("NO LONGER ABOVE THE ISLAND! SETTING FLAG TO FALSE");
             mapArea.setIsAboveGround(false);
             mapArea.setEchoReponseDirectionOutOfRange();
         }
    }
}
