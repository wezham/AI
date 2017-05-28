
import java.util.*;
import java.io.*;

public class Point{

   public Point (Integer x, Integer y, char value) {
      this.value = value;
      this.x = x;
      this.y = y;
      this.neighbours = new LinkedList<Point>();
      this.numObstacleNeighbours = 0;
   }
   // Setters and Getters
   public Integer getX (){
      return this.x;
   }

   public Integer getY (){
      return this.y;
   }

   public char getValue (){
      return this.value;
   }

   public void setValue (char val){
      this.value = val;
   }

   public int xDistTo(Point p2) {
      return this.x - p2.getX();
   }

   public int yDistTo(Point p2) {
      return this.y - p2.getY();
   }
   public LinkedList<Point> neighbours(){
     return this.neighbours;
   }
   public int numNeighbours() {
      return this.neighbours.size();
   }

   //A Star
   private Point parentPoint;
   private float gCost;
   private float hCost;
   private float fCost;
   // Set previous point for path recollection
   public Point getParentPoint(){
     return this.parentPoint;
   }
   public void setPrevious(Point pp){
     this.parentPoint = pp;
   }
   public float getGCost(){
     return this.gCost;
   }
   public float getHCost(){
     return this.hCost;
   }
   public float getFCost(){
     return this.fCost;
   }
   public void setGCost(float distance){
     if(this.parentPoint != null){
      this.gCost = this.parentPoint.getGCost() + distance;
    }else{
      this.gCost = 0;
    }

   }
   public void setHCost(float distance){
     this.hCost = distance;
   }
   public void setFCost(){
     this.fCost = this.gCost + this.hCost;
   }

   public void addNeighbour(Point v1) {
      //if the neighbours doesnt already contain a node at the same point:
      boolean contains = false;
      for (Point v : this.neighbours) {
         if (v.sameLocationAs(v1)) {
            contains = true;
         }
      }
      if (!contains) {
         if(isBlocker(v1)){
            this.numObstacleNeighbours++;
         }
         this.neighbours.add(v1);
      }
   }

   public void printPath(){
    System.out.println("Me");
    System.out.println(this.toString());
    if(this.parentPoint != null){
      System.out.println("My parent");
      this.parentPoint.printPath();
    }
   }

   // Debugging
   public String toString() {
      // turn list into string
      String s = "";
      // for(Point p : this.neighbours) {
      //     s += " (" + p.getX() + "," + p.getY() + ") ";
      // }
      return this.getValue() + " (" + this.getX() + "," + this.getY() + ")"; //+ " - neighbours: " + s;
    }


    //used for creating copy of a point
    public Point(Point another) {
       this.value = another.getValue();
       this.x = another.getX();
       this.y = another.getY();
   }

   public int numObstacleNeighbours(){
      return this.numObstacleNeighbours;
   }


    // Equality
   @Override
   public boolean equals(Object other){
      if (!(other instanceof Point)) {
           return false;
      }else{
         Point otherClass = (Point)other;
         return (otherClass.getValue() == this.value && this.x == otherClass.getX() && this.y == otherClass.getY());
      }
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

   public boolean sameLocationAs(Point other) {
      return (this.x  == other.getX() && this.y == other.getY());
   }


   private LinkedList<Point> neighbours;
   private char value;
   private Integer x;
   private Integer y;
   private int numObstacleNeighbours;
}
