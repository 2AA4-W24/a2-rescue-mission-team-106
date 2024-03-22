package ca.mcmaster.se2aa4.island.team106.Exploration;

import org.json.JSONArray;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team106.DroneTools.Direction;
import ca.mcmaster.se2aa4.island.team106.DroneTools.FatalErrorHandler;
import ca.mcmaster.se2aa4.island.team106.DroneTools.Status;
import ca.mcmaster.se2aa4.island.team106.Drones.BaseDrone;
import ca.mcmaster.se2aa4.island.team106.Locations.POI;
import ca.mcmaster.se2aa4.island.team106.Locations.Point;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResultsAcknowledger {

    private final Logger logger = LogManager.getLogger();
    BaseDrone drone;
    MapArea mapArea;
    FatalErrorHandler fatalErrorHandler;
    

    public ResultsAcknowledger(BaseDrone baseDrone, MapArea mapArea, FatalErrorHandler handler) {
        this.drone = baseDrone;
        this.mapArea = mapArea;
        this.fatalErrorHandler = handler;
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
        logger.info("Additional information received: {}", extraInfo);

        // extract any creeks if a scan is done
        this.extractCreeks(extraInfo);
        this.extractEmergencySite(extraInfo);
        
        if (extraInfo.has("found")) {
            String echoResult = extraInfo.getString("found");

            int echoInt = extraInfo.getInt("range");
            mapArea.setLastDistance(echoInt); // Sets the last distance echoed till either out of range or ground.

            if (echoResult.equals("OUT_OF_RANGE")) {
                this.outOfRangeAction(echoInt);
            }
            
            if (drone.getStatus() == Status.GROUND_FINDER_STATE) {
                this.startStateHandler(echoResult);
            } else if (drone.getStatus() == Status.CENTER_START_STATE) {
                this.centerStartStateHandler(echoResult);
            } else if (drone.getStatus() == Status.WIDTH_STATE) {
                this.widthStateHandler(echoResult, echoInt);
            } else if (drone.getStatus() == Status.LENGTH_STATE){
                this.lengthStateHandler(echoResult, echoInt);
            } else if (drone.getStatus() == Status.MOVE_CENTER_STATE){
                this.moveCenterStateHandler(echoResult);
            }else if (drone.getStatus() == Status.CENTER_STATE){
                this.centerStateHandler(echoResult);
            }
        }
    }

    
    public void displayBatteryHeading() {
        logger.info("Drone Battery:" + drone.getBatteryLevel() + " Heading: " + mapArea.getHeading() + " X: " + mapArea.getDroneX() + " Y:" + mapArea.getDroneY());
        logger.info("\n");
    }


    private void extractCreeks(JSONObject extraInfo){
        if (extraInfo.has("creeks")){
            JSONArray creeksArray = extraInfo.getJSONArray("creeks");
            if (creeksArray.length() != 0){

                for (int i = 0; i < creeksArray.length(); i++) {
                    String creekInfo = creeksArray.getString(i);
                    Point creekPoint = new Point(mapArea.getDroneX(), mapArea.getDroneY());
                    mapArea.addCreek(new POI(creekPoint, creekInfo));
                }
            }
        }
    }

    
    private void extractEmergencySite(JSONObject extraInfo){
        if (extraInfo.has("sites")){
            JSONArray emergencySiteArray = extraInfo.getJSONArray("sites");
            if (emergencySiteArray.length() != 0){
                String emergencySiteID = emergencySiteArray.getString(0);
                Point emergencySitePoint = new Point(mapArea.getDroneX(), mapArea.getDroneY()); 
                POI emergencySite = new POI(emergencySitePoint, emergencySiteID);
                mapArea.setEmergencySite(emergencySite);

            }
            
        }
    }


    private void outOfRangeAction(int echoInt) {
        fatalErrorHandler.setDanger(echoInt);
        if (fatalErrorHandler.getDanger()) {
            logger.info("Nearing Fatal Danger");
        }
    }


    private void startStateHandler(String echoResult) {
        logger.info("CURRENT STATE: " + Status.GROUND_FINDER_STATE);
        Direction groundDirection = mapArea.getPrevEchoDirection(); // store the ground direction we have ground

        if (echoResult.equals("GROUND")) {
            mapArea.setGroundStatus(true); // ground has been ground so notify drone that status of ground found is true

            mapArea.setGroundEchoDirection(groundDirection); // sets the direction of where we have confirmed there is ground
        }
    }
    
    private void centerStartStateHandler(String echoResult) {
        logger.info("CURRENT STATE: " + Status.CENTER_START_STATE);
        Direction groundDirection = mapArea.getPrevEchoDirection(); // store the ground direction we have ground

        if (echoResult.equals("GROUND")) {
            mapArea.setGroundStatus(true); // ground has been ground so notify drone that status of ground found is true

            mapArea.setGroundEchoDirection(groundDirection); // sets the direction of where we have confirmed there is ground
        } else {
            if (mapArea.getPrevEchoDirection() == mapArea.getStartDirection()) {
                mapArea.setGroundStatus(false);
            }
        }
    }


    private void widthStateHandler(String echoResult, int echoInt) {
        logger.info("CURRENT STATE: " + Status.WIDTH_STATE);
        if (echoResult.equals("GROUND")) { // these echo results right here are in front of our drone since we are verifying after our turn that the ground is still in front of us
            mapArea.setGroundStatus(true);
            mapArea.setIsAbove(true);

            if (mapArea.getPrevEchoDirection() == Direction.S){
                int currentDistance = mapArea.getSouthDistance();
                mapArea.setSouthDistance(Math.min(currentDistance, echoInt));
            }
            else if (mapArea.getPrevEchoDirection() == Direction.N){
                int currentDistance = mapArea.getNorthDistance();
                mapArea.setNorthDistance(Math.min(currentDistance, echoInt));
            }
        } 
        else {
            mapArea.setIsAbove(false);
        }
    }


    private void lengthStateHandler(String echoResult, int echoInt) {
        logger.info("CURRENT STATE: " + Status.LENGTH_STATE);
        if (echoResult.equals("GROUND")) { // these echo results right here are in front of our drone since we are verifying after our turn that the ground is still in front of us
            mapArea.setGroundStatus(true);
            mapArea.setIsAbove(true);
            if (mapArea.getPrevEchoDirection() == Direction.E) {
                int currentDistance = mapArea.getEastDistance();
                mapArea.setEastDistance(Math.min(currentDistance, echoInt));
            } else if (mapArea.getPrevEchoDirection() == Direction.W) {
                int currentDistance = mapArea.getWestDistance();
                mapArea.setWestDistance(Math.min(currentDistance, echoInt));
            }
        } else {
            mapArea.setIsAbove(false);
        }
    }
    
    private void moveCenterStateHandler(String echoResult) {
        logger.info("CURRENT STATE: " + Status.MOVE_CENTER_STATE);
        if (echoResult.equals("GROUND")) { // these echo results right here are in front of our drone since we are verifying after our turn that the ground is still in front of us
            mapArea.setGroundStatus(true);
        }
    }

    private void centerStateHandler(String echoResult) {
        logger.info("CURRENT STATE: " + Status.CENTER_STATE);
        if (echoResult.equals("GROUND")) { // these echo results right here are in front of our drone since we are verifying after our turn that the ground is still in front of us
            mapArea.setGroundStatus(true);
        }
    }
}
