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

   private LinkedList<String> history;
   private HashMap<String, Integer> toolkit = new HashMap<String, Integer>();
   private Point playerPos = new Point(2,2,'v');

   //A path to a point is good if non of the constraints are violated



   public char get_action( char view[][] ) {

      // REPLACE THIS CODE WITH AI TO CHOOSE ACTION
      LinkedList<Point> important = searchForImportant(view);

      for (Point p : important) {
         System.out.println(p.toString());
      }

      int ch=0;

      System.out.print("Enter Action(s): ");

      try {
         while ( ch != -1 ) {
            // read character from keyboard
            ch  = System.in.read();

            switch( ch ) { // if character is a valid action, return it
            case 'F': case 'L': case 'R': case 'C': case 'U': case 'B':
            case 'f': case 'l': case 'r': case 'c': case 'u': case 'b':
               return((char) ch );
            }
         }
      }
      catch (IOException e) {
         System.out.println ("IO error:" + e );
      }

      return 0;
   }

   private Queue<Point> getAdjPoint(char view[][], Point pChek){
      Queue<Point> adjPoints = new LinkedList<Point>();
      if(pChek.getX() != 0){
         adjPoints.add(new Point(pChek.getX()-1, pChek.getY(), view[pChek.getX()][pChek.getY()]));
      }
      if(pChek.getX() != 4){
         adjPoints.add(new Point(pChek.getX()+1, pChek.getY(), view[pChek.getX()][pChek.getY()]));
      }
      if(pChek.getX() != 0){
         adjPoints.add(new Point(pChek.getX(), pChek.getY() -1, view[pChek.getX()][pChek.getY()]));
      }
      if(pChek.getX() != 4){
         adjPoints.add(new Point(pChek.getX(), pChek.getY() +1, view[pChek.getX()][pChek.getY()]));
      }
      return adjPoints;
   }

   private LinkedList<Point> findPath(char view[][], Point startingPos, Point goalPos) {
      LinkedList<Point> path = new LinkedList<Point>();
      Queue<Point> unseenPoints =  new LinkedList<Point>();

      unseenPoints.add(startingPos);
      while(unseenPoints.peek() != null){
         Point current = unseenPoints.remove();
         if(current.equals(goalPos)){
            path.add(current);
            return path;
         }else{
            Queue<Point> adjP = getAdjPoint(view, current);
            for(Point p : adjP){
               if(!unseenPoints.contains(p)){
                  unseenPoints.add(p);
               }
            }
         }
      }
      return path;
   }

   private LinkedList<Point> searchForImportant(char view[][]) {
      LinkedList<Point> importantThings = new LinkedList<Point>();
      int x,y = 0;
      for (x = 0; x < 5 ; x++) {
         for (y = 0; y < 5 ; y++) {
            switch( view[x][y] ) { // if character is a valid action, return it
            case 'k': case 'd': case '*': case '$': case 'a': case 'T':
            case '-': case '~':
               importantThings.add(new Point(x, y, view[x][y]));
            }
         }
      }
      return importantThings;
   }

   private Point searchFor(char view[][], char desired) {
      int x,y = 0;
      for (x = 0; x < 5 ; x++) {
         for (y = 0; y < 5 ; y++) {
            if (view[x][y] == desired) {new Point(x, y, desired);}
         }
      }
      return null;
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
