
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.PriorityQueue;

public class Agent {

   private Integer counter = 0;
   private LinkedList<Character> moves = new LinkedList<Character>();
   private Orientation orientation;
   private Map map;
   private Point currentVertex;
   private PathPlanner pathPlanner;
   private Search search;
   private LinkedList<Character> pathToTake;
   private LinkedList<Point> exploredPoints;
   private boolean gottenTreasure;
   private HashMap<Character, Integer> toolkit;
   private Heuristic heuristic;

   public char get_action( char view[][] ) {
     char move = 'L';
     if(gottenTreasure && (pathToTake.size() == 0)) { returnFromGoal(true); } // if we have the treasure try return to starting point
     if (counter == 0) { initaliseVars(view); } // initalise variables on first move
     else { map.update(view, currentVertex, orientation); } //otherwise update the map
     if (pathToTake.size() == 0) { get_actions(currentVertex); } //we need to generate a new path
     counter ++;
     move = pathToTake.pollFirst();
     simulateMoveAndUpdate(move, view);
     return move;
   }


   private void simulateMoveAndUpdate(char move, char[][] view){
     orientation.updateOrientation(move, currentVertex); //update orientation once weve decided our move
     currentVertex = newVertexCalc(view, move);  //update the vertex to the new point
     addToToolKitIfUseful(currentVertex); //add tools to toolkit if aquired
     updateToolKit(move); //based on what we are about to do, update toolkit
     map.removeIfAcquired(currentVertex);  //remove any tools or trees from our list if we aquire them or cut tree
     this.exploredPoints.add(currentVertex); // add to our explored points
   }

   private void initaliseVars(char[][] view){
     //if it is our first move
     gottenTreasure = false;
     initToolkit();
     this.orientation = new Orientation();
     this.pathPlanner = new PathPlanner();
     this.exploredPoints = new LinkedList<Point>();
     this.heuristic = new Heuristic();
     pathToTake = new LinkedList<Character>();
     this.map = new Map(view);
     this.search = new Search(this.map);
     // map.print();
     currentVertex = map.findVertexByCoordinates(2,2);
   }

   private boolean get_actions(Point playerPos){
     //Our point
     Point player = playerPos;
     LinkedList<Point> boundaryPoints = this.map.getGoodBoundaries();
     ListIterator<Point> it = boundaryPoints.listIterator();
     LinkedList<Point> path = new LinkedList<Point>();
     //set the first end point to the first element of the iterator
     System.out.println("Trying to get treasure");
     if(weCanGetTreasure(player)){ gottenTreasure = true; return true; }
     System.out.println("Trying to search boundaries");
     if(foundValidPathsToAdd(searchForBoundaryPoints(it, player))) { return true; }
     System.out.println("Trying to search for objects next to obstacles");
     if(foundValidPathsToAdd(searchForPointsNextToObstacles(player))){ return true; }
     System.out.println("Trying to find keys");
     if(getKeys()){ return true; }
     System.out.println("Trying to unlock doors");
     if(unlockDoors()){ return true; }
     System.out.println("Trying to get dynamites");
     if(getDynamites()){ return true; }
     System.out.println("Trying to get axes");
     if(getAxes()){ return true; }
     System.out.println("Trying to get raft");
     if(getRaft()){return true; }
     System.out.println("Trying to get to treasure via water");
     if(getToTreasureViaWater()){ return true; };
     printItemsFound();
     System.out.println("trying to find alt path");
     if(cutTrees()){return true;};
     printItemsFound();
    //  if()
     //suppose all the above is untrue we want to take uncostly actions
     //this includes opening doors or cutting trees ( such that we dont impose future costs)

     return false;
   }

   //for each tree that we know exists, can we find one given the tools we currently have
   private boolean cutTrees(){
     boolean result = false;
     ListIterator<Point> treesIterator = this.map.trees().listIterator();
     //if we have an axe and we know of trees, itterate over them trying to find one we can access
     if(toolkit.get('a') > 0 && this.map.trees().size() > 0){
       while(treesIterator.hasNext()){
         Point tree = treesIterator.next();
         LinkedList<Point> path = this.search.aStar(currentVertex, tree, orientation, heuristic, toolkit, true);
         if(findValidPathToObject(path)){ this.map.trees().remove(tree); result = true; break;}
       }
       return result;
     }
     return result;
   }

    //for each dynamite that we know exists, can we find one given the tools we currently have
   private boolean getDynamites(){
     boolean result = false;
     ListIterator<Point> dynamitesIterator = this.map.dynamites().listIterator();
     if(this.map.dynamites().size() > 0){
       while(dynamitesIterator.hasNext()){
         Point dynamite = dynamitesIterator.next();
         LinkedList<Point> path = this.search.aStar(currentVertex, dynamite, orientation, heuristic, toolkit, true);
         if(findValidPathToObject(path)){ this.map.dynamites().remove(dynamite); result = true; break;}
       }
       return result;
     }
     return result;
   }

