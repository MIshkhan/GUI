package graph;

public class Graph {
  private Vertexes vertexes = null;
  private Edges edges = null;

  public Graph(int numberOfVertexes) {
    if(numberOfVertexes > 0) {
      vertexes = new Vertexes(numberOfVertexes);
      edges = new Edges(numberOfVertexes);
    }
    //catch Exception
  }

  public void addVertex(Integer v) {
    vertexes.add(v);
    //catch Exception
  }

  public void removeVertex(Integer v) {
    vertexes.remove(v);
    edges.removeEdgesOf(v);
    //catch Exception
  }

  public void addEdge(Integer v1, Integer v2, Integer weight) {
    edges.add(v1, v2, weight);
    //catch Exception
  }

  public void removeEdge(Integer v1, Integer v2) {
    edges.remove(v1, v2);
    //catch Exception
  }

  public Integer getEdgeWeight(Integer v1, Integer v2) {
    return edges.getWeight(v1, v2);
    //catch Exception
  }
  
}
