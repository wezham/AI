
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

   public char get_action( char view[][] ) {
     char move = 'L';
    //  System.out.println("Current center: " + currentVertex);

     //FIRST THING IS UPDATE THE MAP

     if(gottenTreasure && (pathToTake.size() == 0)) {
       returnFromGoal();
     }

     if (counter == 0) {
        //if it is our first move
        gottenTreasure = false;
        initToolkit();
        this.orientation = new Orientation();
        this.pathPlanner = new PathPlanner();
        this.exploredPoints = new LinkedList<Point>();
        pathToTake = new LinkedList<Character>();
        this.map = new Map(view);
        this.search = new Search(this.map);
        // map.print();
        currentVertex = map.findVertexByCoordinates(2,2);
      } else {
        map.update(view, currentVertex, orientation);
        // map.print();
      }

      if (pathToTake.size() == 0) {
         get_actions(currentVertex);
      }

      counter ++;
      move = pathToTake.pollFirst();
      //update orientation once weve decided our move
      orientation.updateOrientation(move, currentVertex);

      //update the vertex to the new point
      currentVertex = newVertexCalc(view, move);

      //add tools to toolkit if aquired
      addToToolKitIfUseful(currentVertex);

      //based on what we are about to do, update toolkit
      updateToolKit(move);

      //remove any tools or trees from our list if we aquire them or cut tree
      map.removeIfAcquired(currentVertex);
      //
      this.exploredPoints.add(currentVertex);
      // System.out.println("Making Move: " + move);



      return move;
   }

   private void get_actions(Point playerPos){
     //Our point
     Point player = playerPos;
     LinkedList<Point> boundaryPoints = this.map.getGoodBoundaries();
     ListIterator<Point> it = boundaryPoints.listIterator();
     LinkedList<Point> path = new LinkedList<Point>();

     //set the first end point to the first element of the iterator
     if(boundaryPoints.size() > 0){
       Point mayExplore = it.next();
       //search for the first path
       path = this.search.aStar(player, mayExplore, orientation);

       while((containsBlockers(path) || exploredPoints.contains(mayExplore)) && it.hasNext()){
         mayExplore = it.next();
         path = this.search.aStar(player, mayExplore, orientation);
       }
       if(path.size() > 0 && !containsBlockers(path)){
          pathToTake = pathPlanner.generatePath(path, orientation);
       } else {
          //get list of points with neighbours that are obstacles
          PriorityQueue<Point> pointsNextToObs = map.setAndGetObstaclePoints();
          mayExplore = pointsNextToObs.poll();
          path = this.search.aStar(player, mayExplore, orientation);
          while((containsBlockers(path) || exploredPoints.contains(mayExplore)) && (pointsNextToObs.peek() != null)){
           //  map.print();
            path = this.search.aStar(player, mayExplore, orientation);
            mayExplore = pointsNextToObs.poll();
          }
          if(path.size() > 0 && !containsBlockers(path)){
            pathToTake = pathPlanner.generatePath(path, orientation);
         }else{
           getTreasure();
         }
       }
     }else{
       getTreasure();
     }

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

   private void getTreasure(){
     System.out.println(this.map.trees());
     System.out.println(this.map.keys());
     System.out.println(this.map.dynamites());
     System.out.println(this.map.axes());
     System.out.println(this.map.treasure());
     if(map.treasure() != null){
       LinkedList<Point> path = this.search.aStar(currentVertex, map.treasure(), orientation);
       if(path.size() > 0){
         pathToTake = pathPlanner.generatePath(path, orientation);
         gottenTreasure = true;
         System.out.println(toolkit.values());
       }
     }
   }

   private void returnFromGoal(){
      LinkedList<Point> path = this.search.aStar(currentVertex, map.findVertexByCoordinates(2,2), orientation);
      if(path.size() > 0){
       pathToTake = pathPlanner.generatePath(path, orientation);
     }
   }

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
