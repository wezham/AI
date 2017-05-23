
import java.util.*;
import java.io.*;

public class Edge {

   public Edge (Vertex a, Vertex b) {
      this.a = a;
      this.b = b;
   }

   public Vertex getV1() {
     return this.a;
   }

   public Vertex getV2() {
     return this.b;
   }

  //  Equality
  @Override
  public boolean equals(Object other){
     if (!(other instanceof Edge)) {
          return false;
     }else{
        Edge otherClass = (Edge)other;
        return ((this.a == otherClass.getV1() && this.b == otherClass.getV2()) || (this.b == otherClass.getV1() && this.a == otherClass.getV2()));
     }
  }

   private Vertex a;
   private Vertex b;
}
