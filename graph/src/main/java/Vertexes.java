package graph;

import java.util.ArrayList;

class Vertexes {

  int size = 0;
  int[] vertexes = null;
  
  Vertexes(int size) {
    if( size > 0 ) {
      this.size = size;
      vertexes = new int[size];
    }
  }

  boolean add(Integer v) {
    if( v >= 0 && !contains(v) ) {
      vertexes[v] = 1;
      return true;
    }
    return false;
  }

  boolean remove(Integer v) {
    if( v >= 0 && contains(v) ) {
      vertexes[v] = 0;
      return true;
    }
    return false;
  }
  
  boolean contains(Integer v) {
    if( v >= 0 && size >= v && vertexes[v] == 1 ) {
      return true;
    }
    return false;
  }
  
}
