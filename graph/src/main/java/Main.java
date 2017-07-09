import javafx.stage.Stage;
import javafx.scene.Scene; 
import javafx.scene.paint.Color; 
import javafx.application.Application;

import graph.Grid;

import javafx.application.Application;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.geometry.Bounds;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class Main  extends Application {

  private void init(Stage stage) {
    
    Grid graph = new Grid(50, 50, 14, 18, 50, 50);
	    
    //Scene scene = new Scene(graph.getRoot());
    //scene.setFill(Color.SKYBLUE);
    //primaryStage.setScene(scene);
    //primaryStage.setResizable(false);

    Group group = graph.getRoot();
    Parent zoomPane = graph.createZoomPane();

    VBox layout = new VBox();
    layout.getChildren().setAll(
        zoomPane
    );

    VBox.setVgrow(zoomPane, Priority.ALWAYS);
    Scene scene = new Scene(
        layout
    );
    scene.setFill(Color.SKYBLUE);

    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();

    
  }
  @Override
  public void start(Stage primaryStage) throws Exception {
    init(primaryStage);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

/*{
  graph.addVertex(Color.CORAL,      10, 500, 100, 1);
  graph.addVertex(Color.DODGERBLUE, 10, 550, 100, 2);
  graph.addVertex(Color.GREEN,      10, 600, 100, 3);
  graph.addVertex(Color.MAGENTA,    10, 500, 200, 4);
  graph.addVertex(Color.BLACK,      10, 550, 200, 5);
  graph.addVertex(Color.RED,        10, 500, 150, 6);
  graph.addVertex(Color.YELLOW,     10, 650, 200, 7);

  graph.addEdge(1,2, 8);
  graph.addEdge(2,3, 8);
  graph.addEdge(3,4, 8);
  graph.addEdge(4,1, 8);
  graph.addEdge(3,5, 10);
  graph.addEdge(4,6, 10);
  graph.addEdge(7,6, 10);
  }*/
