import java.util.*;
import java.io.*;

public class PathPlanner {

  public PathPlanner() {
  }

  public LinkedList<Character> generatePath(LinkedList<Point> path, Orientation o) {
    Orientation variableO = new Orientation(o);
    for (Point p : path) {
      System.out.println(p);
    }

    Point current = path.pollFirst();
    Point next = path.pollFirst();
    LinkedList<Character> moves = new LinkedList<Character>();

    while( next != null) {
      // System.out.println("Moving from " + current + " to " + next);
      //while we arent facing the goal, turn until we are
      while(!variableO.facing(current, next)) {
        variableO.updateOrientation('R', null);
        moves.add('R');
      }
      //move forward to land at goal
      moves.add('F');
      //update pointers and continue
      current = next;
      next = path.pollFirst();
    }

    return moves;
  }

}
