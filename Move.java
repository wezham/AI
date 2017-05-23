
import java.util.*;
import java.io.*;

public class Move{

   public Move (char[][] view, char action) {
      this.action = action;
      this.heuristic = 0;
      this.view = view;
   }

   public Boolean voilatesConstraint() {
      Boolean voilatesConstraint = false;
      switch (this.action) {
         // if we move
         case 'F':
            // System.out.println("Trying to move forward " + this.action);
            switch (this.view[2][2]) {
               case 'v': voilatesConstraint = unstandable(this.view[2][1]);break;
               case '^': voilatesConstraint = unstandable(this.view[2][3]);break;
               case '>': voilatesConstraint = unstandable(this.view[3][2]);break;
               default: voilatesConstraint = unstandable(this.view[1][2]);
            }
         break;
         //if we try to use a tool, voilates constraint (this will change)
         case 'C': case 'B':
            // System.out.println("Trying to perform action " + this.action);
            voilatesConstraint = true;
            break;
         default:
            voilatesConstraint = false;
            break;
      }
      return voilatesConstraint;
   }

   //returns true if a player cant stand on the supplied space.
   //ie if there is a tree, water ,wall etc
   private Boolean unstandable(char valueAtLocation) {
      switch (valueAtLocation) {
         case 'T': case '*': case '~': case '-':
            // System.out.println("Unstandable " + valueAtLocation);
            return true;
         default:
            // System.out.println( "Standable " + valueAtLocation);
            return false;
      }
   }

   public char getAction() {
      return this.action;
   }

   public String toString() {
      return String.valueOf(this.action);
   }

   public Integer getHeuristic() {
      return this.heuristic;
   }

   private char[][] view;
   private Integer heuristic;
   private char action;
}
