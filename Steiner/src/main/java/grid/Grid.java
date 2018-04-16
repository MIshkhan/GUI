package graph;

import javafx.scene.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

import javafx.scene.shape.*;
import javafx.scene.image.*;
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

import javafx.geometry.*;

import javafx.application.Platform;

import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import java.io.*;

public class Grid {

  private final double START_X = 0;
  private final double START_Y = 0;
  
  private final int ROW_NUMBER = 70;
  private final int COLUMN_NUMBER = 150;

  private final int CELL_WIDTH = 10;
  private final int CELL_HEIGHT = 10;

  private final double MIN_SCALE = 1; 
  private final double MAX_SCALE = 11; 
  private final double SCALE_DELTA = 1.1;
  
  private Group root = new Group();
  private StackPane zoomPane = null;

  private ArrayList<Integer>[] verticies = new ArrayList[ROW_NUMBER];
  private Text wirelength;
  
  public Grid() {
    wirelength = new Text("Wirelength: 0");
    wirelength.setFont(new Font(15));
    
    for(int i = 0; i < ROW_NUMBER; ++i) 
      verticies[i] = new ArrayList<Integer>();

    makeGrid();
    
    for(Tuple<Integer, Integer> p: FileIO.readPoints())
      addVertex(p.k, p.v);
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
    ScrollBar vScrol = new ScrollBar();
    vScrol.setOrientation(Orientation.VERTICAL);
    vScrol.valueProperty().addListener(new ChangeListener<Number>() {
        public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
          root.setTranslateY(-new_val.doubleValue());
        }});
    
