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

   //A path to a point is good if non of the constraints are violated

   private Integer counter = 0;
   private char lastAction;

   private Orientation absoluteOrientation;
   private LinkedList<Character> moves = new LinkedList<Character>();

   private void get_actions(char view[][]){
     PathPlanner pathPlanner = new PathPlanner();
     Point a = new Point(2, 2, '^');
     Point b = new Point(4, 4, view[4][4]);
     //should be map
     Search s = new Search(view);
     // Given the map = run method that generates aStar searches two boundary points
     LinkedList<Point> path = s.aStar(a, b);
   }


   public char get_action( char view[][] ) {
     return 'F';
   }

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

/*
//represents the absolute orientation of the agent (NSEW)
//  private Orientation orientation;
//  private Map map;
//
//
//  public char get_action( char view[][] ) {
//
//  if (counter < 20) {
//     //if it is our first move
//     if (currentState == null) {
//        currentState = new State(view, new Point(0, 0, view[2][2]));
//        orientation = new Orientation();
//     }
//     else {
//        //create a new state based and caluclate new position
//        State newState = new State(view, absolutePointCalculator(lastAction));
//        //replace state and update child parent relationship btw old and new
//        currentState.setChildState(newState);
//        newState.setParentState(currentState);
//        currentState = newState;
//     }
//       //determine best move
//       Move m = this.currentState.findBestMove();
//       lastAction = m.getAction();
//       orientation.updateOrientation(lastAction);
//
//       if (counter == 0 ){
//         map = new Map(view);
//         map.print();
//         System.out.println(map);
//      } else if (counter == 4) {
//         map.update(view, currentState.getAbsolutePoint(), orientation);
//        //  map.print();
//      }
//
//      System.out.println("Move Made: " + m);
//      System.out.println("Absolute Center Before Move: " + currentState.getAbsolutePoint());
//      System.out.println("----------");
//
//       counter ++;
//
//       return lastAction;
//    }
//
//
*/
