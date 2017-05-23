
import java.util.*;
import java.io.*;

public class Point {

   public Point (Integer x, Integer y, char value) {
      this.value = value;
      this.x = x;
      this.y = y;
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
   //A Star
   private Point parentPoint;
   private float gCost;
   private float hCost;
   private float fCost;
   // Set previous point for path recollection
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
     }
   }
   public void setHCost(float distance){
     this.hCost = distance;
   }
   public void setFCost(){
     this.fCost = this.gCost + this.hCost;
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
       return (this.getValue() + " at: (" + this.getX() + ", " + this.getY() + ")");
    }


    //used for creating copy of a point
    public Point(Point another) {
       this.value = another.getValue();
       this.x = another.getX();
       this.y = another.getY();
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


   private char value;
   private Integer x;
   private Integer y;
}
