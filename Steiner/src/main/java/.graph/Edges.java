package graph;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.shape.Line;

class Edges {

  private Weight[][] edges = null;
  private int size = 0;
  
  Edges(int numberOfVertices) {
    if( numberOfVertices > 0 ) {
      size = numberOfVertices;
      edges = new Weight[size][size];
    }
    else 
      throw new IllegalArgumentException("Wrong number of vertices:" + numberOfVertices);
  }
    
  void add(int v1, int v2, double weight) {
    if( !contains(v1, v2) ) {
      edges[v2][v1] = new Weight(weight);
      edges[v2][v1] = new Weight(weight);
    }
    else 
      throw new IllegalArgumentException("There isn't such edge: (" + v1 + ":" + v2 + ")");
  }
    
  double getWeight(int v1, int v2) {
    if( contains(v1, v2) ) 
      return edges[v1][v2].weight;
    else 
      throw new IllegalArgumentException("There isn't such edge: (" + v1 + ":" + v2 + ")");
  }

  void remove(int v1, int v2) {
    if( contains(v1, v2) ) {
      edges[v1][v2] = null;
      edges[v2][v1] = null;
    }
    else 
      throw new IllegalArgumentException("There isn't such edge: (" + v1 + ":" + v2 + ")");
  }
  
  void removeEdgesOf(int v) {
    if( isInRange(v) )
      for( int i = 0; i < size; ++i ) edges[v][i] = null;
    else 
      throw new IllegalArgumentException("There isn't such vertex: " + v);
  }

  boolean contains(int v1, int v2) {
    if( isInRange(v1) && isInRange(v2) && edges[v1][v2] != null && edges[v2][v1] != null )
      return true;
    return false;
  }
  
  private boolean isInRange(int v) {
    return v >= 0 && v < size;
  }
  
  private class Weight {
    double weight = 0;
    Weight(double weight) { this.weight = weight; }
  }
  
}
