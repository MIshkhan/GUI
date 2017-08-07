package graph;

class Vertices {

  private int size = 0;
  private int[] vertices = null;
  
  Vertices(int size) {
    if( size > 0 ) {
      this.size = size;
      vertices = new int[size];
    }
    else
      throw new IllegalArgumentException("Wrong number of vertices:" + size);
  }

  void add(int v) {
    if( contains(v) )
      throw new IllegalArgumentException("Vertex already exists:" + v);      

    if( isInRange(v) ) 
       vertices[v] = 1;
    else
      throw new IllegalArgumentException("Wrong vertex index:" + v);
  }

  void remove(int v) {
    if( !contains(v) )
      throw new IllegalArgumentException("There isn't such vertex:" + v);

    if( isInRange(v) ) 
      vertices[v] = 0;
    else
      throw new IllegalArgumentException("Wrong vertex index:" + v);
  }
  
  boolean contains(int v) {
    if( isInRange(v) && vertices[v] == 1 ) {
      return true;
    }
    return false;
  }

  int[] getVertices() {
    return this.vertices;
  }

  private boolean isInRange(int v) {
    return v >= 0 && v < size;
  }
}
