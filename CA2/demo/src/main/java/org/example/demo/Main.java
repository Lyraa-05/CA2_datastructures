package org.example.demo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Load FXML
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("routerfinder.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Load the graph from resources
        SubwayGraph graph = GraphLoader.loadGraph("/vienna_subway.csv");

        // Get controller and pass the graph
        Controller controller = fxmlLoader.getController();
        controller.setSubwayGraph(graph);

        // Set up and show the stage
        stage.setTitle("Vienna U-Bahn Route Finder");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(); // launches JavaFX application
    }
}