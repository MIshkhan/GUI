package org.jgrapht.demo;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;

import org.jgraph.*;
import org.jgraph.graph.*;

import org.jgrapht.*;
import org.jgrapht.ext.*;
import org.jgrapht.graph.*;
// resolve ambiguity
import org.jgrapht.graph.DefaultEdge;

public class JGraphAdapterDemo extends JApplet {
  private static final long serialVersionUID = 3256444702936019250L;
  private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");
  private static final Dimension DEFAULT_SIZE = new Dimension(530, 320);

  //
  private JGraphModelAdapter<String, DefaultEdge> jgAdapter;

  /**
   * An alternative starting point for this demo, to also allow running this applet as an
   * application.
   *
   * @param args ignored.
   */
  public static void main(String[] args) {
    JGraphAdapterDemo applet = new JGraphAdapterDemo();
    applet.init();

    JFrame frame = new JFrame();
    frame.getContentPane().add(applet);
    frame.setTitle("JGraphT Adapter to JGraph Demo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init() {
    // create a JGraphT graph
    ListenableGraph<String, DefaultEdge> g =
      new ListenableUndirectedMultigraph<>(DefaultEdge.class);

    // create a visualization using JGraph, via an adapter
    jgAdapter = new JGraphModelAdapter<>(g);

    JGraph jgraph = new JGraph(jgAdapter);

    adjustDisplaySettings(jgraph);
    getContentPane().add(jgraph);
    resize(DEFAULT_SIZE);

    String v1 = "223";
    String v2 = "564";
    String v3 = "774";
    String v4 = "7";

    // add some sample data (graph manipulated via JGraphT)
    g.addVertex(v1);
    g.addVertex(v2);
    g.addVertex(v3);
    g.addVertex(v4);

    g.addEdge(v1, v2);
    g.addEdge(v2, v3);
    g.addEdge(v3, v1);
    g.addEdge(v4, v3);

    // position vertices nicely within JGraph component
    positionVertexAt(v1, 130, 40);
    positionVertexAt(v2, 60, 200);
    positionVertexAt(v3, 310, 230);
    positionVertexAt(v4, 380, 70);

    // that's all there is to it!...
  }

  private void adjustDisplaySettings(JGraph jg) {
    jg.setPreferredSize(DEFAULT_SIZE);

    Color c = DEFAULT_BG_COLOR;
    String colorStr = null;

    try {
      colorStr = getParameter("bgcolor");
    } catch (Exception e) {
    }

    if (colorStr != null) {
      c = Color.decode(colorStr);
    }

    jg.setBackground(c);
  }

  @SuppressWarnings("unchecked")
  private void positionVertexAt(Object vertex, int x, int y) {
    DefaultGraphCell cell = jgAdapter.getVertexCell(vertex);
    AttributeMap attr = cell.getAttributes();
    Rectangle2D bounds = GraphConstants.getBounds(attr);

    Rectangle2D newBounds = new Rectangle2D.Double(x, y, bounds.getWidth(), bounds.getHeight());

    GraphConstants.setBounds(attr, newBounds);

    AttributeMap cellAttr = new AttributeMap();
    cellAttr.put(cell, attr);
    jgAdapter.edit(cellAttr, null, null, null);
  }

  /**
   * a listenable directed multigraph that allows loops and parallel edges.
   */
  private static class ListenableUndirectedMultigraph<V, E>
    extends DefaultListenableGraph<V, E>
    implements UndirectedGraph<V, E> {
    private static final long serialVersionUID = 1L;

    ListenableUndirectedMultigraph(Class<E> edgeClass) {
      super(new DirectedMultigraph<>(edgeClass));
    }
  }
  
}

