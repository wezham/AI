import java.util.*;
import java.io.*;
public class Search {

  public Search(char view[][], Point a, Point b){
    this.view = view;
    this.a = a;
    this.b = b;
  }

  public Point aStar(){
    PriorityQueue<Point> openList = new PriorityQueue<Point>(11, this.pointComparator);
    LinkedList<Point> openListCheck = new LinkedList<Point>();
    LinkedList<Point> closedList = new LinkedList<Point>();

    // Initalise starting node
    this.a.setGCost(0);
    this.a.setHCost(straightLineDistance(this.a, this.b));
    this.a.setFCost();

    openList.add(this.a);
    openListCheck.add(this.a);
    while(openList.size() != 0){
      Point curr = openList.poll();
      Queue<Point> adjNodes = this.getAdjPoint(view, curr);
      for(Point aj : adjNodes){
        aj.setPrevious(curr);
        // If goal return current point
        if(aj.equals(this.b)){
          return aj;
        }
        aj.setGCost(1);
        aj.setHCost(straightLineDistance(aj, this.b));
        aj.setFCost();
        int x = closedList.indexOf(aj);
        int y = openListCheck.indexOf(aj);
        if(y > 0){
          if(openListCheck.get(y).getFCost() < aj.getFCost()){
            continue;
          }
        }
        else if(x > 0){
          if(closedList.get(x).getFCost() < aj.getFCost()){
            continue;
          }
        }else{
          openList.add(aj);
          openListCheck.add(aj);
        }
      }
      closedList.add(curr);
    }
    return this.b;
  }

  // For calculating hCost subject to change I guess
  public float straightLineDistance(Point a, Point b){
    double x = Math.pow((a.getX()-b.getX()), 2);
    double y = Math.pow((a.getY()-b.getY()), 2);
    float d = (float) Math.sqrt(x + y);
    return d;
  }

  // Get adj points
  private Queue<Point> getAdjPoint(char view[][], Point cur){
     Queue<Point> adjPoints = new LinkedList<Point>();
     if(cur.getX() != 0){
        adjPoints.add(new Point(cur.getX()-1, cur.getY(), view[cur.getX()][cur.getY()]));
     }
     if(cur.getX() != 4){
        adjPoints.add(new Point(cur.getX()+1, cur.getY(), view[cur.getX()][cur.getY()]));
     }
     if(cur.getY() != 0){
        adjPoints.add(new Point(cur.getX(), cur.getY() -1, view[cur.getX()][cur.getY()]));
     }
     if(cur.getY() != 4){
        adjPoints.add(new Point(cur.getX(), cur.getY() +1, view[cur.getX()][cur.getY()]));
     }
     return adjPoints;
  }

  private Comparator<Point> pointComparator = new Comparator<Point>(){
      @Override
      public int compare(Point o1, Point o2){
          return (int)(o1.getFCost() - o2.getFCost());
      }
  };

  private Point a;
  private Point b;
  private char view[][];
}
