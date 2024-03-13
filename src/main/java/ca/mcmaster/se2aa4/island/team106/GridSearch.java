// package ca.mcmaster.se2aa4.island.team106;



// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;
// import org.json.JSONObject;


// public class GridSearch {
//     private final Logger logger = LogManager.getLogger();

//     private IslandEndReacher islandEndReacher; //! problem
//     private WidthFinder widthFinder; 
//     private LengthFinder lengthFinder; 
//     private MapArea mapArea; 

//     private int lengthDistance; 
//     private int widthDistance; 

//     private boolean hasReachedEndOfIsland = false;
//     private boolean findingWidth = false; 
//     private boolean findingLength = false; 
//     private boolean spiral = false; 


//     public GridSearch(MapArea mapArea){
//         this.islandEndReacher = new IslandEndReacher(mapArea);
//         this.widthFinder = new WidthFinder(mapArea);
//         this.lengthFinder = new LengthFinder(mapArea);
//         this.mapArea = mapArea; 
//     }

//     public void gridSearchIsland(Drone drone, JSONObject decision, JSONObject parameters){

//         if (!this.hasReachedEndOfIsland){
//             logger.info("USING ISLAND END REACHER");
//            this.goToEndOfIsland(drone, decision, parameters);
//         }
//         else if (this.findingWidth){
//             logger.info("USING WIDTH FINDER");
//             this.getWidth(drone, decision, parameters);
//             logger.info("FACING: " + mapArea.getHeading() + " PREVIOUS DIRECTION: " + mapArea.getPrevHeading());
//         }
//         else if (this.findingLength){
//             logger.info("USING LENGTH FINDER");
//             this.getLength(drone, decision, parameters);
//         }
//         else if (this.spiral){
//             logger.info("IM IN THE ZOO WITH THE LIONS length: " + this.lengthDistance + " width: " + this.widthDistance);
//             SpiralSearch spiralSearch = new SpiralSearch(this.lengthDistance, this.widthDistance);
//             drone.stop(decision);
//         }

//     }


//     public boolean getHasReachedEndOfIsland(){
//         return this.hasReachedEndOfIsland;
//     }


//     public void setFirstTime(boolean flag){
//         islandEndReacher.setFirstTime(flag);
//     }

//     public void setNeedToTurn(boolean flag){
//         islandEndReacher.setNeedToTurn(flag);
//     }

//     public boolean isFirstTime() {
//         return islandEndReacher.isFirstTime();
//     }
    
//     public boolean isNeedToTurn() {
//         return islandEndReacher.isNeedToTurn();
//     }


//     private void getWidth(Drone drone, JSONObject decision, JSONObject parameters){
//         if (!this.widthFinder.getHasWidth()){
//             this.widthDistance = widthFinder.getWidth(drone, decision, parameters);
//         }
//         else{
//             this.findingLength = true; 
//             this.findingWidth= false; 
//             drone.scan(decision);
//             logger.info("Boy do I have info for you! Take a look what I got about the width of the island pal ;) " + this.widthDistance);
//         }
//     }

    
//     private void getLength(Drone drone, JSONObject decision, JSONObject parameters){
//         if (!this.lengthFinder.getHasLength()){
//             this.lengthDistance = lengthFinder.getLength(drone, decision, parameters);
//         }
//         else{
//             this.findingLength = false; 
//             this.spiral = true; 
//             drone.scan(decision);
//             logger.info("Boy do I have info for you! Take a look what I got about the length of the island pal ;) " + this.lengthDistance);
//         }
//     }


//     private void goToEndOfIsland(Drone drone, JSONObject decision, JSONObject parameters){
//         if (!islandEndReacher.getAtEndOfIsland()){
//             islandEndReacher.reachEndOfIsland(drone, decision, parameters); 
//         }
//         else{
//             this.hasReachedEndOfIsland = true; 
//             this.findingWidth = true; 
//             drone.scan(decision);
//             // drone.stop(decision);
//         } 
//     }



    
// }
