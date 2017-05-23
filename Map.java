
import java.util.*;
import java.io.*;

public class Map {

   //note that the center point is at (2,2) in the view
   public Map (char[][] view, Point center) {
      //initialize
      this.verticies = new LinkedList<Vertex>();
      //create verticies
      int i,j;
      for( i=0; i < 5; i++ ) {
         for( j=0; j < 5; j++ ) {
            Vertex v = new Vertex(new Point(i,j,view[i][j]));
         }
      }

   }

   private void createEdgeBtw(Vertex a, Vertex b) {
      a.addNeighbour(b.point());
      b.addNeighbour(a.point());
   }

   private LinkedList<Vertex> verticies;
}
