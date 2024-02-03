package ca.mcmaster.se2aa4.island.team106;

import java.io.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Explorer implements IExplorerRaid {

    private final Logger logger = LogManager.getLogger();

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
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
    }

    @Override
    public String takeDecision() {
        // create a brand new json object this is not initalized with any data from before hand
        JSONObject decision = new JSONObject();
        // here we are adding data with the key "action" and its assocaited value "stop"
        decision.put("action", "stop"); // we stop the exploration immediately
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

        //get the status same idea as above
        String status = response.getString("status");
        logger.info("The status of the drone is {}", status);

        // get the 'extras' value same idea as above
        JSONObject extraInfo = response.getJSONObject("extras");
        logger.info("Additional information received: {}", extraInfo);
    }

    @Override
    public String deliverFinalReport() {
        return "no creek found";
    }

}
