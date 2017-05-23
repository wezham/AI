
import java.util.*;
import java.io.*;

public class Vertex {

   public Vertex (Point point) {
      this.point = point;
      this.neighbours = new LinkedList<Point>();
   }

   public void addNeighbour(Point p) {
      this.neighbours.add(p);
   }

   public boolean isNeighbour (Point p) {
      return neighbours.contains(p);
   }

   //returns the point of a vertex. excuse naming, reads nicely: vertex.point();
   public Point point() {
      return this.point;
   }

   @Override
   public boolean equals(Object other){
     if (!(other instanceof Vertex)) {
          return false;
     }else{
         Vertex otherClass = (Vertex)other;
         return( this.point.equals(otherClass.point()));
     }
   }

   private LinkedList<Point> neighbours;
   private Point point;
}
