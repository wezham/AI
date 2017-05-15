
import java.util.*;
import java.io.*;

public class State {

   public State (char view[][], char action, State parentState) {
      this.view = view;
      this.action = action;
      this.parentState = parentState;
      this.childrenStates = new LinkedList<State>();
      // this.flags = new HashMap<String, Boolean>();
   }

   public char getAction() {
      return this.action;
   }

   public LinkedList<State> generateChildren() {
      char[] possibleMoves = {'F','L','R','C','B'};
      for (char move : possibleMoves) {
         this.childrenStates.add(new State(view, move, this));
      }
      pruneChildren();
      return this.childrenStates;
   }

   private void pruneChildren() {
      LinkedList<State> dontViolate = new LinkedList<State>();
      for (State s : this.childrenStates) {
         if (!s.voilatesConstraint()) {
            System.out.println(s.action + " doesnt violate constraint, adding");
            dontViolate.add(s);
         }
      }
      this.childrenStates = dontViolate;
   }

   public Integer evalHeuristic() {
      return 0;
   }

   public Boolean voilatesConstraint() {
      Boolean voilatesConstraint = false;
      switch (this.action) {
         // if we move
         case 'F':
            System.out.println("Trying to move forward " + this.action);
            switch (this.view[2][2]) {
               case 'v': voilatesConstraint = unstandable(this.view[2][1]);break;
               case '^': voilatesConstraint = unstandable(this.view[2][3]);break;
               case '>': voilatesConstraint = unstandable(this.view[3][2]);break;
               default: voilatesConstraint = unstandable(this.view[1][2]);
            }
         break;
         //if we try to use a tool, voilates constraint
         case 'C': case 'B':
            System.out.println("Trying to perform action " + this.action);
            voilatesConstraint = true;
            break;
         default:
            voilatesConstraint = false;
            break;
      }
      return voilatesConstraint;
   }

   //returns true if a player cant stand on the supplied space.
   //ie if there is a tree, water etc
   private Boolean unstandable(char valueAtLocation) {
      switch (valueAtLocation) {
         case 'T': case '*': case '~': case '-':
            System.out.println("Unstandable " + valueAtLocation);
            return true;
         default:
            System.out.println("Standable " + valueAtLocation);
            return false;
      }
   }

   public String toString() {
       return ("State: " + this.action);
    }

   // private HashMap<String, Boolean> flags;
   private LinkedList<State> childrenStates;
   private char action;
   private State parentState;
   private char[][] view;
}


//
// private LinkedList<Point> searchForImportant(char view[][]) {
//    LinkedList<Point> importantThings = new LinkedList<Point>();
//    int x,y = 0;
//    for (x = 0; x < 5 ; x++) {
//       for (y = 0; y < 5 ; y++) {
//          switch( view[x][y] ) { // if character is a valid action, return it
//          case 'k': case 'd': case '*': case '$': case 'a': case 'T':
//          case '-': case '~':
//             importantThings.add(new Point(x, y, view[x][y]));
//          }
//       }
//    }
//    return importantThings;
// }
//
// private Point searchFor(char view[][], char desired) {
//    int x,y = 0;
//    for (x = 0; x < 5 ; x++) {
//       for (y = 0; y < 5 ; y++) {
//          if (view[x][y] == desired) {new Point(x, y, desired);}
//       }
//    }
//    return null;
// }
