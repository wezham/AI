import java.util.*;
import java.io.*;

public class PathPlanner {

  public PathPlanner() {
  }

  public LinkedList<Character> generatePath(LinkedList<Point> path, Orientation o) {
    Orientation variableO = new Orientation(o);
    Point currentPoint = path.pollFirst();
    Point nextPoint = path.pollFirst();
    LinkedList<Character> moves = new LinkedList<Character>();

    while( nextPoint != null) {
      //keep turning till we face the goal
      while(!variableO.facing(currentPoint, nextPoint)) {
        variableO.updateOrientation('R', null);
        moves.add('R');
      }
      //move forward to land at goal
      moves.add('F');
      //update pointers and continue
      currentPoint = nextPoint;
      nextPoint = path.pollFirst();
    }

    return moves;
  }

}
