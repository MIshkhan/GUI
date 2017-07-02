package graph;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Text;

public class VisualGraph {

  //variables for storing initial position before drag of circle
  private double initX;
  private double initY;
  private Group root = new Group();

  private Circle[] vertices = null;
  private Graph graph = null;
    
  //create a rectangle - (450px X 250px) in which our circles can move
  final Rectangle rectangle = RectangleBuilder.create()
  .width(1100).height(700)
    .fill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop[]{
    	  new Stop(1, Color.rgb(156, 216, 255)),
    	  new Stop(0, Color.rgb(156, 216, 255, 0.5))
    	}))
    .stroke(Color.BLACK)
    .build();


  public VisualGraph(int numberOfVertices) {
    if(numberOfVertices > 0) {
      graph = new Graph(numberOfVertices);
      vertices = new Circle[numberOfVertices];
      root.getChildren().add(rectangle);
    }
    else
      throw new IllegalArgumentException("Number of vertices in graph must be positive ...");
  }
    
  public void addEdge(int v1, int v2, double weight) {
    graph.addEdge(v1, v2 , weight);
    
    Line link = new Line();
    link.setFill(Color.RED);
    link.setStrokeWidth(5);
	
    link.startXProperty().bind(vertices[v1].centerXProperty());
    link.startYProperty().bind(vertices[v1].centerYProperty());	
    link.endXProperty().bind(vertices[v2].centerXProperty());
    link.endYProperty().bind(vertices[v2].centerYProperty());

    root.getChildren().add(link);
    link.toBack();

    Text text = new Text( String.valueOf(weight) );
    text.xProperty().bind((vertices[v1].centerXProperty().add(vertices[v2].centerXProperty()).divide(2).add(5)));
    text.yProperty().bind((vertices[v1].centerYProperty().add(vertices[v2].centerYProperty()).divide(2).add(5)));
    root.getChildren().add(text);
    
  }

  public void addVertex(final Color color, int radius, int x, int y, int index) {
    
    graph.addVertex(index);
    //create a circle with desired name,  color and radius
    final Circle circle = new Circle(radius , new RadialGradient(0, 0, 0.2, 0.3, 1, true, CycleMethod.NO_CYCLE, new Stop[]{
	  new Stop(0, Color.rgb(250, 250, 255)),
	  new Stop(1, color)
	}));
    circle.setCenterX(x);
    circle.setCenterY(y);
	
    //add a shadow effect
    circle.setEffect(new InnerShadow(7, color.darker().darker()));
    //change a cursor when it is over circle
    circle.setCursor(Cursor.HAND);
	
    circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
	public void handle(MouseEvent me) {
	  double dragX = me.getSceneX();
	  double dragY = me.getSceneY();
	  //calculate new position of the circle
	  double newXPosition = initX + dragX;
	  double newYPosition = initY + dragY;
	  //if new position do not exceeds borders of the rectangle, translate to this position
	  if ((newXPosition >= circle.getRadius()) && (newXPosition <= rectangle.getWidth() - circle.getRadius())) {
	    circle.setCenterX(newXPosition);
	  }
	  if ((newYPosition >= circle.getRadius()) && (newYPosition <= rectangle.getHeight() - circle.getRadius())) {
	    circle.setCenterY(newYPosition);
	  }
	}
      });
    circle.setOnMouseEntered(new EventHandler<MouseEvent>() {
	public void handle(MouseEvent me) {
	  //change the z-coordinate of the circle
	  circle.toFront();
	}
      });
    circle.setOnMousePressed(new EventHandler<MouseEvent>() {
	public void handle(MouseEvent me) {
	  //when mouse is pressed, store initial position
	  initX = circle.getTranslateX();
	  initY = circle.getTranslateY();
	}
      });
	
    vertices[index] = circle;

    Text text = new Text("V"+index);
    text.xProperty().bind(vertices[index].centerXProperty().add(circle.getRadius()));
    text.yProperty().bind(vertices[index].centerYProperty().add(circle.getRadius()));

    root.getChildren().add(text);
    root.getChildren().add(circle);
  }

  public Group getRoot() {
    return this.root;
  }
}
