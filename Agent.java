/*********************************************
 *  Agent.java
 *  Sample Agent for Text-Based Adventure Game
 *  COMP3411 Artificial Intelligence
 *  UNSW Session 1, 2017
*/

import java.util.*;
import java.io.*;
import java.net.*;

public class Agent {

   // private LinkedList<String> history;
   // private HashMap<String, Integer> toolkit = new HashMap<String, Integer>();

   //A path to a point is good if non of the constraints are violated
   private State currentState;
   private Integer counter = 0;

   public char get_action( char view[][] ) {

      if (counter < 20) {
         this.currentState = new State(view, ' ', null);
         LinkedList<State> optionalMoves = this.currentState.generateChildren();
         System.out.println("Move Made: " + optionalMoves.peek());
         System.out.println("----------");
         counter ++;
         return optionalMoves.peek().getAction();
      }
      return 'F';
   }





   // private Queue<Point> getAdjPoint(char view[][], Point pChek){
   //    Queue<Point> adjPoints = new LinkedList<Point>();
   //    if(pChek.getX() != 0){
   //       adjPoints.add(new Point(pChek.getX()-1, pChek.getY(), view[pChek.getX()][pChek.getY()]));
   //    }
   //    if(pChek.getX() != 4){
   //       adjPoints.add(new Point(pChek.getX()+1, pChek.getY(), view[pChek.getX()][pChek.getY()]));
   //    }
   //    if(pChek.getX() != 0){
   //       adjPoints.add(new Point(pChek.getX(), pChek.getY() -1, view[pChek.getX()][pChek.getY()]));
   //    }
   //    if(pChek.getX() != 4){
   //       adjPoints.add(new Point(pChek.getX(), pChek.getY() +1, view[pChek.getX()][pChek.getY()]));
   //    }
   //    return adjPoints;
   // }

   // private LinkedList<Point> findPath(char view[][], Point startingPos, Point goalPos) {
   //    LinkedList<Point> path = new LinkedList<Point>();
   //    Queue<Point> unseenPoints =  new LinkedList<Point>();
   //
   //    unseenPoints.add(startingPos);
   //    while(unseenPoints.peek() != null){
   //       Point current = unseenPoints.remove();
   //       if(current.equals(goalPos)){
   //          path.add(current);
   //          return path;
   //       }else{
   //          Queue<Point> adjP = getAdjPoint(view, current);
   //          for(Point p : adjP){
   //             if(!unseenPoints.contains(p)){
   //                unseenPoints.add(p);
   //             }
   //          }
   //       }
   //    }
   //    return path;
   // }











   void print_view( char view[][] )
   {
      int i,j;

      System.out.println("\n+-----+");
      for( i=0; i < 5; i++ ) {
         System.out.print("|");
         for( j=0; j < 5; j++ ) {
            if(( i == 2 )&&( j == 2 )) {
               System.out.print('^');
            }
            else {
               System.out.print( view[i][j] );
            }
         }
         System.out.println("|");
      }
      System.out.println("+-----+");
   }

   public static void main( String[] args )
   {
      InputStream in  = null;
      OutputStream out= null;
      Socket socket   = null;
      Agent  agent    = new Agent();
      char   view[][] = new char[5][5];
      char   action   = 'F';
      int port;
      int ch;
      int i,j;

      if( args.length < 2 ) {
         System.out.println("Usage: java Agent -p <port>\n");
         System.exit(-1);
      }

      port = Integer.parseInt( args[1] );

      try { // open socket to Game Engine
         socket = new Socket( "localhost", port );
         in  = socket.getInputStream();
         out = socket.getOutputStream();
      }
      catch( IOException e ) {
         System.out.println("Could not bind to port: "+port);
         System.exit(-1);
      }

      try { // scan 5-by-5 wintow around current location
         while( true ) {
            for( i=0; i < 5; i++ ) {
               for( j=0; j < 5; j++ ) {
                  if( !(( i == 2 )&&( j == 2 ))) {
                     ch = in.read();
                     if( ch == -1 ) {
                        System.exit(-1);
                     }
                     view[i][j] = (char) ch;
                  }
               }
            }
            agent.print_view( view ); // COMMENT THIS OUT BEFORE SUBMISSION
            action = agent.get_action( view );
            out.write( action );
         }
      }
      catch( IOException e ) {
         System.out.println("Lost connection to port: "+ port );
         System.exit(-1);
      }
      finally {
         try {
            socket.close();
         }
         catch( IOException e ) {}
      }
   }
}
