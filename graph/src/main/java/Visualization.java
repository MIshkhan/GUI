package graph;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.Group; 
import javafx.scene.Scene; 
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
import javafx.scene.layout.StackPane;

import graph.Graph;

public class Visualization extends Application {

  Group root = new Group(); 
  Graph graph = new Graph(5);
  
  public Text text(int x, int y, int fontSize, String message) {
    Text text = new Text();
    text.setFont(new Font(fontSize));
    text.setX(x);
    text.setY(y);
    text.setText(message);
    return text;
  }

  public StackPane makeVertex(Integer v) {
    graph.addVertex(v);
    
    Rectangle r = new Rectangle();
    r.setX(v*50);
    r.setY(v*50);
    r.setWidth(50);
    r.setHeight(25);
    r.setArcWidth(5);
    r.setArcHeight(5);
    r.setFill(Color.YELLOW);

    StackPane pane = new StackPane();
    Text text = new Text("V"+v);
    text.setBoundsType(TextBoundsType.VISUAL); 
    pane.getChildren().addAll(r, text);
    
    return pane;
  }
  
  public void drowLineWithText(Group root) {
    Line l = new Line(350, 100, 720, 100);    
  }
    
  @Override 
  public void start(Stage stage) {

    //root.getChildren().add( makeVertex(1) );
    
    //root.getChildren().add( makeVertex(2) );

    // root.getChildren().add( makeVertex(3) );
    
    Scene scene = new Scene(root, 1200, 700);  
    stage.setTitle("Graph");
    scene.setFill(Color.SKYBLUE);
    
    //    scene.getChildren().add(new Scene(makeVertex(1)));
    stage.setScene(scene);

    stage.show();         
  } 
  
  public void main(String args[]){
    launch(args);     
  }
  
}
