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
      int counter = 0;
      while(!variableO.facing(currentPoint, nextPoint)) {
        variableO.updateOrientation('R', null);
        counter ++;
      }
      if(counter==3){ moves.add('L'); }
      else if( counter==2 ){ moves.add('R');moves.add('R'); }
      else if( counter==1 ){ moves.add('R'); }

      if (nextPoint.getValue() == '*') {
        moves.add('B');
      }else if(nextPoint.getValue() == 'T') {
        moves.add('C');
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
