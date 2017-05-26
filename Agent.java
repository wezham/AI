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

   private Integer counter = 0;
   //represents the absolute orientation of the agent (NSEW)
   private Orientation orientation;
   private Map map;
   private Point currentVertex;

   public char get_action( char view[][] ) {
     char move = 'L';

     //FIRST THING IS UPDATE THE MAP

     if (counter == 0) {
        //if it is our first move
        orientation = new Orientation();
        map = new Map(view);
        map.print();
        currentVertex = map.findVertexByCoordinates(2,2);
        move = 'L';
      } else {
        map.update(view, currentVertex, orientation);
        map.print();
      }

      counter ++;

      //update orientation once weve decided our move
      orientation.updateOrientation(move);

      //update the vertex to the new point
      currentVertex = newVertexCalc(view, move);
      System.out.println("Making Move: " + String.valueOf(move));
      System.out.println("New center: " + currentVertex);

      return 'F';
   }



   //only called if not the first action, meaning that currentState.point != null
   //given a move to make, caluclates the absolute center position of the new state
   private Point newVertexCalc(char[][] view, char action) {
      //if we arent moving, the center remains the same
      Point newVertex = currentVertex;
      int x = currentVertex.getX();
      int y = currentVertex.getY();
      char absOrientation = orientation.getOrientation();

      if (action == 'F') {
         //adjusts the center position for
         switch (absOrientation) {
            case 'S':
               y++; break;
            case 'N':
               y--; break;
            case 'E':
               x++; break;
            default:
               x--;
         }
      }


      return map.findVertexByCoordinates(x, y);
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
