
import java.util.*;
import java.io.*;

public class Orientation {

  //gets given ^, >, < , v
  public Orientation(char c) {
    this.orientation = 'N';
  }

  public void updateOrientation(char move) {
    switch (move) {
       case 'L':
         switch (this.orientation) {
            case 'N':
               this.orientation = 'W';break;
            case 'W':
               this.orientation = 'S';break;
            case 'S':
               this.orientation = 'E';break;
            default:
               this.orientation = 'N';
         }break;
       case 'R':
         switch (move) {
            case 'N':
               this.orientation = 'E';break;
            case 'W':
               this.orientation = 'N';break;
            case 'S':
               this.orientation = 'W';break;
            default:
               this.orientation = 'S';
         }break;
    }
  }

  //given a view and an orientation, realigns the orientation of the view as would be seen
  //if the player was facing north
  public char[][] orientToNorth(char[][] oldView) {
    int oldx,oldy,newx,newy;
    char[][] newView = oldView;
    switch (this.orientation) {
       case 'S':
         for( oldy = 0, newy = 5; oldy < 5; oldy++, newy--) {
           for( oldx = 0, newx=5; oldx < 5; oldx++, newx--) {
              newView[newx][newy] = oldView[oldx][oldy];
            }
         }
          break;
       case 'W':
         for( oldy = 0, newx = 5; oldy < 5; oldy++, newx--) {
           for( oldx = 0, newy = 0; oldx < 5; oldx++, newy++) {
              newView[newx][newy] = oldView[oldx][oldy];
            }
         }
          break;
       case 'E':
         for( oldy = 0, newx = 0; oldy < 5; oldy++, newx++) {
           for( oldx = 0, newy = 5; oldx < 5; oldx++, newy--) {
              newView[newx][newy] = oldView[oldx][oldy];
            }
         }
          break;
       default:
        break;
       //do nothing if facing north
    }
    return newView;
  }

  public char getOrientation() {
    return this.orientation;
  }

  public String toString() {
    return String.valueOf(this.orientation);
  }

  private char orientation;

}
