package graph;

import javafx.collections.ObservableList;
import javafx.scene.Group; 
import javafx.scene.paint.Color; 
import javafx.scene.shape.Line;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle; 
import javafx.scene.effect.ImageInput; 
import javafx.scene.image.Image; 
import javafx.scene.layout.Pane;

import graph.Graph;

public class Visualization  {

  private Group root = new Group();
  private Graph graph = new Graph(5);
  private XY[] vCordinate = new XY[5];
  
  public Text text(int x, int y, int fontSize, String message) {
    Text text = new Text();
    text.setFont(new Font(fontSize));
    text.setX(x);
    text.setY(y);
    text.setText(message);
    return text;
  }

  public void setVertex(Integer v, int x, int y) {
    graph.addVertex(v);
    vCordinate[v] = new XY(x, y);
    
    Rectangle r = new Rectangle();
    r.setX(x);
    r.setY(y);
    r.setWidth(50);
    r.setHeight(30);
    r.setArcWidth(10);
    r.setArcHeight(10);
    r.setFill(Color.WHITE);

    Text text = text(x+16, y+17, 12, "V"+v);
    
    root.getChildren().addAll(r, text);
  }
  
  public void setEdge(Integer v1, Integer v2, Integer weight) {
    graph.addEdge(v1, v2, weight);    
    int x1, y1, x2, y2;

    if( vCordinate[v1].x > vCordinate[v2].x ) {
      x1 = vCordinate[v2].x + 50 ;
      x2 = vCordinate[v1].x;
      if( vCordinate[v1].y == vCordinate[v2].y ) {
        y1 = vCordinate[v1].y + 15;
        y2 = y1;
      } else {
        y1 = vCordinate[v2].y + 15;
        y2 = vCordinate[v1].y + 15;
      }
    } else if( vCordinate[v1].x < vCordinate[v2].x ) {
      x1 = vCordinate[v1].x + 50;
      x2 = vCordinate[v2].x;
      if( vCordinate[v1].y == vCordinate[v2].y ) {
        y1 = vCordinate[v1].y+15;
        y2 = y1;
      } else {
        y1 = vCordinate[v1].y + 15;
        y2 = vCordinate[v2].y + 15;
      }
    } else {
      x1 = vCordinate[v1].x + 25;
      x2 = x1;
      if( vCordinate[v1].y > vCordinate[v2].y ) {
        y1 = vCordinate[v1].y;
        y2 = vCordinate[v2].y + 30;
      } else {
        y1 = vCordinate[v1].y + 30;
        y2 = vCordinate[v2].y;
      }
    }
    
    root.getChildren().addAll( new Line(x1, y1, x2, y2) );    
  }

  public Group getRoot() {
    return this.root;
  }
  
  class XY {
    int x = 0;
    int y = 0;

    public XY(int x, int y) {
      this.x = x;
      this.y = y; 
    }
  }
}
