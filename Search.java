import java.util.*;
import java.io.*;
public class Search {

  public Search(char view[][], Point a, Point b){
    this.view = view;
    this.a = a;
    this.b = b;
    this.openList = new PriorityQueue<Point>(11, this.pointComparator);
    this.closedList = new LinkedList<Point>();
  }

  public void aStar(){

  }


  public float straightLineDistance(Point a, Point b){
    double x = Math.pow((a.getX()-b.getX()), 2);
    double y = Math.pow((a.getY()-b.getY()), 2);
    float d = (float) Math.sqrt(x + y);
    return d;
  }

  private Comparator<Point> pointComparator = new Comparator<Point>(){
      @Override
      public int compare(Point o1, Point o2){
          return o1.getFCost() - o2.getFCost();
      }
  };

  private Point a;
  private Point b;
  private char view[][];
  private PriorityQueue<Point> openList;
  private LinkedList<Point> closedList;
}
