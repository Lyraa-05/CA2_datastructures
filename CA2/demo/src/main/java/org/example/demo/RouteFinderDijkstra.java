package org.example.demo;

import java.util.*;

public class RouteFinderDijkstra {
    private final SubwayGraph graph;
    private final Set<String> stationsToAvoid;
    private final List<String> waypoints;
    private final double lineChangePenalty;

    public RouteFinderDijkstra(SubwayGraph graph, Set<String> stationsToAvoid, List<String> waypoints, double lineChangePenalty) {
        this.graph = graph;
        this.stationsToAvoid = stationsToAvoid;
        this.waypoints = waypoints;
        this.lineChangePenalty = lineChangePenalty;
    }

    public List<Connection> findShortestRoute(String start, String end) {
        if (waypoints.isEmpty()) {
            // Find route directly from start to end
            return findDijkstraPath(start, end);
        } else {
            // Handle waypoints by finding routes between sequential pairs
            List<String> allPoints = new ArrayList<>();
            allPoints.add(start);
            allPoints.addAll(waypoints);
            allPoints.add(end);

            // Combine routes through all waypoints
            List<Connection> fullRoute = new ArrayList<>();
            for (int i = 0; i < allPoints.size() - 1; i++) {
                List<Connection> segment = findDijkstraPath(allPoints.get(i), allPoints.get(i + 1));
                fullRoute.addAll(segment);
            }
            return fullRoute;
        }
    }

    private List<Connection> findDijkstraPath(String start, String end) {
        Station startStation = graph.getStation(start);
        Station endStation = graph.getStation(end);

        if (startStation == null || endStation == null) {
            return Collections.emptyList();
        }

        // Priority queue ordered by distance
        PriorityQueue<NodeWithDistance> queue = new PriorityQueue<>(Comparator.comparingDouble(NodeWithDistance::getDistance));
        Map<String, Double> distance = new HashMap<>();
        Map<String, Connection> previousConnection = new HashMap<>();
        Map<String, String> previousLine = new HashMap<>();

        // Initialize distances to infinity for all stations
        for (Station station : graph.getAllStations()) {
            distance.put(station.getName(), Double.POSITIVE_INFINITY);
        }

        // Distance to start is 0
        distance.put(startStation.getName(), 0.0);
        queue.add(new NodeWithDistance(startStation.getName(), 0.0));

        while (!queue.isEmpty()) {
            NodeWithDistance current = queue.poll();
            String currentName = current.getName();

            // Skip stations to avoid
            if (stationsToAvoid.contains(currentName)) {
                continue;
            }

            // If we've reached the destination, reconstruct the path
            if (currentName.equals(endStation.getName())) {
                return reconstructPath(startStation, endStation, previousConnection);
            }

            // If we've found a worse path to the current node, skip it
            if (current.getDistance() > distance.get(currentName)) {
                continue;
            }

            Station currentStation = graph.getStation(currentName);
            String currentLine = previousLine.get(currentName);

            // Check all connections from current station
            for (Connection connection : currentStation.getConnections()) {
                Station next = connection.getDestination();
                String nextName = next.getName();

                // Skip stations to avoid
                if (stationsToAvoid.contains(nextName)) {
                    continue;
                }

                // Calculate new distance
                double edgeDistance = connection.getDistance();

                // Add line change penalty if applicable
                if (currentLine != null && !connection.getLine().equals(currentLine)) {
                    edgeDistance += lineChangePenalty;
                }

                double newDistance = distance.get(currentName) + edgeDistance;

                // If we've found a better path, update it
                if (newDistance < distance.get(nextName)) {
                    distance.put(nextName, newDistance);
                    previousConnection.put(nextName, connection);
                    previousLine.put(nextName, connection.getLine());
                    queue.add(new NodeWithDistance(nextName, newDistance));
                }
            }
        }

        // No path found
        return Collections.emptyList();
    }

    private List<Connection> reconstructPath(Station start, Station end, Map<String, Connection> previousConnection) {
        List<Connection> path = new ArrayList<>();
        Station current = end;

        while (!current.equals(start)) {
            Connection connection = previousConnection.get(current.getName());
            if (connection == null) {
                break; // No path exists
            }
            path.add(0, connection); // Add to front of list
            current = findSourceStation(connection);
        }

        return path;
    }

    private Station findSourceStation(Connection connection) {
        // This finds the station that is the source of this connection
        for (Station station : graph.getAllStations()) {
            for (Connection conn : station.getConnections()) {
                if (conn.equals(connection)) {
                    return station;
                }
            }
        }
        return null;
    }

    private static class NodeWithDistance {
        private final String name;
        private final double distance;

        public NodeWithDistance(String name, double distance) {
            this.name = name;
            this.distance = distance;
        }

        public String getName() {
            return name;
        }

        public double getDistance() {
            return distance;
        }
    }
}