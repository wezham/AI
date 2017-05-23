import java.util.*;
import java.io.*;

public class PathPlanner {

  public static final Integer GOAL_TO_LEFT = 1;
  public static final Integer GOAL_TO_RIGHT = 3;
  public static final Integer GOAL_DOWN = 0;
  public static final Integer GOAL_UP = 2;

  final static char UP   = '^';
  final static char DOWN  = 'v';
  final static char LEFT   = '<';
  final static char RIGHT  = '>';

  final static String SFORWARD   = "F";
  final static String SLEFT  = "L";
  final static String SRIGHT   = "R";

  final static char CFORWARD = 'F';
  final static char CLEFT  = 'L';
  final static char CRIGHT = 'R';

  public PathPlanner(){
    this.initHash();
  }


  public char generateMove(Point player, Point goal){
    char currentDirection = player.getValue();
    if(goalIsToLeft(player.getX(), goal.getX())){
      if(currentDirection == DOWN  || currentDirection == RIGHT){
        return CRIGHT;
      }else if(currentDirection == UP){
        return CLEFT;
      }
    }
    else if(goalIsToRight(player.getX(), goal.getX())){
      if(currentDirection == UP  || currentDirection == LEFT){
        return CRIGHT;
      }else if(currentDirection == DOWN){
        return CLEFT;
      }
    }
    else{
      if(goalIsUp(player.getY(), goal.getY())){
        if(currentDirection == LEFT  || currentDirection == DOWN){
          return CRIGHT;
        }else if(currentDirection == RIGHT){
          return CLEFT;
        }
      }else { //goal must be down
        if(currentDirection == RIGHT || currentDirection == UP){
          return CRIGHT;
        }else if(currentDirection == LEFT){
          return CLEFT;
        }
      }
  }
  return CFORWARD;
}

public LinkedList<String> generateMoves(Point player, Point goal){
  LinkedList<String> moves = new LinkedList<String>();
  //two cases
  char currentDirection = player.getValue();
  int minrotations = 100;
  int playDirec = this.directionValue.get(String.valueOf(player.getValue()));

  if(goalIsToLeft(player.getX(), goal.getX())){
     minrotations = dgm(playDirec, GOAL_TO_LEFT, 2)+ dgm(playDirec, GOAL_TO_LEFT, 4)*((dgm(playDirec, GOAL_TO_LEFT, 2)+1)%2);
       if(minrotations == 2){
         moves.add(SRIGHT);
         moves.add(SRIGHT);
       }else if(minrotations == 1 && currentDirection == DOWN){
         moves.add(SRIGHT);
       }else{
         moves.add(SLEFT);
       }
  }
  else if(goalIsToRight(player.getX(), goal.getX())){
     minrotations = dgm(playDirec, GOAL_TO_RIGHT, 2)+ dgm(playDirec, GOAL_TO_RIGHT, 4)*((dgm(playDirec, GOAL_TO_RIGHT, 2)+1)%2);
     if(minrotations == 2){
       moves.add(SLEFT);
       moves.add(SLEFT);
     }else if(minrotations == 1 && currentDirection == UP ){
       moves.add(SRIGHT);
     }else{
       moves.add(SLEFT);
     }
  }
  else{
    if(goalIsUp(player.getY(), goal.getY())){
      minrotations = dgm(playDirec, GOAL_UP, 2)+ dgm(playDirec, GOAL_UP, 4)*((dgm(playDirec, GOAL_UP, 2)+1)%2);
      if(minrotations == 2){
        moves.add(SLEFT);
        moves.add(SLEFT);
      }else if(minrotations == 1 && currentDirection == LEFT){
        moves.add(SRIGHT);
      }else{
        moves.add(SLEFT);
      }
    }else { //goal must be down
      minrotations = dgm(playDirec, GOAL_DOWN, 2)+ dgm(playDirec, GOAL_DOWN, 4)*((dgm(playDirec, GOAL_DOWN, 2)+1)%2);
      if(minrotations == 2){
        moves.add(SLEFT);
        moves.add(SLEFT);
      }else if(minrotations == 1 && currentDirection == RIGHT){
        moves.add(SRIGHT);
      }else{
        moves.add(SLEFT);
      }
    }
  }
  moves.add(SFORWARD);
  return moves;
}
  // Formula for min number of rotations
  private int dgm(int dir, int goal, int modVal){
    return ( ( (dir-goal) % modVal) + modVal )% modVal;
  }

  private void initHash(){
    this.directionValue = new HashMap<String, Integer>();
    this.directionValue.put("v", 0);
    this.directionValue.put("<", 1);
    this.directionValue.put("^", 2);
    this.directionValue.put(">", 3);
  }

  private boolean goalIsUp(int y, int y2){
    if(y - y2 < 0){
      return true;
    }
    return false;
  }

  private boolean goalIsToLeft(int x, int x2){
    if(x - x2 > 0){
      return true;
    }
    return false;
  }
  private boolean goalIsToRight(int x, int x2){
    if(x - x2 < 0){
      return true;
    }
    return false;
  }

  private LinkedList<String> moves;
  private HashMap<String, Integer> directionValue;
  private Point player;
  private Point goal;

}

/*
state of my agent
PathPlanner pathPlanner = new PathPlanner();

if(counter == 0){
  Point a = new Point(2, 2, '^');
  Point b = new Point(4, 4, view[4][4]);
  LinkedList<Point> path = new LinkedList<Point>();
  LinkedList<String> moves = new LinkedList<String>();

  Search s = new Search(view, a, b);
  Point goal = s.aStar();

  while((goal.getParentPoint()) != null){
    path.addFirst(goal);
    goal = goal.getParentPoint();
  }
  ListIterator<Point> it = path.listIterator();
  Point previous = a;
  Point p;
  while(it.hasNext()){
    p = it.next();
    moves = pathPlanner.generateMoves(previous, p);
    for(String ss : moves){
      System.out.println(ss);
    }
    p.setValue('>');
    previous = p;
  }
}

 return 'F';
*/
