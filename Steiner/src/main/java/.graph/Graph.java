package graph;

public class Graph {
  private Vertices vertices = null;
  private Edges edges = null;

  public Graph(int numberOfVertices) {
    vertices = new Vertices(numberOfVertices);
    edges = new Edges(numberOfVertices);
  }

  public void addVertex(int v) {
    vertices.add(v);
  }

  public void removeVertex(int v) {
    vertices.remove(v);
    edges.removeEdgesOf(v);
  }

  public void addEdge(int v1, int v2, double weight) {
    edges.add(v1, v2, weight);
  }

  public void removeEdge(int v1, int v2) {
    edges.remove(v1, v2);
  }

  public double getEdgeWeight(int v1, int v2) {
    return edges.getWeight(v1, v2);
  }
  
}
