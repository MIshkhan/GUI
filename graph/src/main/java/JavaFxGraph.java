import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.Group; 
import javafx.scene.Scene; 
import javafx.scene.paint.Color; 
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle; 
import javafx.scene.effect.ImageInput; 
import javafx.scene.image.Image; 
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
  
public class JavaFxGraph extends Application {

  private List<List<Integer>> edges = new ArrayList<>();
  
  public Text text(int x, int y, int fontSize, String message) {
    Text text = new Text();
    text.setFont(new Font(fontSize));
    text.setX(x);
    text.setY(y);
    text.setText(message);
    return text;
  }

  public Line addEdge(int v1, int v2, int weight) {
    if(edges.get(v1).get(v2) == null) {
      edges.get(v1).add(v2);
      edges.get(v2).add(v1);
    }
    return new Line(350, 100, 720, 100);
  }
  
  public void drowLineWithText(Group root) {
    Line l = new Line(350, 100, 720, 100);
    
    root.getChildren().add(l);
    
  }
    
  @Override 
  public void start(Stage stage) {       
    //Creating a Group object  
    Group root = new Group(); 

    drowLineWithText(root);
 
    Scene scene = new Scene(root, 1200, 700);  
    stage.setTitle("Drawing an arc through a path");
    scene.setFill(Color.SKYBLUE);     
    stage.setScene(scene);
    stage.show();         
  } 
  
  public static void main(String args[]){          
    launch(args);     
  }

  class Edge {
    private int firstVertex;
    private int secondVertex;
    private int weight;

    public Edge(int v1, int v2, int weight){
      this.firstVertex = v1;
      this.secondVertex = v2;
      this.weight = weight;
    }
    
    public int getWeight() {
      return this.weight;
    }
  }
  
} 
