import graph.Grid;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.application.Application;
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

public class Main  extends Application {

    private void init(Stage stage) throws InterruptedException {

        Grid grid = new Grid();
        Parent zoomPane =  grid.createZoomPane();

        VBox layout = new VBox();
        layout.getChildren().setAll(zoomPane);
        VBox.setVgrow(zoomPane, Priority.ALWAYS);

        Scene scene = new Scene(layout, grid.getWindowWidth(), grid.getWindowHeight());
        scene.setFill(Color.SKYBLUE);
        
        stage.setScene(scene);
        stage.setTitle("Steiner tree calculator");
        stage.setResizable(false);
        stage.show();
    }
  
    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
