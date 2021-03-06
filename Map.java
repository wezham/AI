
import java.util.*;
import java.io.*;
//We build a map of the world as we explore it so we can remember where things were
public class Map {


   //note that the center point is at (2,2) in the view
   public Map (char[][] view) {
      //initialize
      this.verticies = new LinkedList<Point>();
      this.boundary = new LinkedList<Point>();
      this.axes = new LinkedList<Point>();
      this.keys = new LinkedList<Point>();
      this.dynamites = new LinkedList<Point>();
      this.trees = new LinkedList<Point>();
      this.doors = new LinkedList<Point>();

      this.pointsNextToObsticles = new PriorityQueue<Point>(40, this.neighbourComparator);
      //create verticies
      int x,y;
      for( x=0; x < 5; x++ ) {
         for( y=0; y < 5; y++ ) {
            char value = view[y][x];
            //add the players direction char in
            if (x == 2 && y == 2) { value = '^'; } //set player pos first time round
            Point p = new Point(x, y, value);
            addIfUseful(p);
            this.verticies.add(p);
         }
      }

      //if two points have adjacent x or y, connect them
      for (Point a : this.verticies) {
         for (Point b : this.verticies) {
            //if they are neighbours laterally but not diagonally, create an edge between them
            Boolean xDiff = Math.abs(a.getX() - b.getX()) == 1 && a.getY() == b.getY();
            Boolean yDiff = Math.abs(a.getY() - b.getY()) == 1 && a.getX() == b.getX();
            if (xDiff || yDiff) {
               createEdgeBtw(a, b);
            }
         }
      }

      setBoundary();
   }

   //Get valuable information lists and points
   public LinkedList<Point> trees(){
     return this.trees;
   }
   public LinkedList<Point> axes(){
     return this.axes;
   }
   public LinkedList<Point> dynamites(){
     return this.dynamites;
   }
   public LinkedList<Point> keys(){
     return this.keys;
   }
   public Point treasure(){
     return this.treasure;
   }
   public  LinkedList<Point> doors(){
     return this.doors;
   }

   //itterates over (land based) points which border multiple boundaries. These points are adjacent to boundaries and will
   //be likelky to give us information if we visit them
   public PriorityQueue<Point> setAndGetObstaclePoints(){
      this.pointsNextToObsticles =  new PriorityQueue<Point>(40, this.neighbourComparator);
      for(Point p : this.verticies){
         if(p.numObstacleNeighbours() > 0 && (p.numObstacleNeighbours() != 4) && !(isBlocker(p))){
            this.pointsNextToObsticles.add(p);
         }
      }return this.pointsNextToObsticles;
   }

   public PriorityQueue<Point> getPtsNextToObs() {
      return this.pointsNextToObsticles;
   }

   public LinkedList<Point> getBoundaries(){
     return this.boundary;
   }

