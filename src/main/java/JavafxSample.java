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

public class JavafxSample extends Application {

  @Override 
  public void start(Stage stage) { 
    //Creating a Path 
    Path path = new Path(); 
       
    //Moving to the starting point 
    MoveTo moveTo = new MoveTo(108, 71);
    
    LineTo line1 = new LineTo(321, 161);  
    LineTo line2 = new LineTo(126,232);       
    LineTo line3 = new LineTo(232,52);  
    LineTo line4 = new LineTo(269, 250);   
    LineTo line5 = new LineTo(108, 71);  
       
    //Adding all the elements to the path 
    path.getElements().add(moveTo); 
    path.getElements().addAll(line1, line2, line3, line4, line5);        
         
    //Creating a Group object  
    Group root = new Group(path); 
         
    //Creating a scene object 
    Scene scene = new Scene(root, 600, 300);  
      
    //Setting title to the Stage 
    stage.setTitle("Drawing an arc through a path");
      
    //Adding scene to the stage 
    stage.setScene(scene);
      
    //Displaying the contents of the stage 
    stage.show();         
  } 

  /*
  @Override     
  public void start(Stage primaryStage) throws Exception {
     
    Line line = new Line();
    line.setStartX(50.0); 
    line.setStartY(100.0); 
    line.setEndX(420.0); 
    line.setEndY(100.0); 

    Text text = new Text();
    text.setFont(new Font(45));
    text.setX(50);
    text.setY(150);
    text.setText("What's up dude?");

    
    Group root = new Group();   
    ObservableList list = root.getChildren(); 

    list.add(text);
    list.add(line);

    Scene scene = new Scene(root, 600, 300); 


    
    scene.setFill(Color.SKYBLUE);  
      
    primaryStage.setTitle("Sample Application"); 
    primaryStage.setScene(scene); 
    primaryStage.show(); 
  }
  */
  
  public static void main(String args[]){          
    launch(args);     
  }
  
} 
