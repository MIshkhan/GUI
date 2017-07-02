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

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.shape.Circle;

import graph.Graph;

public class Visualization  {

    private Pane root = new Pane();
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

	final double handleRadius = 5 ;
	Circle moveHandle = new Circle(handleRadius, Color.GREEN);
	// bind to bottom center of Rectangle:
	moveHandle.centerXProperty().bind(r.xProperty().add(r.widthProperty()));
	moveHandle.centerYProperty().bind(r.yProperty().add(r.heightProperty()));
				      
	// force circles to live in same parent as rectangle:
	r.parentProperty().addListener((obs, oldParent, newParent) -> {
		Circle c =  moveHandle;
		Pane currentParent = (Pane)c.getParent();
		if (currentParent != null) {
		    currentParent.getChildren().remove(c);
		}
		((Pane)newParent).getChildren().add(c);
	    });
    
	Wrapper<Point2D> mouseLocation = new Wrapper<>();
	setUpDragging(moveHandle, mouseLocation) ;
    
	moveHandle.setOnMouseDragged(event -> {
		if (mouseLocation.value != null) {
		    double deltaX = event.getSceneX() - mouseLocation.value.getX();
		    double deltaY = event.getSceneY() - mouseLocation.value.getY();
		    double newX = r.getX() + deltaX ;
		    double newMaxX = newX + r.getWidth();
		    if (newX >= handleRadius && newMaxX <= r.getParent().getBoundsInLocal().getWidth() - handleRadius) {
			r.setX(newX);
		    }
		    double newY = r.getY() + deltaY ;
		    double newMaxY = newY + r.getHeight();
		    if (newY >= handleRadius && newMaxY <= r.getParent().getBoundsInLocal().getHeight() - handleRadius) {
			r.setY(newY);
		    }
		    mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
		}
	    
	    });
    
	root.getChildren().addAll(r, text);
    }

    private void setUpDragging(Circle circle, Wrapper<Point2D> mouseLocation) {
	
        circle.setOnDragDetected(event -> {
		circle.getParent().setCursor(Cursor.CLOSED_HAND);
		mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
	    });

        circle.setOnMouseReleased(event -> {
		circle.getParent().setCursor(Cursor.DEFAULT);
		mouseLocation.value = null ;
	    });
    }
    
    static class Wrapper<T> { T value ; }
  
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

    public Pane getRoot() {
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
