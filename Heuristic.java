import java.io.*;
import java.net.*;
import java.util.*;

public class Heuristic {

   public Heuristic(){
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
      case('.'): val = 100000;break;
      default: val = 0;
    }
    return val;
  }
  private float obstacleHeuristic(Point dest, HashMap<Character, Integer> tools){
    float val = 0;
    switch(dest.getValue()){
      case('T'): if (tools.get('a') > 0) { val = 100; }else{val=350;};break;
      case('-'): if (tools.get('k') > 0) { val = 100; }else{val=300;};break;
      case('*'): if (tools.get('d') > 0) { val = 200; tools.put('d', (tools.get('d')-1));}else{val=700;};break;
      case('~'): if (tools.get('r') > 0) {  val=dealWithWater(dest, tools); }else{val=800;};break;
      case('.'): val = 100000;break;
      default: val = 0;
    }
    return val;
  }

  private float dealWithWater(Point dest, HashMap<Character, Integer> tools){
    if(dest.getParentPoint().getValue() == '~' && dest.getValue() != '~'){
        tools.put('r', (tools.get('r')-1));
        return 800;
    }else{
      return 50;
    }
  }
}
