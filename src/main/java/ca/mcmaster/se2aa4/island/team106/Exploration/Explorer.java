package ca.mcmaster.se2aa4.island.team106.Exploration;

import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.team106.DroneTools.Direction;
import ca.mcmaster.se2aa4.island.team106.DroneTools.FatalErrorHandler;
import ca.mcmaster.se2aa4.island.team106.Drones.BaseDrone;
import ca.mcmaster.se2aa4.island.team106.Drones.Drone;

public class Explorer implements IExplorerRaid {

    private final Logger logger = LogManager.getLogger();
    private MapArea mapArea = new MapArea();
    private final int MINIMUM_BATTERY_TO_OPERATE = 20;

    private BaseDrone drone = new Drone(MINIMUM_BATTERY_TO_OPERATE, mapArea);
    private FatalErrorHandler fatalErrorHandler = new FatalErrorHandler(MINIMUM_BATTERY_TO_OPERATE, drone, mapArea);
    private DecisionMaker decisionMaker = new DecisionMaker(drone, mapArea, fatalErrorHandler);
    private ResultsAcknowledger acknowledger = new ResultsAcknowledger(drone, mapArea, fatalErrorHandler);
    private Reporter reporter = new Reporter(mapArea);


    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center"); 
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));  
        logger.info("** Initialization info:\n {}",info.toString(2));

        String direction = info.getString("heading");
        Integer batteryLevel = info.getInt("budget");

        Direction heading = Direction.fromString(direction); 
        
        // update drone to starting battery and heading facing at start
        drone.updateDrone(batteryLevel.intValue(), heading);
        mapArea.setStartDirection(heading);

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

        acknowledger.determineCost(response);
        acknowledger.displayStatus(response);
        acknowledger.parseRecord(response);
        acknowledger.displayBatteryHeading();
    }

    @Override
    public String deliverFinalReport() {
        String report = reporter.deliverReport();
        logger.info(report);
        return report; 
    }
}
