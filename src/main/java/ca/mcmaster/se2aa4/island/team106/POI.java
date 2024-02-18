package ca.mcmaster.se2aa4.island.team106;
import java.util.*;

public class POI {
    private HashMap<Integer, Creek> creekMap = new HashMap<>();
    
    public Creek getCreek(int key){
        return creekMap.get(key);
    }

    public void setCreek(Creek creek){
        creekMap.put(creek.getKey(), creek);
    }
}
