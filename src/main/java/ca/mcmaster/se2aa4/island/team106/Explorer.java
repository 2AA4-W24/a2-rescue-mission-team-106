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

        Direction heading = Direction.fromString(direction); 
        drone = new Drone(batteryLevel.intValue(), heading); 


        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
        logger.info("****************** ENDING OF INITALIZATION*********************************** \n\n");
    }

    @Override
    public String takeDecision() {
        logger.info("Times called: " + this.counts);

        // create a brand new json object this is not initalized with any data from before hand
        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();

        // here we are adding data with the key "action" and its assocaited value "stop"

        switch (this.counts){
            case 1: {
                logger.info("CASE 1 FIRST HIT");
                decision.put("action", "fly");
                break; 
            }
            case 2: {
                logger.info("ECHOING EAST");
                parameters.put("direction", "E");
                decision.put("action", "echo");
                decision.put("parameters", parameters);
                break; 
            }

            case 3: {
                logger.info("ECHOING SOUTH");
                parameters.put("direction", "S");
                decision.put("action", "echo");
                decision.put("parameters", parameters);
                break; 
            }

            case 4: {
                logger.info("ECHOING NORTH");
                parameters.put("direction", "N");
                decision.put("action", "echo");
                decision.put("parameters", parameters);
                break; 
            }
            case 5: {

            }

        }

        this.counts++;
        
        //decision.put("action", "stop"); // we stop the exploration immediately
        //reading the json file as a string 
        logger.info("** Decision: {}",decision.toString());
        return decision.toString();
    }

    @Override
    public void acknowledgeResults(String s) {
        // Creating a new JSON object called "response"
        // Specifically, we are initializing it with the contents of an existing JSON file,
        // which is represented by a JSON TOKEN (let's call it X).
        // We read the contents of the JSON file (X) using a JSONTokener,
        // which is constructed with a StringReader initialized with the string 's'.
        // The 'info' object is now initialized with all the data from the JSON file (X) passed into the constructor.
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
        logger.info("Additional information received: {}", extraInfo);

        logger.info("Drone Battery:" + drone.getBatteryLevel() + " Heading: " + drone.getHeading());
        logger.info("\n");

    }

    @Override
    public String deliverFinalReport() {
        return "no creek found";
    }

}
