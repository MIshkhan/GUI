package graph;

import javafx.scene.*;
import javafx.stage.Stage;

import javafx.scene.shape.*;
import javafx.scene.paint.Color;

import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.CategoryAxis;

import javafx.event.*;
import javafx.beans.value.*;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;

import javafx.application.Platform;

import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import java.io.*;

public class Grid {

  private final double START_X = 0;
  private final double START_Y = 0;
  
  private final int ROW_NUMBER = 40;
  private final int COLUMN_NUMBER = 120;

  private final int CELL_WIDTH = 10;
  private final int CELL_HEIGHT = 10;

  private final double MIN_SCALE = 1; 
  private final double MAX_SCALE = 11; 
  private final double SCALE_DELTA = 1.1;
  
  private Group root = new Group();
  private StackPane zoomPane = null;
  private GridPane daddy = new GridPane();

  private ArrayList<Integer>[] verticies = new ArrayList[ROW_NUMBER];
  
  public Grid() {
    for(int i = 0; i < ROW_NUMBER; ++i) 
      verticies[i] = new ArrayList<Integer>();

    makeGrid();

    for(Point p: FileIO.readPoints())
      addVertex(p.x, p.y);
  }

  private void makeGrid() {
      
    double radius = CELL_WIDTH / 5;
    for( int i = 0; i < ROW_NUMBER; ++i ) {
      double y = START_Y + i * CELL_HEIGHT;
      for( int j = 0; j < COLUMN_NUMBER; ++j ) {
        double x = START_X + j * CELL_WIDTH;
        
        Circle circle = new Circle(x, y, radius, Color.WHITE);
        circle.setCursor(Cursor.HAND);
        circle.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
              if (me.isPrimaryButtonDown() && circle.getFill() == Color.WHITE) { //left click
                circle.setFill(Color.BLACK);
                verticies[(int)circle.getCenterY() / 10].add( (int)circle.getCenterX() / 10 );
              } else if (me.isSecondaryButtonDown() && circle.getFill() == Color.BLACK) { //right click
                circle.setFill(Color.WHITE);
                verticies[(int)circle.getCenterY() / 10].remove( new Integer((int)circle.getCenterX() / 10 ));
              }
            }
          });
        
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
          //System.out.println(new_val.doubleValue());
        }
      });
     
    GridPane header = new GridPane();
    GridPane footer = new GridPane();
    Button clearButton = new Button("Clean grid");

    clearButton.setOnAction((event) -> cleanGrid());
    
    header.add(getMenuBar(), 0, 0);
    footer.add(clearButton, 0, 0);
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
    
    return daddy;
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
    MenuItem steiner = new MenuItem("Steiner");
    MenuItem generator = new MenuItem("Generator");
    MenuItem analyzer = new MenuItem("Analyzer");
    run.getItems().addAll(steiner, generator, analyzer);

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
          case "Green"    : zoomPane.setStyle("-fx-background-color: #669994"); break;
          case "Black"    : zoomPane.setStyle("-fx-background-color: #242020"); break;
          case "Open"     : // for(Point p: FileIO.readPoints()) addVertex(p.x, p.y); break;
          case "Steiner"  : runSteiner(); break;
          case "Generator": runGenerator(); break;
          case "Analyzer" : runAnalyzer(); break; 
          case "Exit"     : Platform.exit();
          }
        }
      };

    open.setOnAction(MEHandler);
    exit.setOnAction(MEHandler);
    black.setOnAction(MEHandler);
    green.setOnAction(MEHandler);
    skyBlue.setOnAction(MEHandler);
    steiner.setOnAction(MEHandler);
    analyzer.setOnAction(MEHandler);
    generator.setOnAction(MEHandler);
      
    rootNode.setTop(mb);
    return  rootNode;
  }

  public void runSteiner() {
    try {
      FileIO.writePoints(getPoints());
      Process process = Runtime.getRuntime().exec("./flute-run.sh");
      
      while(process.isAlive()) {
        Thread.sleep(50);
      }
      
      ArrayList<Point> sPoints = FileIO.readPoints("./steiner-points.txt");
      ArrayList<Point> mstEdges = FileIO.readPoints("./mst-edges.txt");

      for(int i = 0; i < mstEdges.size(); i += 2) {
        Point start = mstEdges.get(i);
        Point end = mstEdges.get(i+1);
                
        start.x *= CELL_WIDTH;
        start.y *= CELL_HEIGHT;
        end.x *= CELL_WIDTH;
        end.y *= CELL_HEIGHT;
                
        if(start.x == end.x) {
          if(start.y == end.y)
            continue;
          root.getChildren().add(new Line(start.x, start.y, end.x, end.y));
        } else {
          if(start.y == end.y)
            root.getChildren().add(new Line(start.x, start.y, end.x, end.y));
          else {
            root.getChildren().add(new Line(start.x, start.y, start.x, end.y));
            root.getChildren().add(new Line(end.x, end.y, start.x, end.y));
          }
        }
      }

      for(Point sp: sPoints)
        ((Circle)root.getChildren().get( sp.y * COLUMN_NUMBER + sp.x)).setFill(Color.RED);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void runGenerator() {
    FlowPane root = new FlowPane();
    Stage stage = new Stage();
    Scene scene = new Scene(root, 350, 100);
    Label label = new Label("Vertices number:");
    Spinner<Integer> spinner = new Spinner<Integer>(1, COLUMN_NUMBER * ROW_NUMBER, 120);
    Button generateButton = new Button("Generate");
      
    generateButton.setOnAction((event) -> {
        Integer numOfVertices = spinner.getValueFactory().getValue();
        Set<String> points = new HashSet();
        
        for(int i = 0; i < numOfVertices; ++i) {
          int randomX = 0 + (int) (Math.random() * COLUMN_NUMBER);
          int randomY = 0 + (int) (Math.random() * ROW_NUMBER);
          String point = randomX + " " + randomY;
          
          if(points.contains(point))
            --i;
          else
            points.add(point);
        }

        cleanGrid();
        FileIO.writePoints(points);
        
        for(Point p: FileIO.readPoints())
          addVertex(p.x, p.y);

        stage.close();
      });
    
    generateButton.setTranslateX(120);
    generateButton.setTranslateY(10);
        
    root.setHgap(30);
    root.setVgap(10);
    root.setPadding(new Insets(10));
    root.getChildren().addAll(label, spinner, generateButton);

    stage.setTitle("Vertex Generator");
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();
  }

  public void runAnalyzer() {
    FlowPane root = new FlowPane();
    Stage stage = new Stage();
    Scene scene = new Scene(root, 900, 450);
    Label range = new Label("Range:");
    Label step = new Label("Step:");
    
    Spinner<Integer> rangeSpinner = new Spinner<Integer>(2, COLUMN_NUMBER * ROW_NUMBER, 2);
    Spinner<Integer> stepSpinner = new Spinner<Integer>(3, COLUMN_NUMBER * ROW_NUMBER, 3);
    Button analyzeButton = new Button("Analyze");
    
    analyzeButton.setOnAction((event) -> {
        // Integer numOfVertices = spinner.getValueFactory().getValue();
        // Set<String> points = new HashSet();
        
        // for(int i = 0; i < numOfVertices; ++i) {
        //   int randomX = 0 + (int) (Math.random() * COLUMN_NUMBER);
        //   int randomY = 0 + (int) (Math.random() * ROW_NUMBER);
        //   String point = randomX + " " + randomY;
          
        //   if(points.contains(point))
        //     --i;
        //   else
        //     points.add(point);
        // }

        // cleanGrid();
        // FileIO.writePoints(points);
        
        // for(Point p: FileIO.readPoints())
        //   addVertex(p.x, p.y);

        stage.close();
      });

    analyzeButton.setTranslateX(120);
    analyzeButton.setTranslateY(250);
    
    range.setTranslateX(565);
    range.setTranslateY(150);
    rangeSpinner.setTranslateX(565);
    rangeSpinner.setTranslateY(150);
    
    step.setTranslateX(300);
    step.setTranslateY(200); 
    stepSpinner.setTranslateX(300);
    stepSpinner.setTranslateY(200);


    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();       
    LineChart<String,Number> lineChart = new LineChart<String,Number>(xAxis,yAxis);
                
    //lineChart.setTitle("Time Monitoring");
                                
    XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();
        
    series.getData().add(new XYChart.Data<String,Number>("Jan", 23));
    series.getData().add(new XYChart.Data<String,Number>("Feb", 14));
    series.getData().add(new XYChart.Data<String,Number>("Mar", 15));
    series.getData().add(new XYChart.Data<String,Number>("Apr", 24));
    series.getData().add(new XYChart.Data<String,Number>("May", 34));
    series.getData().add(new XYChart.Data<String,Number>("Jun", 36));
    series.getData().add(new XYChart.Data<String,Number>("Jul", 22));
    series.getData().add(new XYChart.Data<String,Number>("Aug", 45));
    series.getData().add(new XYChart.Data<String,Number>("Sep", 43));
    series.getData().add(new XYChart.Data<String,Number>("Oct", 17));
    series.getData().add(new XYChart.Data<String,Number>("Nov", 29));
    series.getData().add(new XYChart.Data<String,Number>("Dec", 25));
    series.setName("Time Monitoring");
    lineChart.getData().add(series);
       
    root.setHgap(30);
    root.setVgap(5);
    root.setPadding(new Insets(10));
    root.getChildren().addAll(range, rangeSpinner, step, stepSpinner, analyzeButton, lineChart);

    stage.setTitle("Analyzer");
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();
  }
  
  public ArrayList<Point> getPoints() {
    ArrayList<Point> points = new ArrayList<>();
    
    for(int y = 0; y < verticies.length; ++y) 
      if(!verticies[y].isEmpty())
        for(Integer x: verticies[y])
          points.add(new Point(x, y));

    return points;
  }
  
  public void addVertex(int x, int y) {
    verticies[y].add(x);
    ((Circle)root.getChildren().get( y * COLUMN_NUMBER + x)).setFill(Color.BLACK);
  }

  public void cleanGrid() {
    ObservableList<Node> children = root.getChildren();
        
    for(int y = 0; y < verticies.length; ++y) {
      for(Integer x: verticies[y])
        ((Circle)children.get(y * COLUMN_NUMBER + (int)x)).setFill(Color.WHITE);
      verticies[y].clear();
    }

    // recoloring striner points white 
    for(Point p: FileIO.readPoints("./steiner-points.txt"))
      ((Circle)children.get(p.y * COLUMN_NUMBER + p.x)).setFill(Color.WHITE);
        
    // case when contains circles and something else -> remove somthing
    if(children.size() > ROW_NUMBER * COLUMN_NUMBER)
      children.remove(ROW_NUMBER * COLUMN_NUMBER, children.size());
        
    FileIO.cleanFile();
    FileIO.cleanFile("./steiner-points.txt");
    FileIO.cleanFile("./mst-edges.txt");
  }
  
  public double getWindowHeight() {
    return 900;
  }

  public double getWindowWidth() {
    return 1600;
  }
  
}
