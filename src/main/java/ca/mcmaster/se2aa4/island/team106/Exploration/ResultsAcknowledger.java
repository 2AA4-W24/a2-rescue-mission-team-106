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

    /**
     * Constructs a ResultsAcknowledger object with the given parameters.
     *
     * @param baseDrone the drone being used to carry out the various actions
     * @param mapArea the map area in which details about the findings of the
     * map area are stored
     * @param handler the FatalErrorHandler object that detects any dangerous
     * situations.
     */
    public ResultsAcknowledger(BaseDrone baseDrone, MapArea mapArea, FatalErrorHandler handler) {
        this.drone = baseDrone;
        this.mapArea = mapArea;
        this.fatalErrorHandler = handler;
    }

    /**
     * Determines the cost of the action and updates drone's battery level. It
     * also checks if the battery is critical and takes appropriate action if
     * that is the case.
     * 
     * @param response the response JSON Object received
     */
    public void determineCost(JSONObject response) {

        logger.info("** Response received:\n" + response.toString(2));

        Integer cost = response.getInt("cost");
        logger.info("the cost of the action was {}", cost);

        if (drone.canMakeDecision(cost.intValue())) {
            drone.useBattery(cost.intValue());
            this.fatalErrorHandler.setBatteryDanger(false);
        } else {
            if (!("stop".equals(mapArea.getCurrentAction()))) {
                this.fatalErrorHandler.setBatteryDanger(true);
                drone.useBattery(cost.intValue());
            }
        }
    }

    /**
     * Displays the status of the drone.
     * 
     * @param response the response JSON object received
     */
    public void displayStatus(JSONObject response) {
        //get the status same idea as above
        String status = response.getString("status");
        logger.info("the status of the drone is {}", status);
    }

    /**
     * Parses the additional information received in the JSON response.
     * 
     * @param response the response JSON object received
     */
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
            mapArea.setLastDistance(echoInt);

            if ("OUT_OF_RANGE".equals(echoResult)) {
                this.outOfRangeAction(echoInt);
            }

            if (drone.getStatus() == Status.GROUND_FINDER_STATE) {
                this.groundFinderStateHandler(echoResult);
            } else if (drone.getStatus() == Status.CENTER_START_STATE) {
                this.centerStartStateHandler(echoResult);
            } else if (drone.getStatus() == Status.WIDTH_STATE) {
                this.widthStateHandler(echoResult, echoInt);
            } else if (drone.getStatus() == Status.LENGTH_STATE) {
                this.lengthStateHandler(echoResult, echoInt);
            } else if (drone.getStatus() == Status.MOVE_CENTER_STATE) {
                this.moveCenterStateHandler(echoResult);
            } else if (drone.getStatus() == Status.CENTER_STATE) {
                this.centerStateHandler(echoResult);
            }
        }
    }

    /**
     * Displays the battery level and current heading of the drone.
     */
    public void displayBatteryHeading() {
        logger.info("Drone Battery:" + drone.getBatteryLevel() + " Heading: " + mapArea.getHeading() + " X: "
                + mapArea.getDroneX() + " Y:" + mapArea.getDroneY());
        logger.info("\n");
    }

    /**
    * Extracts creek information from the response JSON object and updates the
    * map area accordingly.
    *
    * @param extraInfo the JSON object containing extra information.
    */
    private void extractCreeks(JSONObject extraInfo) {
        if (extraInfo.has("creeks")) {
            JSONArray creeksArray = extraInfo.getJSONArray("creeks");
            if (creeksArray.length() != 0) {
                for (int i = 0; i < creeksArray.length(); i++) {
                    String creekInfo = creeksArray.getString(i);
                    Point creekPoint = new Point(mapArea.getDroneX(), mapArea.getDroneY());
                    mapArea.addCreek(new POI(creekPoint, creekInfo));
                }
            }
        }
    }

    /**
     * Extracts emergency site information from the response JSON object and updates the map area accordingly.
     * 
     * @param extraInfo the JSON object containing extra information.
     */
    private void extractEmergencySite(JSONObject extraInfo) {
        if (extraInfo.has("sites")) {
            JSONArray emergencySiteArray = extraInfo.getJSONArray("sites");
            if (emergencySiteArray.length() != 0) {
                String emergencySiteID = emergencySiteArray.getString(0);
                Point emergencySitePoint = new Point(mapArea.getDroneX(), mapArea.getDroneY());
                POI emergencySite = new POI(emergencySitePoint, emergencySiteID);
                mapArea.setEmergencySite(emergencySite);
            }
        }
    }

    /**
     * Handles actions when the echo result indicates that the object is nearing
     * OUT OF RANGE.
     * 
     * @param echoInt the range obtained by echoing in a direction
     */
    private void outOfRangeAction(int echoInt) {
        fatalErrorHandler.setRangeDanger(echoInt);
        if (fatalErrorHandler.getDanger()) {
            logger.info("Nearing Fatal Danger");
        }
    }

    /**
     * Handles actions when the drone is in the start state.
     * 
     * @param echoResult the results of the echo action.
     */
    private void groundFinderStateHandler(String echoResult) {
        logger.info("CURRENT STATE: " + Status.GROUND_FINDER_STATE);
        Direction groundDirection = mapArea.getPrevEchoDirection();
        if ("GROUND".equals(echoResult)) {
            mapArea.setGroundStatus(true);
            mapArea.setGroundEchoDirection(groundDirection);
        }
    }

    /**
     * Handles actions when the drone is in the center start state.
     * 
     * @param echoResult the results of the echo action.
     */
    private void centerStartStateHandler(String echoResult) {
        logger.info("CURRENT STATE: " + Status.CENTER_START_STATE);
        Direction groundDirection = mapArea.getPrevEchoDirection();
        if ("GROUND".equals(echoResult)) {
            mapArea.setGroundStatus(true);
            mapArea.setGroundEchoDirection(groundDirection);
        } else {
            if (mapArea.getPrevEchoDirection() == mapArea.getStartDirection()) {
                mapArea.setGroundStatus(false);
            }
        }
    }

    /**
     * Handles actions when the drone is in the width state.
     * 
     * @param echoResult the results of the echo action.
     * @param echoInt the range obtained by echoing in a direction
     */
    private void widthStateHandler(String echoResult, int echoInt) {
        logger.info("CURRENT STATE: " + Status.WIDTH_STATE);
        if ("GROUND".equals(echoResult)) {
            mapArea.setGroundStatus(true);
            mapArea.setIsAbove(true);
            if (mapArea.getPrevEchoDirection() == Direction.S) {
                int currentDistance = mapArea.getSouthDistance();
                mapArea.setSouthDistance(Math.min(currentDistance, echoInt));
            } else if (mapArea.getPrevEchoDirection() == Direction.N) {
                int currentDistance = mapArea.getNorthDistance();
                mapArea.setNorthDistance(Math.min(currentDistance, echoInt));
            }
        } else {
            mapArea.setIsAbove(false);
        }
    }

    /**
     * Handles actions when the drone is in the length state.
     * 
     * @param echoResult the results of the echo action.
     * @param echoInt the range obtained by echoing in a direction
     */
    private void lengthStateHandler(String echoResult, int echoInt) {
        logger.info("CURRENT STATE: " + Status.LENGTH_STATE);
        if ("GROUND".equals(echoResult)) {
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

    /**
     * Handles actions when the drone is in the move center state.
     * 
     * @param echoResult the results of the echo action.
     */
    private void moveCenterStateHandler(String echoResult) {
        logger.info("CURRENT STATE: " + Status.MOVE_CENTER_STATE);
        if ("GROUND".equals(echoResult)) {
            mapArea.setGroundStatus(true);
        }
    }

    /**
     * Handles actions when the drone is in the center state.
     * 
     * @param echoResult the results of the echo action.
     */
    private void centerStateHandler(String echoResult) {
        logger.info("CURRENT STATE: " + Status.CENTER_STATE);
        if ("GROUND".equals(echoResult)) {
            mapArea.setGroundStatus(true);
        }
    }
}