    ScrollBar hScrol = new ScrollBar();
    hScrol.setOrientation(Orientation.HORIZONTAL);
    hScrol.valueProperty().addListener(new ChangeListener<Number>() {
        public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
          root.setTranslateX(-new_val.doubleValue());
        }});

    Slider slider = new Slider(1, 10.0, 1);
    
    zoomPane = new StackPane();
    zoomPane.setMaxSize(COLUMN_NUMBER * CELL_WIDTH, ROW_NUMBER * CELL_HEIGHT);
    zoomPane.setStyle("-fx-background-color: #669994");
    zoomPane.getChildren().add(root);
    
    zoomPane.setOnScroll(new EventHandler<ScrollEvent>() {
        @Override public void handle(ScrollEvent event) {
          event.consume();

          if (event.getDeltaY() == 0) 
            return;
          
          double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;
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
          double scale = 1.1 * slider.getValue();
          root.setScaleX(scale);
          root.setScaleY(scale);
        }
      });
    
    StackPane footer = new StackPane();
    HBox hBox = new HBox();
    hBox.setAlignment(Pos.TOP_RIGHT);
    hBox.setSpacing(20);
    hBox.getChildren().addAll(wirelength, slider);
    footer.setMargin(hBox, new Insets(20, 0, 0, 0));
    footer.setMargin(hScrol, new Insets(0, 0, 100, 0)); 
    footer.getChildren().addAll(hScrol, hBox);
      
    BorderPane mainWindow = new BorderPane();
    mainWindow.setTop(getMenuBar());
    mainWindow.setCenter(zoomPane);
    mainWindow.setRight(vScrol);
    mainWindow.setBottom(footer);
    
    return mainWindow;
  }

  public MenuBar getMenuBar() {
    MenuBar mb = new MenuBar();

    // Create the File menu.
    Menu fileMenu = new Menu("File");
    MenuItem open = new MenuItem("Open");
    MenuItem save = new MenuItem("Save");
    MenuItem clear = new MenuItem("Clear");
    MenuItem exit = new MenuItem("Exit");
    fileMenu.getItems().addAll(open, save, clear, new SeparatorMenuItem(), exit);

    mb.getMenus().add(fileMenu);

    Menu view = new Menu("View");
    Menu backgrounds = new Menu("Backgrounds");
    MenuItem skyBlue = new MenuItem("Sky-blue");
    MenuItem green = new MenuItem("Green");
    backgrounds.getItems().addAll(skyBlue, green);
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
          case "Open"     : /* for(Point p: FileIO.readPoints()) addVertex(p.k, p.v);*/ break;
          case "Clear"    : cleanGrid(); break;
          case "Steiner"  : runSteiner(); break;
          case "Generator": runGenerator(); break;
          case "Analyzer" : runAnalyzer(); break; 
          case "Exit"     : Platform.exit();
          }
        }
      };

    fileMenu.setOnAction(MEHandler);
    backgrounds.setOnAction(MEHandler);
    run.setOnAction(MEHandler);
    helpMenu.setOnAction(MEHandler);  
    
    return mb;
  }
  
  public void runSteiner() {
    try {
      FileIO.writePoints(getPoints());
      Process process = Runtime.getRuntime().exec("./flute-run.sh");
      
      while(process.isAlive()) {
        Thread.sleep(50);
      }
      
      ArrayList<Tuple<Integer, Integer>> sPoints = FileIO.readPoints("./steiner-points.txt");
      ArrayList<Tuple<Integer, Integer>> mstEdges = FileIO.readPoints("./mst-edges.txt");

      for(int i = 0; i < mstEdges.size(); i += 2) {
        Tuple<Integer, Integer> start = mstEdges.get(i);
        Tuple<Integer, Integer> end = mstEdges.get(i+1);
                
        start.k *= CELL_WIDTH;
        start.v *= CELL_HEIGHT;
        end.k *= CELL_WIDTH;
        end.v *= CELL_HEIGHT;
                
        if(start.k == end.k) {
          if(start.v == end.v)
            continue;
          root.getChildren().add(new Line(start.k, start.v, end.k, end.v));
        } else {
          if(start.v == end.v)
            root.getChildren().add(new Line(start.k, start.v, end.k, end.v));
          else {
            root.getChildren().add(new Line(start.k, start.v, start.k, end.v));
            root.getChildren().add(new Line(end.k, end.v, start.k, end.v));
          }
        }
      }

      for(Tuple<Integer, Integer> sp: sPoints) {
        Circle steinerPoint = ((Circle)root.getChildren().get( sp.v * COLUMN_NUMBER + sp.k)); 
        steinerPoint.setFill(Color.RED);
        steinerPoint.setOnMousePressed(new EventHandler<MouseEvent>() {
          public void handle(MouseEvent me) {
            if (me.isPrimaryButtonDown() && steinerPoint.getFill() == Color.WHITE) { //left click
              steinerPoint.setFill(Color.RED);
              verticies[(int)steinerPoint.getCenterY() / 10].add( (int)steinerPoint.getCenterX() / 10 );
            } else if (me.isSecondaryButtonDown() && steinerPoint.getFill() == Color.RED) { //right click
                steinerPoint.setFill(Color.WHITE);
                verticies[(int)steinerPoint.getCenterY() / 10].remove( new Integer((int)steinerPoint.getCenterX() / 10 ));
            }
          }
        });
      }
      
      java.util.List<Integer> wirelengths = FileIO.readWireLengths("./durations.txt");
      wirelength.setText("Wirelength: " + wirelengths.get(wirelengths.size()-1).toString());
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

    spinner.setEditable(true);
    
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
        
        for(Tuple<Integer, Integer> p: FileIO.readPoints())
          addVertex(p.k, p.v);

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
    int x  = 550;
    int dX = 150;
    int y  = 150;
    int dY = 33;
    
    Label rangeStartLabel = new Label("Range start:");
    rangeStartLabel.setTranslateX(x);
    rangeStartLabel.setTranslateY(y);

    Spinner<Integer> rangeStart = new Spinner<Integer>(2, 10000, 2);
    rangeStart.setTranslateX(x + dX);
    rangeStart.setTranslateY(y);
    rangeStart.setEditable(true);

    Label rangeEndLabel = new Label("Range end:");
    rangeEndLabel.setTranslateX(x);
    rangeEndLabel.setTranslateY(y + dY);

    Spinner<Integer> rangeEnd = new Spinner<Integer>(2, 10000, 2);
    rangeEnd.setTranslateX(x + dX);
    rangeEnd.setTranslateY(y + dY);
    rangeEnd.setEditable(true);

    Label step = new Label("Step:");
    step.setTranslateX(x);
    step.setTranslateY(y + 2 * dY);

    Spinner<Integer> stepSpinner = new Spinner<Integer>(10, 3000, 3);
    stepSpinner.setTranslateX(x + dX);
    stepSpinner.setTranslateY(y + 2 * dY);
    stepSpinner.setEditable(true);

    Button analyzeButton = new Button("Analyze");
    analyzeButton.setTranslateX(x + 1.32 * dX);
    analyzeButton.setTranslateY(y + 3 * dY);
    
    CategoryAxis xAxis = new CategoryAxis();
    xAxis.setLabel("Pins");
    NumberAxis yAxis = new NumberAxis();       
    yAxis.setLabel("Seconds");
    LineChart<String,Number> lineChart = new LineChart<String,Number>(xAxis,yAxis);
                                               
    XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();

    analyzeButton.setOnMousePressed(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent me) {
          FileIO.cleanFile("./durations.txt");
          series.getData().clear();
            
          Integer delta = stepSpinner.getValueFactory().getValue();
          Integer rEnd = rangeEnd.getValueFactory().getValue();
          Integer rStart = rangeStart.getValueFactory().getValue();

          try {
            for(; rStart <= rEnd; rStart += delta) {
              Process process = Runtime.getRuntime().exec("./flute-time.sh " + rStart);
              
              while(process.isAlive()) 
                Thread.sleep(50);
            }

            for(Tuple<Integer, Double> t: FileIO.readExecTimes("durations.txt"))
              series.getData().add(new XYChart.Data<String, Number>(t.k.toString(), t.v));
            
          } catch (Exception e) {
            e.printStackTrace();
            FileIO.cleanFile("./steiner-points.txt");
            FileIO.cleanFile("./mst-edges.txt");
          }
          FileIO.cleanFile("./steiner-points.txt");
          FileIO.cleanFile("./mst-edges.txt");
        }
      });
    
    series.setName("Time Monitoring");
    lineChart.getData().add(series);
    lineChart.setTranslateX(20);
    lineChart.setTranslateY(30);
    
    StackPane buttonsLayout = new StackPane();
    buttonsLayout.getChildren().addAll(rangeStartLabel, rangeEndLabel, rangeStart, rangeEnd, step, stepSpinner, analyzeButton);

    VBox chartLayout = new VBox();
    chartLayout.getChildren().add(lineChart);
    
    Pane window = new Pane();
    window.getChildren().addAll(chartLayout, buttonsLayout);
    
    Scene scene = new Scene(window, 900, 450);
    Stage stage = new Stage();
    stage.setTitle("Analyzer");
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();
  }
  
  public ArrayList<Tuple<Integer, Integer>> getPoints() {
    ArrayList<Tuple<Integer, Integer>> points = new ArrayList<>();
    
    for(int y = 0; y < verticies.length; ++y) 
      if(!verticies[y].isEmpty())
        for(Integer x: verticies[y])
          points.add(new Tuple<Integer, Integer>(x, y));

    return points;
  }
  
  public void addVertex(Integer x, Integer y) {
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
    for(Tuple<Integer, Integer> p: FileIO.readPoints("./steiner-points.txt"))
      ((Circle)children.get(p.v * COLUMN_NUMBER + p.k)).setFill(Color.WHITE);
        
    // case when contains circles and something else -> remove somthing
    if(children.size() > ROW_NUMBER * COLUMN_NUMBER)
      children.remove(ROW_NUMBER * COLUMN_NUMBER, children.size());
        
    FileIO.cleanFile("./vertices.txt");
    FileIO.cleanFile("./mst-edges.txt");
    FileIO.cleanFile("./steiner-points.txt");
    FileIO.cleanFile("./durations.txt");
    
    wirelength.setText("Wirelength: 0");
  }
  
  public double getWindowHeight() {
    return 775;
  }

  public double getWindowWidth() {
    return 1510;
  }
  
}