   //Searches through the graph and returns a list of points which are boundary points, and also are accessible without tools
   public LinkedList<Point> getGoodBoundaries(){
    LinkedList<Point> goodBoundary = new LinkedList<Point>();
    for(Point p : this.boundary) {
       if (!isBlocker(p)) {
         goodBoundary.add(p);
       }
     }
     return goodBoundary;
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

   //adds tools to our toolkit if we find them
   private void addIfUseful(Point p) {
     switch(p.getValue()){
       case('d'): System.out.println("add="+p);this.dynamites.add(p);break;
       case('$'): this.treasure = p;break;
       case('-'): this.doors.add(p);break;
       case('k'): this.keys.add(p);break;
       case('a'): this.axes.add(p);break;
       case('T'): this.trees.add(p);break;
       default: ;
     }
   }

   //removes a tool once its been used
   public void removeIfAcquired(Point currentP) {
     Point toRemove = null;
     for (Point p2 : this.dynamites) {
       if (currentP.sameLocationAs(p2)) {
          toRemove = p2 ;
          break;
       }
     }
     if (toRemove != null) {this.dynamites.remove(toRemove);}
     for (Point p2 : this.keys) {
       if (currentP.sameLocationAs(p2)) {
          toRemove = p2 ;
          break;
       }
     }
     if (toRemove != null) {this.keys.remove(toRemove);}
     for (Point p2 : this.axes) {
       if (currentP.sameLocationAs(p2)) {
          toRemove = p2 ;
          break;
       }
     }
     if (toRemove != null) {this.axes.remove(toRemove);}
     for (Point p2 : this.trees) {
       if (currentP.sameLocationAs(p2)) {
          toRemove = p2 ;
          break;
       }
     }
     if (toRemove != null) {this.trees.remove(toRemove);}
   }

   public void update(char[][] view, Point center, Orientation o) {

      char[][] updatedView = o.orientToNorth(view);


      // loop through point in our view
      int x,y;
      //keep a list of newly created verticies to connect appropriately afterward
      LinkedList<Point> newVerticies = new LinkedList<Point>();
      // System.out.println("Current center: " + center);

      for( y=0; y < 5; y++ ) {
         for( x=0; x < 5; x++ ) {
            //account for offset of current center from absolute center
            int relX = x + center.getX() - 2;
            int relY = y + center.getY() - 2;
            char value = updatedView[y][x];

            Point p = new Point(relX, relY, value);
            //if we have seen a vertex with this point before
            Point existingV = containsPointAtSameLocation(p);
            if (existingV != null) {
               //only update the value
               existingV.setValue(value);
            } else {
               //otherwise create a new vertex
               if (p.getValue() != '.') {
                 this.verticies.add(p);
                 newVerticies.add(p);
                 addIfUseful(p);
               }
            }
         }
      }

      //place the player into our map
      findVertexByCoordinates(center.getX(), center.getY()).setValue(o.playerCharacter());

      // connects all the new points in our boundaries to our map and visa versa
      if (newVerticies.size() > 0) {
         //connect the new points to the existing map
         switch(o.getOrientation()) {
            case 'N':
               newToNewBoundaryMatch(1, 0, newVerticies);
            break;
            case 'S':
               newToNewBoundaryMatch(1, 0, newVerticies);
            break;
            case 'E':
               newToNewBoundaryMatch(0, 1, newVerticies);
            break;
            default:
               newToNewBoundaryMatch(0, 1, newVerticies);
         }
         //connect the new points to eachother
         oldToNewBoundaryMatch(newVerticies, this.boundary);
      }
   }


   //connects the newly created boundary points to the existing map
   private void oldToNewBoundaryMatch(LinkedList<Point> newBoundaries, LinkedList<Point> oldBoundaries) {

      for(Point oldB : oldBoundaries) {
         for(Point newB : newBoundaries) {
            Boolean xDiff = Math.abs(newB.xDistTo(oldB)) == 1 && newB.getY() == oldB.getY();
            Boolean yDiff = Math.abs(newB.yDistTo(oldB)) == 1 && newB.getX() == oldB.getX();
            if (xDiff || yDiff) {
               createEdgeBtw(oldB, newB);
            }
         }
      }


      //clean up the boundary
      setBoundary();
   }

   //connects new boundaries to eachother
   private void newToNewBoundaryMatch(int xDiff, int yDiff, LinkedList<Point> b1) {
      for(Point p1 : b1) {
         for(Point p2 : b1) {
            if ((p1.xDistTo(p2) == xDiff && p1.yDistTo(p2) == yDiff) && !(p1 == p2)) {
               //connect the two points
               createEdgeBtw(p2, p1);
            }
         }
      }
   }

   //checks if twp poitns are at the same location
   private Point containsPointAtSameLocation(Point p1) {
      for (Point p : this.verticies) {
         if (p.sameLocationAs(p1)) {
            return p;
         }
      }
      return null;
   }

   private void createEdgeBtw(Point a, Point b) {
      //connet the verticies
      a.addNeighbour(b);
      b.addNeighbour(a);

   }

   public void print() {
      System.out.println("New num verticies is " + this.verticies.size());
      System.out.println("New num boundaries is " + this.boundary.size());
      for (Point p : this.verticies) {
         System.out.println("Vertex: " + p);
      }
      for (Point p : this.boundary) {
         System.out.println("Boundary: " + p);
      }
   }

   //initially, a boundary is anything without 4 neighbours
   private void setBoundary() {
      //first clear, then calculate again
      this.boundary = new LinkedList<Point>();
      for (Point p : this.verticies) {
         if (p.numNeighbours() < 4) {
            this.boundary.add(p);
         }
      }
   }

   public Point findVertexByCoordinates(int x, int y) {
      for (Point p : this.verticies) {
         if(p.getX() == x && p.getY() == y) {
            return p;
         }
      }
      //this should never happen, need to throw exception?
      return null;
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

  //  Astar clear parent points
  public void clearParentPoints(){
    for(Point p : this.verticies){
      p.setPrevious(null);
    }
  }
  // For comparing num neighbours that are obstacles
  private Comparator<Point> neighbourComparator = new Comparator<Point>(){
     @Override
     public int compare(Point o1, Point o2){
         return o2.numObstacleNeighbours() - o1.numObstacleNeighbours();
     }
};

   private PriorityQueue<Point> pointsNextToObsticles;
   private LinkedList<Point> boundary;
   private LinkedList<Point> verticies;
   private LinkedList<Point> axes;
   private LinkedList<Point> dynamites;
   private LinkedList<Point> keys;
   private LinkedList<Point> trees;
   private LinkedList<Point> doors;
   private Point treasure;
}
