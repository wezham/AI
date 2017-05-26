import java.util.*;
import java.io.*;

public class PathPlanner {

  public static final Integer GOAL_TO_LEFT = 1;
  public static final Integer GOAL_TO_RIGHT = 3;
  public static final Integer GOAL_DOWN = 0;
  public static final Integer GOAL_UP = 2;

  final static char NORTH   = '^';
  final static char SOUTH  = 'v';
  final static char WEST   = '<';
  final static char EAST  = '>';

  final static char FORWARD = 'F';
  final static char LEFT  = 'L';
  final static char RIGHT = 'R';

  public PathPlanner(){
    this.initHash();
  }


  public char generateMove(Point player, Point goal){
    char currentDirection = player.getValue();
    if(goalIsToLeft(player.getX(), goal.getX())){
      if(currentDirection == SOUTH  || currentDirection == EAST){
        return RIGHT;
      }else if(currentDirection == NORTH){
        return LEFT;
      }
    }
    else if(goalIsToRight(player.getX(), goal.getX())){
      if(currentDirection == NORTH  || currentDirection == WEST){
        return RIGHT;
      }else if(currentDirection == SOUTH){
        return LEFT;
      }
    }
    else{
      if(goalIsUp(player.getY(), goal.getY())){
        if(currentDirection == WEST  || currentDirection == SOUTH){
          return RIGHT;
        }else if(currentDirection == EAST){
          return LEFT;
        }
      }else { //goal must be down
        if(currentDirection == EAST || currentDirection == NORTH){
          return RIGHT;
        }else if(currentDirection == WEST){
          return LEFT;
        }
      }
  }
  return FORWARD;
}

public LinkedList<Character> generateMoves(Point player, Point goal, Orientation playerOrientation){
  LinkedList<Character> moves = new LinkedList<Character>();
  //two cases
  char currentDirection = player.getValue();
  int minrotations = 100;
  int playDirec = this.directionValue.get(player.getValue());

  if(goalIsToLeft(player.getX(), goal.getX())){
     minrotations = dgm(playDirec, GOAL_TO_LEFT, 2)+ dgm(playDirec, GOAL_TO_LEFT, 4)*((dgm(playDirec, GOAL_TO_LEFT, 2)+1)%2);
       if(minrotations == 2){
         moves.add(RIGHT);
         moves.add(RIGHT);
       }else if(minrotations == 1 && currentDirection == SOUTH){
         moves.add(RIGHT);
       }else{
         moves.add(LEFT);
       }
  }
  else if(goalIsToRight(player.getX(), goal.getX())){
     minrotations = dgm(playDirec, GOAL_TO_RIGHT, 2)+ dgm(playDirec, GOAL_TO_RIGHT, 4)*((dgm(playDirec, GOAL_TO_RIGHT, 2)+1)%2);
     if(minrotations == 2){
       moves.add(LEFT);
       moves.add(LEFT);
     }else if(minrotations == 1 && currentDirection == NORTH ){
       moves.add(RIGHT);
     }else{
       moves.add(LEFT);
     }
  }
  else{
    if(goalIsUp(player.getY(), goal.getY())){
      minrotations = dgm(playDirec, GOAL_UP, 2)+ dgm(playDirec, GOAL_UP, 4)*((dgm(playDirec, GOAL_UP, 2)+1)%2);
      if(minrotations == 2){
        moves.add(LEFT);
        moves.add(LEFT);
      }else if(minrotations == 1 && currentDirection == WEST){
        moves.add(RIGHT);
      }else{
        moves.add(LEFT);
      }
    }else { //goal must be down
      minrotations = dgm(playDirec, GOAL_DOWN, 2)+ dgm(playDirec, GOAL_DOWN, 4)*((dgm(playDirec, GOAL_DOWN, 2)+1)%2);
      if(minrotations == 2){
        moves.add(LEFT);
        moves.add(LEFT);
      }else if(minrotations == 1 && currentDirection == EAST){
        moves.add(RIGHT);
      }else{
        moves.add(LEFT);
      }
    }
  }
  moves.add(FORWARD);
  return moves;
}
  // Formula for min number of rotations
  private int dgm(int dir, int goal, int modVal){
    return ( ( (dir-goal) % modVal) + modVal )% modVal;
  }

  private void initHash(){
    this.directionValue = new HashMap<Character, Integer>();
    this.directionValue.put('v', 0);
    this.directionValue.put('<', 1);
    this.directionValue.put('^', 2);
    this.directionValue.put('>', 3);
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

  private LinkedList<Character> moves;
  private HashMap<Character, Integer> directionValue;
  private Point player;
  private Point goal;

}

/*
state of my agent
PathPlanner pathPlanner = new PathPlanner();



 return 'F';
*/



/*
   ListIterator<Point> it = path.listIterator();
   Point previous = a;
   Point p;
   while(it.hasNext()){
     p = it.next();
     this.moves.addAll(pathPlanner.generateMoves(previous, p));
     p.setValue('>');
     previous = p;
   }



 // Add to map
 // map.add(orientation, view);

  // Pop next move off list
   if(counter == 0){
     get_actions(view);
     for(char ss : this.moves){
       System.out.println(ss);
     }
     counter++;
     return this.moves.poll();
   }
   return this.moves.poll();
   // If moves are empty, call getActions on map
*/