    //tries to find a path to the goal via water
   private boolean getToTreasureViaWater(){
     boolean result = false;
     //if we know where the treasure is, and we have a raft, run an A*
     if(toolkit.get('r') > 0 && (this.map.treasure() != null)){
       boolean obstacledAllowed = true;
       LinkedList<Point> path = this.search.aStar(currentVertex, this.map.treasure(), orientation, heuristic, toolkit, obstacledAllowed);
       if(path.size() > 0){
         pathToTake = pathPlanner.generatePath(path, orientation);
         result = true;
       }
     }
     return result;
   }

   //tries to build a raft
   private boolean getRaft(){
     boolean result = false;
     //given that we dont have a raft and we know where a tree is
     if(toolkit.get('r') == 0 && this.map.trees().size() > 0){
       boolean noObstacles = false;
       ListIterator<Point> treeIter = this.map.trees().listIterator();
       LinkedList<Point> path;
       while(treeIter.hasNext()){
         Point tree = treeIter.next();
         path = this.search.aStar(currentVertex, tree, orientation, heuristic, toolkit, noObstacles);
         if(findValidPathToObject(path)){ result = true; break; }
       }
       return result;
     }
     return result;
   }

   //tries to find an axe
   private boolean getAxes(){
     boolean obstaclesAllowed = true;
     if(toolkit.get('a') == 0 && this.map.axes().size() > 0){
       Point axe = this.map.axes().peek();
       LinkedList<Point> path = this.search.aStar(currentVertex, axe, orientation, heuristic, toolkit, obstaclesAllowed);
       this.pathToTake = pathPlanner.generatePath(path, orientation);
       return true;
     }
     return false;
   }
   //Lets see if we found valid results here
   private boolean foundValidPathsToAdd(LinkedList<Point> pathFound){
      if(pathFound.size() > 0 && !containsBlockers(pathFound)){
         pathToTake = pathPlanner.generatePath(pathFound, orientation);
         return true;
      }
      return false;
   }

    //checks a path to see if it is directly accessible without the use of tools
   private boolean findValidPathToObject(LinkedList<Point> pathFound){
     Point last = pathFound.pollLast();
     if(pathFound.size() > 0 && !containsBlockers(pathFound)){
        pathFound.add(last);
        pathToTake = pathPlanner.generatePath(pathFound, orientation);
        return true;
     }
     return false;
   }

   //itterates over (land based) boundary points and tries to find a path to one. This drives exploration
   private LinkedList<Point> searchForBoundaryPoints(ListIterator<Point> it, Point player){
     Point mayExplore = it.next();
     LinkedList<Point> path = this.search.aStar(player, mayExplore, orientation, heuristic, toolkit, false);
     while((containsBlockers(path) || exploredPoints.contains(mayExplore)) && it.hasNext()){
       mayExplore = it.next();
       path = this.search.aStar(player, mayExplore, orientation, heuristic, toolkit, false);
     }
     return path;
   }

   //itterates over (land based) points which border multiple boundaries. These points are adjacent to boundaries and will
   //be likelky to give us information if we visit them
   private LinkedList<Point> searchForPointsNextToObstacles(Point player){
     PriorityQueue<Point> pointsNextToObs = map.setAndGetObstaclePoints();
     Point mayExplore = pointsNextToObs.poll();
     LinkedList<Point> path = this.search.aStar(player, mayExplore, orientation, heuristic, toolkit, false);
     while((containsBlockers(path) || exploredPoints.contains(mayExplore)) && (pointsNextToObs.peek() != null)){
       path = this.search.aStar(player, mayExplore, orientation, heuristic, toolkit, false);
       mayExplore = pointsNextToObs.poll();
     }
     return path;
   }

   //checks if we can access the treasure from our current location
   private boolean weCanGetTreasure(Point player){
      boolean withoutObstacles = false;
      Point treasureLocation = this.map.treasure();
      LinkedList<Point> path = new LinkedList<Point>();
      if(treasureLocation != null){
        path = this.search.aStar(player, treasureLocation, orientation, heuristic, toolkit, withoutObstacles);
      }
      if(foundValidPathsToAdd(path)){ return true; }
      else{ return false; }
   }

   private void updateToolKit(char move) {
      switch(move){
       case('B'): removeToolFromToolkit('d');break;
       case('C'): addToolToToolkit('r');break;
       default: ;
     }
   }

   private void addToToolKitIfUseful(Point p) {
     switch(p.getValue()){
       case('d'): addToolToToolkit('d');System.out.println("Found Dyno");break;
       case('k'): addToolToToolkit('k');System.out.println("Found Key");break;
       case('a'): addToolToToolkit('a');;System.out.println("Found Axe");break;
       default: ;
     }
   }

   private void printItemsFound(){
     System.out.println("This is what we've seen");
     System.out.println(this.map.trees());
     System.out.println(this.map.keys());
     System.out.println(this.map.dynamites());
     System.out.println(this.map.axes());
     System.out.println(this.map.doors());
     System.out.println(this.map.treasure());
     System.out.println("This is what we have");
     System.out.println(toolkit);
     System.out.println("Num moves made: "+counter);
   }

