
import java.util.*;
import java.io.*;

public class Point {

   public Point (Integer x, Integer y, char value) {
      this.value = value;
      this.x = x;
      this.y = y;
   }

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

   public String toString() {
       return (this.getValue() + " at: (" + this.getX() + ", " + this.getY() + ")");
    }

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
