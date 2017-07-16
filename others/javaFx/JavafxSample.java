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
    
public class JavafxSample extends Application {

  public void drowStar(Group root) {
    Path path = new Path(); 
    MoveTo moveTo = new MoveTo(108, 71);
    LineTo line1 = new LineTo(321, 161);  
    LineTo line2 = new LineTo(126,232);       
    LineTo line3 = new LineTo(232,52);  
    LineTo line4 = new LineTo(269, 250);   
    LineTo line5 = new LineTo(108, 71);  
      
    path.getElements().add(moveTo); 
    path.getElements().addAll(line1, line2, line3, line4, line5);
    root.getChildren().add(path);
  }

  public void drowLineWithText(Group root) {
    Line line = new Line();
    line.setStartX(350.0); 
    line.setStartY(100.0); 
    line.setEndX(720.0); 
    line.setEndY(100.0); 

    Text text = new Text();
    text.setFont(new Font(45));
    text.setX(350);
    text.setY(150);
    text.setText("What's up dude?");

    root.getChildren().add(line);
    root.getChildren().add(text);
  }

  public void imageInputEffect(Group root) {
    Image image = new Image("http://www.tutorialspoint.com/green/images/logo.png"); 
    Rectangle rectangle = new Rectangle(); 
    ImageInput imageInput = new ImageInput(); 
    imageInput.setX(150); 
    imageInput.setY(300);       
    imageInput.setSource(image); 
    rectangle.setEffect(imageInput);
    root.getChildren().add(rectangle);
  } 
    
  @Override 
  public void start(Stage stage) {       
    //Creating a Group object  
    Group root = new Group(); 
    drowStar(root);
    drowLineWithText(root);
    imageInputEffect(root);
	

    Scene scene = new Scene(root, 1200, 700);  
    stage.setTitle("Drawing an arc through a path");
    scene.setFill(Color.SKYBLUE);     
    stage.setScene(scene);
    stage.show();         
  } 
  
  public static void main(String args[]){          
    launch(args);     
  }
  
} 