   //checks if we can find any keys
   private boolean getKeys(){
     //given that we dont have a key, and know the location of a key, try find one that is accesible
      if(toolkit.get('k') == 0 && this.map.keys().size() > 0){
         for(Point key : this.map.keys()){
            if(!containsBlockers(this.search.aStar(currentVertex, key, orientation, heuristic, toolkit, false))){
               System.out.println("getting keys");
               LinkedList<Point> path = this.search.aStar(currentVertex, key, orientation, heuristic, toolkit,false);
               pathToTake = pathPlanner.generatePath(path, orientation);
               this.map.keys().remove(key);
               return true;
            }
         }
      }
      return false;
   }

   //tries to unlock doors
   private boolean unlockDoors(){
      if(toolkit.get('k') > 0 && this.map.doors().size() > 0){
         for(Point door : this.map.doors()){
            LinkedList<Point> path = this.search.aStar(currentVertex, door, orientation, heuristic, toolkit, false);
            Point last = path.pollLast();
            if(!containsBlockers(path)){
               System.out.println("Unlocking door");
               path.add(last);
               pathToTake = pathPlanner.generatePath(path, orientation);
               pathToTake.add('U');
               this.map.doors().remove(door);
               return true;
            }
         }
      }
      return false;
   }

   private void returnFromGoal(boolean withObjects){
      LinkedList<Point> path = this.search.aStar(currentVertex, map.findVertexByCoordinates(2,2), orientation, heuristic, toolkit, withObjects);
      if(path.size() > 0){
       pathToTake = pathPlanner.generatePath(path, orientation);
     }
   }

   //checks if a path contains any points that require tools to get past
   private boolean containsBlockers(LinkedList<Point> path){
     boolean val = false;
     for(Point p : path){
       if (isBlocker(p)) {
         val = true;
       }
     }
    return val;
   }

   private boolean isBlocker(Point p) {
     switch(p.getValue()){
       case('T'): return true;
       case('-'): return true;
       case('*'): return true;
       case('~'): return true;
       default: return false;
     }
  }

 private void addToolToToolkit(char c) {
    int count = this.toolkit.get(c);
    this.toolkit.put(c, (count + 1));
 }

 private int getToolCount(char c) {
    return this.toolkit.get(c);
}

 private void removeToolFromToolkit(char c) {
    if (this.toolkit.get(c) == 0) {
   }
    int count = this.toolkit.get(c);
    this.toolkit.put(c, count - 1);
 }

 private void initToolkit() {
     this.toolkit = new HashMap<Character, Integer>();
     this.toolkit.put('a', 0);
     this.toolkit.put('d', 0);
     this.toolkit.put('k', 0);
     this.toolkit.put('r', 0);
}

   //only called if not the first action, meaning that currentState.point != null
   //given a move to make, caluclates the absolute center position of the new state
   private Point newVertexCalc(char[][] view, char action) {
      //if we arent moving, the center remains the same
      Point newVertex = currentVertex;
      int x = currentVertex.getX();
      int y = currentVertex.getY();
      char absOrientation = orientation.getOrientation();

      if (action == 'F') {
         //adjusts the center position for
         switch (absOrientation) {
            case 'S':
               y++; break;
            case 'N':
               y--; break;
            case 'E':
               x++; break;
            default:
               x--;
         }
      }


      return map.findVertexByCoordinates(x, y);
   }

   void print_view( char view[][] )
   {
      int i,j;
      System.out.println("\n+-----+");
      for( i=0; i < 5; i++ ) {
         System.out.print("|");
         for( j=0; j < 5; j++ ) {
            if(( i == 2 )&&( j == 2 )) {
               System.out.print('^');
            }
            else {
               System.out.print( view[i][j] );
            }
         }
         System.out.println("|");
      }
      System.out.println("+-----+");
   }

   public static void main( String[] args )
   {
      InputStream in  = null;
      OutputStream out= null;
      Socket socket   = null;
      Agent  agent    = new Agent();
      char   view[][] = new char[5][5];
      char   action   = 'F';
      int port;
      int ch;
      int i,j;

      if( args.length < 2 ) {
         System.out.println("Usage: java Agent -p <port>\n");
         System.exit(-1);
      }

      port = Integer.parseInt( args[1] );

      try { // open socket to Game Engine
         socket = new Socket( "localhost", port );
         in  = socket.getInputStream();
         out = socket.getOutputStream();
      }
      catch( IOException e ) {
         System.out.println("Could not bind to port: "+port);
         System.exit(-1);
      }

      try { // scan 5-by-5 wintow around current location
         while( true ) {
            for( i=0; i < 5; i++ ) {
               for( j=0; j < 5; j++ ) {
                  if( !(( i == 2 )&&( j == 2 ))) {
                     ch = in.read();
                     if( ch == -1 ) {
                        System.exit(-1);
                     }
                     view[i][j] = (char) ch;
                  }
               }
            }
            // agent.print_view( view ); // COMMENT THIS OUT BEFORE SUBMISSION
            action = agent.get_action( view );
            out.write( action );
         }
      }
      catch( IOException e ) {
         System.out.println("Lost connection to port: "+ port );
         System.exit(-1);
      }
      finally {
         try {
            socket.close();
         }
         catch( IOException e ) {}
      }
   }
}
