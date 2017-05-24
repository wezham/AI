
import java.util.*;
import java.io.*;

public class Map {

   //note that the center point is at (2,2) in the view
   public Map (char[][] view) {
      //initialize
      this.verticies = new LinkedList<Vertex>();
      //create verticies
      int x,y;
      for( x=0; x < 5; x++ ) {
         for( y=0; y < 5; y++ ) {
            this.verticies.add(new Vertex(new Point(x,y,view[x][y])));
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

      System.out.println("Length of vertexes is : " + this.verticies.size());
   }

   // public void updateMap(char[][] view, Point center, Orientation o) {
   //    //recenter coordinates with center at 2,2
   //    view = o.orientToNorth(view);
   //    int x,y;
   //    for( x=0; x < 5; x++ ) {
   //       for( y=0; y < 5; y++ ) {
   //          Vertex v = new Vertex(new Point(x,y,view[x][y]));
   //          if (containsVertexAtSameLocation(v) != null) {
   //             v.setPointData(view[x][y]);
   //          } else {
   //             this.verticies.add(v);
   //          }
   //       }
   //    }
   //    // System.out.println("New num verticies is")
   // }
   //
   // // checks whether a node exists at the same coordinates as v1. ie if we need to
   // // make a new node or just update an existing one
   // private Vertex containsVertexAtSameLocation(Vertex v1) {
   //    for (Vertex v : this.verticies) {
   //       if (v.samePointAs(v1)) {
   //          return v;
   //       }
   //    }
   //    return null;
   // }

   private void createEdgeBtw(Vertex a, Vertex b) {
      //connet the verticies
      a.addNeighbour(b);
      b.addNeighbour(a);
   }

   public void print() {
      for (Vertex v : this.verticies) {
         System.out.println("Vertex: " + v);
      }
   }

   private LinkedList<Vertex> verticies;
}
