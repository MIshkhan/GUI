import javafx.stage.Stage;
import javafx.scene.Scene; 
import javafx.scene.paint.Color; 
import javafx.application.Application;

import graph.Visualization;

public class Main  extends Application {

  @Override 
  public void start(Stage stage) {
    Visualization g = new Visualization();
  
    g.setVertex(0,100,350);
    g.setVertex(1,100,50);
    g.setVertex(2,220,290);
    g.setVertex(3,500,350);
    g.setVertex(4,300,70);

    g.setEdge(1,0, 15);
    g.setEdge(2,0, 15);
    g.setEdge(3,0, 15);
    g.setEdge(4,0, 15);

    g.setEdge(2,1, 20);
    g.setEdge(3,1, 20);
    g.setEdge(4,1, 20);
    
    g.setEdge(3,2, 20);
    g.setEdge(4,2, 20);
    
    g.setEdge(4,3, 30);
    
    Scene scene = new Scene(g.getRoot(), 1200, 700);  
    stage.setTitle("Graph");
    scene.setFill(Color.SKYBLUE);
    stage.setScene(scene);
    stage.show();         
  } 
  
   public static void main(String args[]){ 
      launch(args); 
   }
} 
