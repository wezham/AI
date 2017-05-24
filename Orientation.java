
import java.util.*;
import java.io.*;

public class Orientation {

  //gets given ^, >, < , v
  public Orientation() {
    this.orientation = 'N';
  }

  public void updateOrientation(char move) {
    switch (move) {
       case 'L':
         switch (this.orientation) {
            case 'N':
            System.out.println("Changed orientation from N to W");
               this.orientation = 'W';break;
            case 'W':
            System.out.println("Changed orientation from W to S");
               this.orientation = 'S';break;
            case 'S':
                System.out.println("Changed orientation from S to E");
               this.orientation = 'E';break;
            default:
            System.out.println("Changed orientation from E to N");
               this.orientation = 'N';
         }break;
       case 'R':
         switch (move) {
            case 'N':
            System.out.println("Changed orientation from N to E");
               this.orientation = 'E';break;
            case 'W':
            System.out.println("Changed orientation from W to N");
               this.orientation = 'N';break;
            case 'S':
            System.out.println("Changed orientation from S to W");
               this.orientation = 'W';break;
            default:
            System.out.println("Changed orientation from E to S");
               this.orientation = 'S';
         }break;
    }
  }

  //given a view and an orientation, realigns the orientation of the view as would be seen
  //if the player was facing north
  public char[][] orientToNorth(char[][] oldView) {
    print_view(oldView);
    int x,y;

    char[][] newView = new char[5][5];

    switch (this.orientation) {
       case 'S':
          System.out.println("facing South changing: ");
          newView = rotateMapRight(oldView, 2);
          break;
       case 'W':
          System.out.println("facing West changing: ");

          newView = rotateMapRight(oldView, 3);
          break;
       case 'E':
          System.out.println("facing EAST changing: ");
          newView = rotateMapRight(oldView, 1);
          break;
       default:
          return oldView;
    }
    print_view(newView);
    return newView;
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
