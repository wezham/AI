import java.util.*;
import java.io.*;
public class Search {

  public Search(Map map){
    this.map = map;
  }

  public LinkedList<Point> aStar(Point a, Point b, Orientation o){
    this.map.clearParentPoints();
    // System.out.println("Finding Path from " + a + " to " + b);
    PriorityQueue<Point> openList = new PriorityQueue<Point>(11, this.pointComparator);
    LinkedList<Point> openListCheck = new LinkedList<Point>();
    LinkedList<Point> closedList = new LinkedList<Point>();

    // Initalise starting node
    a.setGCost(0);
    a.setHCost(straightLineDistance(a, b));
    a.setFCost();

    openList.add(a);
    openListCheck.add(a);

    while(openList.size() != 0){
      Point curr = openList.poll();
      Queue<Point> adjNodes = curr.neighbours();
      for(Point aj : adjNodes){
        if(aj.equals(b)){
          aj.setPrevious(curr);
          return generatePath(aj, o);
        }
        int x = closedList.indexOf(aj);
        int y = openListCheck.indexOf(aj);
        if((y >= 0 ) && (openListCheck.get(y).getGCost() < (curr.getGCost()+1) ) ){
          continue;
        }
        else if((x >= 0) && (closedList.get(x).getGCost() < (curr.getGCost()+1) ) ){
          continue;
        }else{
          aj.setPrevious(curr);
          aj.setGCost(1);
          aj.setHCost(straightLineDistance(aj, b) + obstacle(aj));
          aj.setFCost();
          openList.add(aj);
          openListCheck.add(aj);
        }
      }
      closedList.add(curr);
    }
    return generatePath(b, o);
  }

  private LinkedList<Point> generatePath(Point b, Orientation o){
    Point goal = b;
    LinkedList<Point> path = new LinkedList<Point>();
    while((goal.getParentPoint()) != null){
      path.addFirst(goal);
      goal = goal.getParentPoint();
    }
    path.addFirst(goal);
    // System.out.println("Astar Point Path:");
    // for(Point p : path){
    //   System.out.println(p.toString());
    // }
    return path;
  }

  // For calculating hCost subject to change I guess
  private float straightLineDistance(Point a, Point b){
    double x = Math.pow((a.getX()-b.getX()), 2);
    double y = Math.pow((a.getY()-b.getY()), 2);
    float d = (float) Math.sqrt(x + y);
    return d;
  }
  private float obstacle(Point dest){
    float val = 0;
    switch(dest.getValue()){
      case('T'): val = 1000 ;break;
      case('-'): val = 1000 ;break;
      case('*'): val = 1000 ;break;
      case('~'): val = 1000 ;break;
      default: val = 0;
    }
    return val;
  }

  private Comparator<Point> pointComparator = new Comparator<Point>(){
      @Override
      public int compare(Point o1, Point o2){
          return (int)(o1.getFCost() - o2.getFCost());
      }
  };

  private Point a;
  private Point b;
  private Map map;
}
