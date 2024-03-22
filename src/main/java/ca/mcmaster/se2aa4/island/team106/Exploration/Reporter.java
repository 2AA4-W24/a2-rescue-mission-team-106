package ca.mcmaster.se2aa4.island.team106.Exploration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team106.Locations.POI;


public class Reporter {
    private MapArea mapArea;
    private final Logger logger = LogManager.getLogger();

    public Reporter(MapArea mapArea){
        this.mapArea = mapArea;
    }

    public String deliverReport(){
        return getClosestCreek(); 
    }

    
    private String getClosestCreek(){
        Set<POI> creeks = mapArea.getCreeks();
        POI emergSite = mapArea.getEmergencySite();
        
        if (creeks.isEmpty()) {
            return "No creek found";
        } else {
            mapArea.viewCreeks();
            if (mapArea.getEmergencySiteStatus()) {
        
                Map<Double, POI> mappings = new HashMap<>();
        
                for (POI creek: creeks){
                    double distance = creek.getDistance(emergSite);
                    mappings.put(distance, creek);
                }
                
                Double bestDistance = Double.MAX_VALUE;
        
                for (Double distance: mappings.keySet()){
                    if ( distance < bestDistance){
                        bestDistance = distance; 
                    }
                }
                String cloestCreekID = mappings.get(bestDistance).getID();
                logger.info("Closest Creek Found: "  + cloestCreekID + " Distance: " + bestDistance);
                return cloestCreekID;
            } else {
                // In case the emergency site is not found, but some creeks have
                // been located, return any one of the creeks.
                POI[] creekArray = creeks.toArray(new POI[0]);
                return creekArray[0].getID();
            }
        }
    }
}
