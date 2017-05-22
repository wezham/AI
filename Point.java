
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
   private Integer gCost;
   private Integer hCost;
   private Integer fCost;
   // Set previous point for path recollection
   public void setPrevious(Point pp){
     this.parentPoint = pp;
   }
   public Integer getGCost(){
     return this.gCost;
   }
   public Integer getHCost(){
     return this.hCost;
   }
   public Integer getFCost(){
     return this.fCost;
   }
   public void setGCost(Integer distance){
     this.gCost = parentPoint.getGCost() + distance;
   }
   public void setHCost(Integer distance){
     this.hCost = distance;
   }
   public void setFCost(){
     this.fCost = this.gCost + this.hCost;
   }

   // Debugging
   public String toString() {
       return (this.getValue() + " at: (" + this.getX() + ", " + this.getY() + ")");
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
