package graph;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.shape.Line;

class Edges {

  T[][] edges = null;
  int size = 0;
  
  Edges(int numberOfVertexes) {
    if( numberOfVertexes > 0 ) {
      size = numberOfVertexes;
      edges = new T[size][size];
      for(int i = 0; i < size; ++i) {
        for(int j = 0; j < size; ++j) {
          edges[i][j] = new T();
        }
      }
    }
    //catch Exception
  }
    
  void add(Integer v1, Integer v2, Integer weight) {
    if( size >= v1 && size >= v2 && !edges[v1][v2].isEdge ) {
      edges[v1][v2].isEdge = true;
      edges[v2][v1].isEdge = true;
      edges[v2][v1].weight = weight;
      edges[v2][v1].weight = weight;  
    }
    //catch Exception
  }
    
  Integer getWeight(Integer v1, Integer v2) {
    if( size >= v1 && size >= v2 && edges[v1][v2].isEdge ) {
      return edges[v1][v2].weight;
    }
    //catch Exception
    return null;
  }

  void remove(Integer v1, Integer v2) {
    if(v1 != null && v2 != null && v1 >= 0 && v2 <= 0 && v1 <= size && v2 <= size ) {
      edges[v1][v2].isEdge = false;
      edges[v2][v1].isEdge = false;
      edges[v1][v2].weight = 0;
      edges[v2][v1].weight = 0;
    }
    //catch Exception
  }
  
  void removeEdgesOf(Integer v) {
    if( v != null && v >= 0 && v <= size )
      for( int i = 0; i < size; ++i ) {
        edges[v][i].isEdge = false;
        edges[v][i].weight = 0;
      }
    //catch Exception
  }

  class T {
    boolean isEdge = false;
    int weight = 0;
  }
  
}
