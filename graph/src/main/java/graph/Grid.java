package graph;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Text;
import javafx.scene.control.Tooltip;

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

public class Grid {
  
  private Group root = new Group();
  private Circle[] vertices = null;
  private Graph graph = null;

  private int startX;
  private int startY;
  private int rowNumber;
  private int columnNumber;
  private int cellHeight;
  private int cellWidth;
  
  final Rectangle rectangle = RectangleBuilder.create()
  .width(1500).height(800)
    .fill(Color.SKYBLUE)
    .stroke(Color.BLACK)
    .build();

  public Grid( int startX, int startY, int rowNumber, int columnNumber, int cellHeight, int cellWidth ) {
    if( startX > 0 && startY > 0 && rowNumber > 0 && columnNumber > 0 && cellHeight > 0 && cellWidth > 0 ) {
      root.getChildren().add(rectangle);
      this.startX = startX;
      this.startY = startY;
      this.rowNumber = rowNumber;
      this.columnNumber = columnNumber;
      this.cellHeight = cellHeight;
      this.cellWidth = cellWidth;
      makeGrid();
    }
    else
      throw new IllegalArgumentException("Wrong argument list ...");
  }

  public Group getRoot() {
    return this.root;
  }
  
  private void makeGrid() {
    //vertices on grid
    double radius = cellWidth / 5;
    addGridIndexing();
    for( int i = 0; i < rowNumber; ++i ) {
      double y = startY + i * cellHeight;
      for( int j = 0; j < columnNumber; ++j ) {
	double x = startX + j * cellWidth;
	Circle circle = new Circle(x, y, radius);	  
	circle.setFill(Color.WHITE);//setFill(Color.color(Math.random(), Math.random(), Math.random()));
	circle.setCursor(Cursor.HAND);
         
	setRemoveableVertexOnClick(circle, i , j);
	  
	root.getChildren().add(circle);
      }
    }
  }
  
  private void addGridIndexing() {
    
    double radius = cellWidth / 5;
    double y1 = startY - radius - 2; 
    double y2 = y1 + rowNumber * cellHeight;
    
    for( int i = 0; i < columnNumber; ++i ) {
      Text upNumber = new Text("" + i);
      upNumber.setX(startX + i * cellWidth - radius + 1);
      upNumber.setY(y1);
      root.getChildren().add(upNumber);

      Text downNumber = new Text("" + i);
      downNumber.setX(startX + i * cellWidth - radius + 1);
      downNumber.setY(y2);
      root.getChildren().add(downNumber);
    }

    double x1  = startX - 4 * radius - 2;
    double x2  = x1 + columnNumber * cellWidth + 4;
    
    for( int i = 0; i < rowNumber; ++i ) {
      Text leftNumber = new Text("" + i);
      leftNumber.setX(x1);
      leftNumber.setY(startY + i * cellHeight + radius + 1);	    
      root.getChildren().add(leftNumber);

      Text rightNumber = new Text("" + i);
      rightNumber.setX(x2);
      rightNumber.setY(startY + i * cellHeight + radius + 1);
      root.getChildren().add(rightNumber);
    }
  }

  public Parent createZoomPane() {
    final double SCALE_DELTA = 1.1;
    final StackPane zoomPane = new StackPane();

    zoomPane.getChildren().add(root);
    zoomPane.setOnScroll(new EventHandler<ScrollEvent>() {
	@Override public void handle(ScrollEvent event) {
	  event.consume();

	  if (event.getDeltaY() == 0) {
	    return;
	  }

	  double scaleFactor =
	    (event.getDeltaY() > 0)
            ? SCALE_DELTA
            : 1/SCALE_DELTA;

	  root.setScaleX(root.getScaleX() * scaleFactor);
	  root.setScaleY(root.getScaleY() * scaleFactor);
	}
      });

    zoomPane.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {
	@Override public void changed(ObservableValue<? extends Bounds> observable, Bounds oldBounds, Bounds bounds) {
	  zoomPane.setClip(new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight()));
	}
      });

    return zoomPane;
  }

  
  private void setRemoveableVertexOnClick(Circle circle, int i, int j) {
    Circle vertex = new Circle(circle.getCenterX(),circle.getCenterY(), circle.getRadius());
    circle.setOnMousePressed(new EventHandler<MouseEvent>() {
	public void handle(MouseEvent me) {
	  //left click
	  if (me.isPrimaryButtonDown() && !root.getChildren().contains(vertex)) {	  
	    vertex.setFill(Color.BLACK);
	    vertex.setCursor(Cursor.HAND);
	    removeOnRightClick(vertex);
	    
	    Tooltip.install(vertex, new Tooltip("\nVertex (" + i + ":" + j + ")"));
	    
	    root.getChildren().add(vertex);
	  }
	}
      });
  }
 
  private void removeOnRightClick(Circle circle) {
    circle.setOnMousePressed(new EventHandler<MouseEvent>() {
	public void handle(MouseEvent me) {
	  if (me.isSecondaryButtonDown() && root.getChildren().contains(circle)) {
	    circle.toBack();
	    root.getChildren().remove(circle);
	  }
	}
      });
  }
}
