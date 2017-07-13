package graph;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.beans.value.*;
import javafx.geometry.Bounds;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

public class Grid {

    private Group root = new Group();

    private int startX = 10;
    private int startY = 10;
    private int rowNumber = 40;
    private int columnNumber = 120;
    private int cellHeight = 10;
    private int cellWidth = 10;
    private StackPane zoomPane = null;

    private GridPane daddy = new GridPane();

    public Grid() {
        makeGrid();
    }

    public Group getRoot() {
        return this.root;
    }

    private void makeGrid() {
        //vertices on grid
        double radius = cellWidth / 5;
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

    public BorderPane getMenuBar() {
        BorderPane rootNode = new BorderPane();
        Label response = new Label("Menu Demo");

        // Create the menu bar.
        MenuBar mb = new MenuBar();

        // Create the File menu.
        Menu fileMenu = new Menu("File");
        MenuItem open = new MenuItem("Open");
        MenuItem save = new MenuItem("Save");
        MenuItem exit = new MenuItem("Exit");
        fileMenu.getItems().addAll(open, save, new SeparatorMenuItem(), exit);

        mb.getMenus().add(fileMenu);


        Menu view = new Menu("View");
        Menu backgrounds = new Menu("Backgrounds");
        MenuItem skyBlue = new MenuItem("Sky-blue");
        MenuItem green = new MenuItem("Green");
        MenuItem black = new MenuItem("Black");
        backgrounds.getItems().addAll(skyBlue, green, black);
        view.getItems().add(backgrounds);

        mb.getMenus().add(view);

        Menu run = new Menu("Run");
        Menu algorithms = new Menu("Algorithms");
        MenuItem steiner = new MenuItem("Steiner");
        algorithms.getItems().add(steiner);
        run.getItems().add(algorithms);

        mb.getMenus().add(run);


        // Create the Help menu.
        Menu helpMenu = new Menu("Help");
        MenuItem about = new MenuItem("About");
        helpMenu.getItems().add(about);
        mb.getMenus().add(helpMenu);

        // Create one event handler that will handle menu action events.
        EventHandler<ActionEvent> MEHandler =
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent ae) {
                        String name = ((MenuItem)ae.getTarget()).getText();
                        switch (name) {
                            case "Sky-blue" : zoomPane.setStyle("-fx-background-color: #87CEEB"); break;
                            case "Green" : zoomPane.setStyle("-fx-background-color: #669994"); break;
                            case "Black" : zoomPane.setStyle("-fx-background-color: #242020"); break;
                            case "Exit": Platform.exit();
                        }
                    }
                };
        // Set action event handlers for the menu items.
        //open.setOnAction(MEHandler);
        //save.setOnAction(MEHandler);
        exit.setOnAction(MEHandler);
        skyBlue.setOnAction(MEHandler);
        black.setOnAction(MEHandler);
        green.setOnAction(MEHandler);

