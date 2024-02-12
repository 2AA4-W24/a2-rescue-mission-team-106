package ca.mcmaster.se2aa4.island.team106;

import java.io.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Explorer implements IExplorerRaid {

    private final Logger logger = LogManager.getLogger();
    private int counts = 1; 
    private Drone drone; 
    private Direction heading;
    private boolean danger = false;

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center"); 

        // Creating a new JSON object called "info"
        // Specifically, we are initializing it with the contents of an existing JSON file,
        // which is represented by a JSON TOKEN (let's call it X).
        // We read the contents of the JSON file (X) using a JSONTokener,
        // which is constructed with a StringReader initialized with the string 's'.
        // The 'info' object is now initialized with all the data from the JSON file (X) passed into the constructor.
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));  
        logger.info("** Initialization info:\n {}",info.toString(2));


        // Extracting the value associated with the "heading" key from the 'info' JSON object.
        // In this case, the value represents the direction, which is assigned to the variable 'direction'.
        String direction = info.getString("heading");
        
        
        // Extracting the battery level from the 'info' JSON object.
        // The battery level is obtained by retrieving the value associated with the "budget" key in the JSON file.
        // The result is stored in the 'batteryLevel' variable.
        Integer batteryLevel = info.getInt("budget");

        heading = Direction.fromString(direction); 
        drone = new Drone(batteryLevel.intValue(), heading);

        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
        logger.info("****************** ENDING OF INITIALIZATION*********************************** \n\n");
    }

    @Override
    public String takeDecision() {
        logger.info("Times called: " + this.counts);

        // create a brand new json object this is not initialized with any data from before hand
        JSONObject decision = new JSONObject(); 
        JSONObject parameters = new JSONObject();

        // here we are adding data with the key "action" and its associated value "stop"

        if (!drone.getGroundStatus() && !danger)
        {
            if (this.counts % 5  == 0){
                decision.put("action", "fly");
            }
            else if (this.counts % 5 == 1){
                logger.info("ECHOING EAST");
                drone.echoEast(parameters, decision);
            }
            else if (this.counts % 5 == 2){
                logger.info("ECHOING SOUTH");
                drone.echoSouth(parameters, decision);
            }
            else if(this.counts % 5 == 3){
                logger.info("ECHOING NORTH");
                drone.echoNorth(parameters, decision);
            }
            else if(this.counts % 5 == 4){
                logger.info("ECHOING WEST");
                drone.echoWest(parameters, decision);
            }
            
        }
        else{ // now we want to fly towards the ground, so a this point we have updated our current heading so it MUST me different than our previous heading

            // drone.updateHeading(parameters, decision, drone.getHeading());
            // logger.info("Drone Battery:" + drone.getBatteryLevel() + " Heading: " + drone.getHeading());
            // drone.echoForwards(parameters, decision);
            drone.stop(decision); // we stop the exploration immediately
        }

        this.counts++;
        
        
        //reading the json file as a string 
        logger.info("** Decision: {}",decision.toString());
        return decision.toString();
    }

    @Override
    public void acknowledgeResults(String s) {

        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));

        // print the json object
        logger.info("** Response received:\n"+response.toString(2));

        // get the cost value from JSON we are accessing its KEY by using getInt
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

        if (extraInfo.has("found")) {
            String echoResult = extraInfo.getString("found");

            if (echoResult.equals("GROUND")) { // we want to move south 
                drone.setGroundStatus(true);
                Direction groundDirection = drone.getPrevEchoDirection();
                logger.info("GROUND HAS BEEN FOUND!");
                logger.info("SETTING DRONES DIRECTION TO " + groundDirection);

                if (groundDirection != drone.getHeading()){ //! needs to be removed/fixed for later
                    drone.setHeading(groundDirection);
                }
            }
            if (echoResult.equals("OUT_OF_RANGE")) {
                if (extraInfo.has("range")) {
                    int echoInt = extraInfo.getInt("range");
                    if (echoInt <= 2 && drone.getPrevEchoDirection() == drone.getHeading()) {
                        danger = true;
                        logger.info("Approaching OUT OF RANGE area");
                    }
                }
            }

        }
    
        logger.info("Drone Battery:" + drone.getBatteryLevel() + " Heading: " + drone.getHeading());
        logger.info("\n");

    }

    @Override
    public String deliverFinalReport() {
        return "no creek found";
    }

}
