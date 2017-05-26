
import java.util.*;
import java.io.*;

public class Map {

   //note that the center point is at (2,2) in the view
   public Map (char[][] view) {
      //initialize
      this.verticies = new LinkedList<Point>();
      this.boundary = new LinkedList<Point>();
      //create verticies
      int x,y;
      for( x=0; x < 5; x++ ) {
         for( y=0; y < 5; y++ ) {
            char value = view[y][x];
            //add the players direction char in
            if (x == 2 && y == 2) {
               value = '^';
            }
            this.verticies.add(new Point(x, y, value));
         }
      }

      //if two points have adjacent x or y, connect them
      for (Point a : this.verticies) {
         for (Point b : this.verticies) {
            // printEdges();
            //if they are neighbours laterally but not diagonally
            Boolean xDiff = Math.abs(a.getX() - b.getX()) == 1 && a.getY() == b.getY();
            Boolean yDiff = Math.abs(a.getY() - b.getY()) == 1 && a.getX() == b.getX();
            if (xDiff || yDiff) {
               createEdgeBtw(a, b);
            }
         }
      }

      initBoundary();
   }

   public void update(char[][] view, Point center, Orientation o) {

      char[][] updatedView = o.orientToNorth(view);

      print_view(updatedView);

      // loop through point in our view
      int x,y;
      //keep a list of newly created verticies to connect appropriately afterward
      LinkedList<Point> newVerticies = new LinkedList<Point>();
      System.out.println("Current center: " + center);

      for( y=0; y < 5; y++ ) {
         for( x=0; x < 5; x++ ) {
            //account for offset of current center from absolute center
            int relX = x + center.getX() - 2;
            int relY = y + center.getY() - 2;
            char value = updatedView[y][x];
            //add the players direction char in
            if (x == center.getX() && y == center.getY()) {
               value = o.playerCharacter();
            }
            Point p = new Point(relX, relY, value);
            //if we have seen a vertex with this point before
            Point existingV = containsPointAtSameLocation(p);
            if (existingV != null) {
               existingV.setValue(value);
            } else {
               //add a new vertex
               // System.out.println("New Vertex: " + p);
               this.verticies.add(p);
               newVerticies.add(p);
            }
         }
      }

      // created edges between newly created verticies (which are new boundaries) and
      //old boundaries
      if (newVerticies.size() > 0) {
         switch(o.getOrientation()) {
            case 'N':
               //look for vert where new boundary y - old boundary y = 1
               oldToNewBoundaryMatch(0, 1, newVerticies, this.boundary);
               newToNewBoundaryMatch(1, 0, newVerticies);
            break;
            case 'S':
               //look for vert where old boundary y - new boundary y = -1
               oldToNewBoundaryMatch(0, 1, this.boundary, newVerticies);
               newToNewBoundaryMatch(1, 0, newVerticies);
            break;
            case 'E':
               //look for vert where new boundary x - old boundary x = 1
               oldToNewBoundaryMatch(1, 0, newVerticies, this.boundary);
               newToNewBoundaryMatch(0, 1, newVerticies);
            break;
            default:
               oldToNewBoundaryMatch(1, 0, this.boundary, newVerticies);
               newToNewBoundaryMatch(0, 1, newVerticies);
         }
      }
   }


   //searches for pairs btw old and new boundaries with the x and y differences passed as params
   // eg. when adding new boundaries to the north of us, look for vert where new boundary y - old boundary y = 1
   //params are in form: new - old = k;
   private void oldToNewBoundaryMatch(int xDiff, int yDiff, LinkedList<Point> newBoundaries, LinkedList<Point> oldBoundaries) {

      //used to delete from boundary after we are finished
      LinkedList<Point> toAddToBoundary = new LinkedList<Point>();

      for(Point oldB : oldBoundaries) {
         for(Point newB : newBoundaries) {
            if (newB.xDistTo(oldB) == xDiff && newB.yDistTo(oldB) == yDiff) {
               //connect the two points and remove the old from boundary
               createEdgeBtw(oldB, newB);
               toAddToBoundary.add(newB);
               toAddToBoundary.add(oldB);
            }
         }
      }

      //add the newly created lists to the boundary set
      for (Point p : toAddToBoundary) {
         if (!this.boundary.contains(p)) {
            this.boundary.add(p);
         }
      }


      //delete the now no longer boundary points
      cleanUpBoundary();
   }

   //removes elements from the boundary list that are no longer boundaries
   public void cleanUpBoundary() {
      LinkedList<Point> toDelete = new LinkedList<Point>();
      for (Point p : this.boundary) {
         if (p.numNeighbours() == 4) {
            toDelete.add(p);
         }
      }
      for (Point p : toDelete) {
         this.boundary.remove(p);
      }

   }

   //connects new boundaries (that need to be connected)
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

   // checks whether a node exists at the same coordinates as v1. ie if we need to
   // make a new node or just update an existing one
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
   private void initBoundary() {
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


   private LinkedList<Point> boundary;
   private LinkedList<Point> verticies;
}