        //about.setOnAction(MEHandler);
        // Add the menu bar to the top of the border pane and
        // the response label to the center position.
        rootNode.setTop(mb);
        return  rootNode;
    }

    public Parent createZoomPane() {
        final double SCALE_DELTA = 1.1;
        zoomPane = new StackPane();
        zoomPane.setStyle("-fx-background-color: #669994");
        zoomPane.getChildren().add(root);
        root.resize(500, 500);
        zoomPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override public void handle(ScrollEvent event) {
                event.consume();

                if (event.getDeltaY() == 0) {
                    return;
                }

                double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1/SCALE_DELTA;
                //System.out.println(event.getDeltaY() + "===");

                double scaleX = root.getScaleX() * scaleFactor;
                double scaleY = root.getScaleY() * scaleFactor;

                //System.out.println(scaleX + "        " + scaleY);
                if(scaleX >= 0.5 && scaleX < 11 && scaleY >= 0.5 && scaleY < 11) {
                    root.setScaleX(scaleX);
                    root.setScaleY(scaleY);
                }
                // try {
                //   Thread.sleep(50);
                // }
                // catch (InterruptedException e) {};
            }
        });

        zoomPane.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override public void changed(ObservableValue<? extends Bounds> observable, Bounds oldBounds, Bounds bounds) {
                zoomPane.setClip(new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight()));
            }
        });

        Slider slider = new Slider(1, 10.0, 5.0);
        slider.setShowTickLabels(true);

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                double scaleFactor = (slider.getValue()  > 5) ? 1.1: 1/1.1;
                root.setScaleX(slider.getValue() * scaleFactor);
                root.setScaleY(slider.getValue() * scaleFactor);
                System.out.println(new_val.doubleValue());
            }
        });


        //daddy.getColumnConstraints().add(new ColumnConstraints(300));
        //daddy.getColumnConstraints().add(new ColumnConstraints(100));
        //daddy.getRowConstraints().add(new RowConstraints(30));

        GridPane menuGrid = new GridPane();
        menuGrid.add(getMenuBar(), 0, 0);

        GridPane lowGrid = new GridPane();
        lowGrid.add(new Button("Clean grid"), 0, 0);
        lowGrid.add(slider, 1, 0);

        daddy.add(menuGrid, 0,0);
        daddy.add(zoomPane, 0,1);
        daddy.add(lowGrid, 0,2);

        daddy.setGridLinesVisible(true);
        return daddy;
    }

    public double getHeight() {
        return 900;
    }

    public double getWidth() {
        return 1600;
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

                    //Tooltip.install(vertex, new Tooltip("\nVertex (" + i + ":" + j + ")"));

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




// import javafx.event.EventHandler;
// import javafx.scene.Cursor;
// import javafx.scene.Group;
// import javafx.scene.input.MouseEvent;
// import javafx.scene.paint.Color;
// import javafx.scene.shape.Circle;
// import javafx.scene.shape.Rectangle;
// import javafx.scene.shape.RectangleBuilder;
// import javafx.scene.text.Text;
// import javafx.scene.control.Tooltip;

// import javafx.application.Application;
// import javafx.beans.value.*;
// import javafx.event.*;
// import javafx.geometry.Bounds;
// import javafx.scene.*;
// import javafx.scene.control.*;
// import javafx.scene.image.*;
// import javafx.scene.input.*;
// import javafx.scene.layout.*;

// import javafx.scene.shape.*;
// import javafx.stage.Stage;

// import javafx.scene.control.Button; 


// public class Grid {
  
//   private Group root = new Group();

//   private int startX = 10;
//   private int startY = 10;
//   private int rowNumber = 40;
//   private int columnNumber = 60;
//   private int cellHeight = 20;
//   private int cellWidth = 20;
  
//   // final Rectangle rectangle = RectangleBuilder.create()
//   // .width(1600).height(1200)
//   //   .fill(Color.SKYBLUE)
//   //   .stroke(Color.BLACK)
//   //   .build();

//   public Grid() {
//     //root.getChildren().add(rectangle);
    
//     //makeGrid();
//     Button button =  new Button("File");
      
//     root.getChildren().add(button);
//   }

//   public Group getRoot() {
//     return this.root;
//   }
  
//   private void makeGrid() {
//     //vertices on grid
//     double radius = cellWidth / 5;
//     for( int i = 0; i < rowNumber; ++i ) {
//       double y = startY + i * cellHeight;
//       for( int j = 0; j < columnNumber; ++j ) {
// 	double x = startX + j * cellWidth;
// 	Circle circle = new Circle(x, y, radius);	  
// 	circle.setFill(Color.WHITE);//setFill(Color.color(Math.random(), Math.random(), Math.random()));
// 	circle.setCursor(Cursor.HAND);
         
// 	setRemoveableVertexOnClick(circle, i , j);
	  
// 	root.getChildren().add(circle);
//       }
//     }
//   }

//   public Parent createZoomPane() {
//     final double SCALE_DELTA = 1.1;
//     final StackPane zoomPane = new StackPane();

//     zoomPane.getChildren().add(root);
//     zoomPane.setOnScroll(new EventHandler<ScrollEvent>() {
// 	@Override public void handle(ScrollEvent event) {
// 	  event.consume();

// 	  if (event.getDeltaY() == 0) {
// 	    return;
// 	  }

// 	  double scaleFactor =
// 	    (event.getDeltaY() > 0) ? SCALE_DELTA : 1/SCALE_DELTA;
// 	  //System.out.println(event.getDeltaY() + "===");
	  
// 	  double scaleX = root.getScaleX() * scaleFactor;
// 	  double scaleY = root.getScaleY() * scaleFactor;

// 	  //System.out.println(scaleX + "        " + scaleY);
// 	  if(scaleX >= 1 && scaleX < 4 && scaleY >= 1 && scaleY < 4) {
// 	    root.setScaleX(scaleX);
// 	    root.setScaleY(scaleY);
// 	  }
// 	  // try {
// 	  //   Thread.sleep(50);
// 	  // }
// 	  // catch (InterruptedException e) {};
// 	}
//       });

//     zoomPane.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {
// 	@Override public void changed(ObservableValue<? extends Bounds> observable, Bounds oldBounds, Bounds bounds) {
// 	  zoomPane.setClip(new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight()));
// 	}
//       });

//     return zoomPane;
//   }

//   public double getHeight() {
//     return cellHeight * rowNumber;
//   }

//   public double getWidth() {
//     return cellWidth * columnNumber;
//   }
  
//   private void setRemoveableVertexOnClick(Circle circle, int i, int j) {
//     Circle vertex = new Circle(circle.getCenterX(),circle.getCenterY(), circle.getRadius());
//     circle.setOnMousePressed(new EventHandler<MouseEvent>() {
// 	public void handle(MouseEvent me) {
// 	  //left click
// 	  if (me.isPrimaryButtonDown() && !root.getChildren().contains(vertex)) {	  
// 	    vertex.setFill(Color.BLACK);
// 	    vertex.setCursor(Cursor.HAND);
// 	    removeOnRightClick(vertex);
	    
// 	    //Tooltip.install(vertex, new Tooltip("\nVertex (" + i + ":" + j + ")"));
	    
// 	    root.getChildren().add(vertex);
// 	  }
// 	}
//       });
//   }
 
//   private void removeOnRightClick(Circle circle) {
//     circle.setOnMousePressed(new EventHandler<MouseEvent>() {
// 	public void handle(MouseEvent me) {
// 	  if (me.isSecondaryButtonDown() && root.getChildren().contains(circle)) {
// 	    circle.toBack();
// 	    root.getChildren().remove(circle);
// 	  }
// 	}
//       });
//   }
// }
