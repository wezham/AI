
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
      return this.point.toString();
   }

   //
   public boolean samePointAs(Vertex other){
      return this.point.sameLocationAs(other.getPoint());
   }

   private LinkedList<Point> neighbours;
   private Point point;
}
