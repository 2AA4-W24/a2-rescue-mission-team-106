package ca.mcmaster.se2aa4.island.team106;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Reporter {
    private MapArea mapArea;
    private final Logger logger = LogManager.getLogger();

    public Reporter(MapArea mapArea){
        this.mapArea = mapArea;
    }

    //! deliver the fucking report
    public String deliverReport(){
        logger.info("Deliver called");
        return getClosestCreek(); 
    }

    
    private String getClosestCreek(){
        logger.info("WAS THIS METHOD CALLED");
        Set<Creek> creeks = mapArea.getCreeks();
        Creek emergSite = mapArea.getEmergencySite(); 

        logger.info("Got creeks and stuff: " + emergSite.toString());

        
        HashMap<Double, Creek> mappings = new HashMap<>();

        for (Creek creek: creeks){
            double distance = creek.getDistance(emergSite);
            mappings.put(distance, creek);
        }

        logger.info("Computed distances");

        Double bestDistance = Double.MAX_VALUE;

        for (Double distance: mappings.keySet()){
            if ( distance < bestDistance){
                logger.info("HEY A NEW BEST DISTANCE WAS FOUND: " + distance);
                bestDistance = distance; 
            }
        }
        

        logger.info("HEY I FOUND THE CLOEST CREEK? " + bestDistance);
        logger.info("BIG BOY TEST: " + mappings.get(bestDistance).getCreekID());
        return mappings.get(bestDistance).getCreekID();

    }
    
}