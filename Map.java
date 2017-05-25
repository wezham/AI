
import java.util.*;
import java.io.*;

public class Map {

   //note that the center point is at (2,2) in the view
   public Map (char[][] view) {
      //initialize
      this.verticies = new LinkedList<Vertex>();
      this.boundary = new LinkedList<Vertex>();
      //create verticies
      int x,y;
      for( x=0; x < 5; x++ ) {
         for( y=0; y < 5; y++ ) {
            this.verticies.add(new Vertex(x, y, view[x][y]));
         }
      }

      //if two points have adjacent x or y, connect them
      for (Vertex a : this.verticies) {
         for (Vertex b : this.verticies) {
            // printEdges();
            //if they are neighbours laterally but not diagonally
            Boolean xDiff = Math.abs(a.getPoint().getX() - b.getPoint().getX()) == 1 && a.getPoint().getY() == b.getPoint().getY();
            Boolean yDiff = Math.abs(a.getPoint().getY() - b.getPoint().getY()) == 1 && a.getPoint().getX() == b.getPoint().getX();
            if (xDiff || yDiff) {
               createEdgeBtw(a, b);
            }
         }
      }

      initBoundary();
      System.out.println("Length of vertexes is : " + this.verticies.size());
   }

   public void update(char[][] view, Vertex center, Orientation o) {

      char[][] updatedView =view;// o.orientToNorth(view);

      print_view(updatedView);

      // loop through point in our view
      int x,y;
      //keep a list of newly created verticies to connect appropriately afterward
      LinkedList<Vertex> newVerticies = new LinkedList<Vertex>();
      System.out.println("Current center: " + center);

      for( y=0; y < 5; y++ ) {
         for( x=0; x < 5; x++ ) {
            //account for offset of current center from absolute center
            int relX = x + center.getX() - 2;
            int relY = y + center.getY() - 2;
            Vertex v = new Vertex(relX, relY, updatedView[y][x]);
            //if we have seen a vertex with this point before
            Vertex existingV = containsVertexAtSameLocation(v);
            if (existingV != null) {
               //only update the vertex's data
               // System.out.println("Found Existing " + existingV.getValue() + " " + relX + "," + relY + " - Want to place (" + x + "," + y + ")" +  updatedView[x][y]);
               // System.out.println("Updating Vertex: " + existingV);
               if (existingV.getX() == o.yPositionInFrontOfPlayer(center) && existingV.getY() == o.xPositionInFrontOfPlayer(center)) {
                  System.out.println("Found point in front " + existingV) ;
                  existingV.setPointData(updatedView[y][x]);
                  System.out.println("updated " + existingV) ;
               }
               // System.out.println("Gave ^^ " + String.valueOf(updatedView[x][y]));
            } else {
               //add a new vertex
               System.out.println("New Vertex: " + v);
               this.verticies.add(v);
               newVerticies.add(v);
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
   private void oldToNewBoundaryMatch(int xDiff, int yDiff, LinkedList<Vertex> newBoundaries, LinkedList<Vertex> oldBoundaries) {

      //used to delete from boundary after we are finished
      LinkedList<Vertex> toAddToBoundary = new LinkedList<Vertex>();

      for(Vertex oldB : oldBoundaries) {
         for(Vertex newB : newBoundaries) {
            if (newB.xDistTo(oldB) == xDiff && newB.yDistTo(oldB) == yDiff) {
               //connect the two points and remove the old from boundary
               createEdgeBtw(oldB, newB);
               toAddToBoundary.add(newB);
            }
         }
      }

      for (Vertex v : toAddToBoundary) {
         this.boundary.add(v);
      }

      cleanUpBoundary();
   }

   //removes elements from the boundary list that are no longer boundaries
   public void cleanUpBoundary() {
      LinkedList<Vertex> toDelete = new LinkedList<Vertex>();
      for (Vertex v : this.boundary) {
         if (v.numNeighbours() == 4) {
            toDelete.add(v);
         }
      }
      for (Vertex v : toDelete) {
         this.boundary.remove(v);
      }

   }

   //connects new boundaries (that need to be connected)
   private void newToNewBoundaryMatch(int xDiff, int yDiff, LinkedList<Vertex> b1) {
      for(Vertex v1 : b1) {
         for(Vertex v2 : b1) {
            if ((v1.xDistTo(v2) == xDiff && v1.yDistTo(v2) == yDiff) && !(v1 == v2)) {
               //connect the two points and remove the old from boundary
               //this algo with connect v2 to v1 and v1 to v2, but createEdgeBtw s
               // System.out.println("Connecting : " + v1 + v2);
               // System.out.println(v1.getX() + " - " + v2.getX() + " == " + xDiff + ", or, " + v1.getY() + " - " + v2.getY() + " == " + yDiff);
               createEdgeBtw(v2, v1);
            }
         }
      }
   }

   // checks whether a node exists at the same coordinates as v1. ie if we need to
   // make a new node or just update an existing one
   private Vertex containsVertexAtSameLocation(Vertex v1) {
      for (Vertex v : this.verticies) {
         if (v.samePointAs(v1)) {
            return v;
         }
      }
      return null;
   }

   private void createEdgeBtw(Vertex a, Vertex b) {
      //connet the verticies
      a.addNeighbour(b);
      b.addNeighbour(a);
   }

   public void print() {
      System.out.println("New num verticies is " + this.verticies.size());
      System.out.println("New num boundaries is " + this.boundary.size());
      for (Vertex v : this.verticies) {
         System.out.println("Vertex: " + v);
      }
      for (Vertex v : this.boundary) {
         System.out.println("Boundary: " + v);
      }
   }

   //initially, a boundary is anything without 4 neighbours
   private void initBoundary() {
      for (Vertex v : this.verticies) {
         if (v.numNeighbours() < 4) {
            this.boundary.add(v);
         }
      }
   }

   public Vertex findVertexByCoordinates(int x, int y) {
      for (Vertex v : this.verticies) {
         if(v.getX() == x && v.getY() == y) {
            return v;
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


   private LinkedList<Vertex> boundary;
   private LinkedList<Vertex> verticies;
}
