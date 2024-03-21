// Search.java
package ca.mcmaster.se2aa4.island.team106;

import org.json.JSONObject;

public interface Search {
    void search(BaseDrone drone, JSONObject decision, JSONObject parameters);
    
    void setDimensions(int width, int height);
}

