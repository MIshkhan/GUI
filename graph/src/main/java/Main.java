import javafx.stage.Stage;
import javafx.scene.Scene; 
import javafx.scene.paint.Color; 
import javafx.application.Application;

import graph.VisualGraph;

public class Main  extends Application {

  private void init(Stage primaryStage) {
    VisualGraph graph = new VisualGraph(8);
	    
    graph.addVertex(Color.CORAL,      15, 250, 150, 1);
    graph.addVertex(Color.DODGERBLUE, 15, 250, 250, 2);
    graph.addVertex(Color.GREEN,      15, 150, 250, 3);
    graph.addVertex(Color.MAGENTA,    15, 350, 150, 4);
    graph.addVertex(Color.BLACK,      15, 450, 150, 5);
    graph.addVertex(Color.RED,        15, 150, 350, 6);
    graph.addVertex(Color.YELLOW,     15, 150, 450, 7);
	
    graph.addEdge(1,2, 8);
    graph.addEdge(2,3, 8);
    graph.addEdge(3,4, 8);
    graph.addEdge(4,1, 8);

    graph.addEdge(3,5, 10);
    graph.addEdge(4,6, 10);
    graph.addEdge(7,6, 10);

    Scene scene = new Scene(graph.getRoot());
    scene.setFill(Color.SKYBLUE);
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);

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
