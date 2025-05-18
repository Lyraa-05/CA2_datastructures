package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.util.List;
import java.util.stream.Collectors;

public class RouteFinderController {

    @FXML
    private ComboBox<String> startStationCombo;

    @FXML
    private ComboBox<String> destinationStationCombo;

    @FXML
    private Button findRouteButton;

    private SubwayGraph graph;

    public void setSubwayGraph(SubwayGraph graph) {
        this.graph = graph;
        loadStationsIntoUI();
    }

    private void loadStationsIntoUI() {
        List<String> stations = graph.getAllStations()
                .stream()
                .map(Station::getName)
                .sorted()
                .collect(Collectors.toList());

        startStationCombo.getItems().addAll(stations);
        destinationStationCombo.getItems().addAll(stations);
    }

    @FXML
    public void onFindRouteClicked() {
        String start = startStationCombo.getValue();
        String destination = destinationStationCombo.getValue();

        if (start == null || destination == null) {
            System.out.println("Please select both start and destination stations.");
            return;
        }

        System.out.println("Finding routes from " + start + " to " + destination);

        // TODO: Add route-finding logic here
    }
}