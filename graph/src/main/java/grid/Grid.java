package graph;

import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

import javafx.event.*;
import javafx.beans.value.*;

import javafx.geometry.Bounds;
import javafx.geometry.Orientation;

import javafx.application.Platform;

public class Grid {

  private final double START_X = 10;
  private final double START_Y = 10;
  
  private final double ROW_NUMBER = 40;
  private final double COLUMN_NUMBER = 120;

  private final double CELL_WIDTH = 10;
  private final double CELL_HEIGHT = 10;

  private final double MIN_SCALE = 1; 
  private final double MAX_SCALE = 11; 
  private final double SCALE_DELTA = 1.1;
  
  private Group root = new Group();
  private StackPane zoomPane = null;
  private GridPane daddy = new GridPane();
  
  public Grid() {
    makeGrid();
  }

  private void makeGrid() {
      
    double radius = CELL_WIDTH / 5;
    for( int i = 0; i < ROW_NUMBER; ++i ) {
      double y = START_Y + i * CELL_HEIGHT;
      for( int j = 0; j < COLUMN_NUMBER; ++j ) {
        double x = START_X + j * CELL_WIDTH;
        
        Circle circle = new Circle(x, y, radius);
        circle.setFill(Color.WHITE);
        circle.setCursor(Cursor.HAND);
        setRemoveableVertexOnClick(circle);
        root.getChildren().add(circle);
      }
    }
    
  }

  public Parent createZoomPane() {
    
    Slider slider = new Slider(1, 10.0, 5.5);

    zoomPane = new StackPane();
    zoomPane.setStyle("-fx-background-color: #669994");

    zoomPane.getChildren().add(root);

    //root.resize(500, 500);
    root.setScaleX(5.5);
    root.setScaleY(5.5);

    {

      zoomPane.setOnScroll(new EventHandler<ScrollEvent>() {
        @Override public void handle(ScrollEvent event) {
          event.consume();

          if (event.getDeltaY() == 0) {
            return;
          }

          double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1/SCALE_DELTA;

          double scaleX = root.getScaleX() * scaleFactor;
          double scaleY = root.getScaleY() * scaleFactor;

          if(scaleX >= MIN_SCALE && scaleX < MAX_SCALE && scaleY >= MIN_SCALE && scaleY < MAX_SCALE) {
            root.setScaleX(scaleX);
            root.setScaleY(scaleY);
            slider.setValue(slider.getValue() * scaleFactor);
          }
        }
      });

    zoomPane.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {
        @Override public void changed(ObservableValue<? extends Bounds> observable, Bounds oldBounds, Bounds bounds) {
          zoomPane.setClip(new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight()));
        }
      });

    slider.setShowTickLabels(true);

    slider.valueProperty().addListener(new ChangeListener<Number>() {
        public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
          double scaleFactor = 1.1;//(slider.getValue()  > 5) ? 1.1: 1/1.1;
          root.setScaleX(slider.getValue() * scaleFactor);
          root.setScaleY(slider.getValue() * scaleFactor);
          System.out.println(new_val.doubleValue());
        }
      });
    }
    {
      
      GridPane header = new GridPane();
      GridPane footer = new GridPane();
      
      header.add(getMenuBar(), 0, 0);
      footer.add(new Button("Clean grid"), 0, 0);
      footer.add(slider, 1, 0);

      ScrollBar vScrol = new ScrollBar();
      ScrollBar hScrol = new ScrollBar();
      vScrol.setOrientation(Orientation.VERTICAL);
      hScrol.setOrientation(Orientation.HORIZONTAL);

      hScrol.setLayoutX(zoomPane.getWidth()-hScrol.getWidth());
      hScrol.valueProperty().addListener(new ChangeListener<Number>() {
          public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
            zoomPane.setLayoutY(-new_val.doubleValue());
          }});
      
      daddy.add(header,   0, 0);
      daddy.add(zoomPane, 0, 1);
      daddy.add(hScrol,   0, 2);
      daddy.add(footer,   0, 3);
      daddy.add(vScrol,   1, 1);
    
      daddy.setGridLinesVisible(true);
    }
    
    return daddy;
  }

  private void setRemoveableVertexOnClick(Circle circle) {
    Circle vertex = new Circle(circle.getCenterX(),circle.getCenterY(), circle.getRadius());
    circle.setOnMousePressed(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent me) {
          //left click
          if (me.isPrimaryButtonDown() && !root.getChildren().contains(vertex)) {
            vertex.setFill(Color.BLACK);
            vertex.setCursor(Cursor.HAND);
            removeOnRightClick(vertex);
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

    exit.setOnAction(MEHandler);
    skyBlue.setOnAction(MEHandler);
    black.setOnAction(MEHandler);
    green.setOnAction(MEHandler);

    rootNode.setTop(mb);
    return  rootNode;
  }

  public double getWindowHeight() {
    return 900;
  }

  public double getWindowWidth() {
    return 1600;
  }
  
}
