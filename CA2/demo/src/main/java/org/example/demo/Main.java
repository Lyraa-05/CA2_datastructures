package org.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("routerfinder.fxml"));

        Scene scene = new Scene(loader.load());

        // Load graph
        SubwayGraph graph = GraphLoader.loadGraph("/vienna_subway.csv");

        // Pass graph to controller
        RouteFinderController controller = loader.getController();
        controller.setSubwayGraph(graph);

        stage.setTitle("Vienna U-Bahn Route Finder");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}