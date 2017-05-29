import java.io.*;
import java.net.*;
import java.util.*;

public class Heuristic {

   public Heuristic(){
     this.usedTools  =  new HashMap<Character, Integer>();
   }

   public float heuristicEvaluator(boolean obstacles, Point p, HashMap<Character, Integer> toolkit){
     if(!obstacles){
       return noObstacleHeuristic(p);
     }else{
       return obstacleHeuristic(p, toolkit);
     }
   }

  private float noObstacleHeuristic(Point dest){
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
  private float obstacleHeuristic(Point dest, HashMap<Character, Integer> toolkit){
    float val = 0;
    switch(dest.getValue()){
      case('T'): if (this.usedTools.get('a') > 0) { val = 100; }else{val=350;};break;
      case('-'): if (this.usedTools.get('k') > 0) { val = 100; }else{val=300;};break;
      case('*'): if (this.usedTools.get('d') > 0) { val = 200; this.usedTools.put('d', (this.usedTools.get('d')-1));}else{val=700;};break;
      case('~'): if (this.usedTools.get('r') > 0) { val = 400; this.usedTools.put('r', (this.usedTools.get('r')-1));}else{val=800;};break;
      default: val = 0;
    }
    return val;
  }

  public void initHashMap(HashMap<Character, Integer> toolkit){
    this.usedTools.put('d', toolkit.get('d'));
    this.usedTools.put('a', toolkit.get('a'));
    this.usedTools.put('k',toolkit.get('k'));
    this.usedTools.put('r',toolkit.get('r'));
  }

  private HashMap<Character, Integer> usedTools;
}
