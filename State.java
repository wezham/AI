
import java.util.*;
import java.io.*;

public class State {

   public State (char view[][], Point center) {
      this.view = view;
      this.parentState = null;
      this.childState = null;
      this.toolKit = new HashMap<String, Integer>();
      this.possibleMoves = new LinkedList<Move>();
      generatePotentialMoves();
      this.center = center;
   }

   //evaluate position of possible moves
   public LinkedList<Move> generatePotentialMoves() {
      char[] possibleMoves = {'F','L','R','C','B'};
      //prune of the children that voilate constraints
      for (char move : possibleMoves) {
         Move m = new Move(this.view, move);
         //prune of child if voilates constraint
         if (!m.voilatesConstraint()) {
            this.possibleMoves.add(m);
         }
         //java takes care of cleanup on its own
      }
      return this.possibleMoves;
   }

   //sorts possible moves by heuristic and returns best move
   public Move findBestMove() {
      //sort the moves based on their heuristic
      this.possibleMoves.sort((o1, o2) -> o1.getHeuristic().compareTo(o2.getHeuristic()));
      return this.possibleMoves.peek();
   }

   public void setChildState(State s) {
      this.childState = s;
   }
   public void setParentState(State s) {
      this.parentState = s;
   }

   public Point getAbsolutePoint() {
      return this.center;
   }

   private HashMap<String, Integer> toolKit;
   private State childState;
   private LinkedList<Move> possibleMoves;
   private State parentState;
   private char[][] view;
   //this is a point whos coordinates are absolute, not relative
   private Point center;
}
