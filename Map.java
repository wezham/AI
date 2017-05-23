
import java.util.*;
import java.io.*;

public class Map {

   //note that the center point is at (2,2) in the view
   public Map (char[][] view) {
      //initialize
      this.verticies = new LinkedList<Vertex>();
      this.edges = new LinkedList<Edge>();
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
            //if they are neighbours laterally but not diagonally
            Boolean xDiff = Math.abs(a.point().getX() - b.point().getX()) == 1 && a.point().getY() == b.point().getY();
            Boolean yDiff = Math.abs(a.point().getY() - b.point().getY()) == 1 && a.point().getX() == b.point().getX();
            if (xDiff || yDiff) {
               // if (xDiff ^ yDiff) {
                  createEdgeBtw(a, b);
                  System.out.println("Making Edge " + a.point() + b.point());
               // }
            }
         }
      }

      System.out.println("Length of vertexes is : " + this.verticies.size());
      System.out.println("Length of edges is : " + this.edges.size());
   }

   private void createEdgeBtw(Vertex a, Vertex b) {
      //add edge
      Edge e = new Edge(a, b);
      //eliminates duplicates
      if (!this.edges.contains(e)) {
         this.edges.add(e);
         a.addNeighbour(b.point());
         b.addNeighbour(a.point());
      }
   }


   private LinkedList<Vertex> verticies;
   private LinkedList<Edge> edges;
}
