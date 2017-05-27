
import java.util.*;
import java.io.*;

public class Orientation{

  //gets given ^, >, < , v
  public Orientation() {
    this.orientation = 'N';
  }

  public Orientation(Orientation o) {
    this.orientation = o.getOrientation();
  }

  public void updateOrientation(char move, Point currentVertex) {
    char direction = this.orientation;
    switch (move) {
       case 'L':
         switch (this.orientation) {
            case 'N':
               direction = 'W'; break;
            case 'W':
               direction = 'S';break;
            case 'S':
               direction = 'E';break;
            default:
               direction = 'N';
         }break;
       case 'R':
         switch (this.orientation) {
            case 'N':
               direction = 'E';break;
            case 'W':
               direction = 'N';break;
            case 'S':
               direction = 'W';break;
            default:
               direction = 'S';
         }break;
    }
    this.orientation = direction;
    if(currentVertex != null){
        currentVertex.setValue(this.playerCharacter());
    }
  }

  //given a view and an orientation, realigns the orientation of the view as would be seen
  //if the player was facing north
  public char[][] orientToNorth(char[][] oldView) {
    int x,y;

    char[][] newView = new char[5][5];

    switch (this.orientation) {
       case 'S':
          // System.out.println("facing South changing: ");
          newView = rotateMapRight(oldView, 2);
          break;
       case 'W':
          // System.out.println("facing West changing: ");
          newView = rotateMapRight(oldView, 3);
          break;
       case 'E':
          // System.out.println("facing EAST changing: ");
          newView = rotateMapRight(oldView, 1);
          break;
       default:
          return oldView;
    }
    // print_view(newView);
    return newView;
  }



  public char playerCharacter () {
    switch (this.orientation) {
       case 'S':
         return 'v';
       case 'W':
         return '<';
       case 'E':
         return '>';
       default:
         return '^';
    }
  }

  public int yPositionInFrontOfPlayer(Vertex current) {
    switch (this.orientation) {
       case 'S':
         return current.getY()+1;
       case 'W':
         return current.getY();
       case 'E':
         return current.getY();
       default:
         return current.getY()-1;
    }
  }

  public char getOrientation() {
    return this.orientation;
  }

  public String toString() {
    return String.valueOf(this.orientation);
  }

  private char orientation;


  private char[][] rotateMapRight(char[][] oldView, int numTimes) {
    char[][] newView = copyView(oldView);

    //rotate
    for(int z = 0; z< numTimes; z++) {
      for (int i = 0; i < 5; i++){
          for (int j =0; j< 5; j++){
            newView[i][j] = oldView[5-1-j][i];
          }
       }
      //  print_view(newView);
       oldView = copyView(newView);
     }

     return newView;
  }

  char[][] copyView(char[][] oldView) {
    char[][] newView = new char[5][5];
    //copy
    for(int x = 0; x < 5; x++) {
      for(int y = 0 ; y < 5; y++) {
         newView[x][y] = oldView[x][y];
       }
    }
    return newView;
  }

  //returns
  public boolean facing(Point current, Point next) {
    boolean facing = false;
    // System.out.println("Currently facing " + this.orientation);
    switch (this.orientation) {
       case 'N':
         facing = ((current.getY()-1) == next.getY());
         break;
       case 'E':
         facing = ((current.getX()+1) == next.getX());
         break;
       case 'W':
         facing = ((current.getX()-1) == next.getX());
         break;
       default:
         facing = ((current.getY()+1) == next.getY());
         break;
    }
    // System.out.println("Facing " + facing);
    return facing;
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

}
