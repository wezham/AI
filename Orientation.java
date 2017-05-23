
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

  public char getOrientation() {
    return this.orientation;
  }

  public String toString() {
    return String.valueOf(this.orientation);
  }

  private char orientation;

}
