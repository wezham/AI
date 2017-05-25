
import java.util.*;
import java.io.*;

public class Vertex {

   public Vertex (Point point) {
      this.point = point;
      this.neighbours = new LinkedList<Vertex>();
   }

   public void addNeighbour(Vertex v1) {
      //if the neighbours doesnt already contain a node at the same point:
      boolean contains = false;
      for (Vertex v : this.neighbours) {
         if (v.samePointAs(v1)) {
            contains = true;
         }
      }
      if (!contains) {this.neighbours.add(v1);}
   }

   public Point getPoint() {
      return this.point;
   }

   @Override
   public boolean equals(Object other){
     if (!(other instanceof Vertex)) {
          return false;
     }else{
         Vertex otherClass = (Vertex)other;
         return( this.point.equals(otherClass.getPoint()));
     }
   }

   public void setPointData(char c) {
      this.point.setValue(c);
   }

   public String toString() {
      //turn list into string
      String s = "";
      for(Vertex v : this.neighbours) {
         s += v.getPoint().toString();
      }
      return this.point.toString() + " - neighbours: " + s;
   }

   //
   public boolean samePointAs(Vertex other){
      return this.point.sameLocationAs(other.getPoint());
   }

   private LinkedList<Vertex> neighbours;
   private Point point;
}