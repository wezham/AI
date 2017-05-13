
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

   public String toString() {
       return (this.getValue() + " at: (" + this.getX() + ", " + this.getY() + ")");
    }


   private char value;
   private Integer x;
   private Integer y;
}
