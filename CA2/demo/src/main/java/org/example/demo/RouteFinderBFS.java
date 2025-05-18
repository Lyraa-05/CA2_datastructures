package org.example.demo;

import java.util.*;

public class RouteFinderBFS {
    private final SubwayGraph graph;
    private final Set<String> stationsToAvoid;
    private final List<String> waypoints;

    public RouteFinderBFS(SubwayGraph graph, Set<String> stationsToAvoid, List<String> waypoints) {
        this.graph = graph;
        this.stationsToAvoid = stationsToAvoid;
        this.waypoints = waypoints;
    }

    public List<Connection> findRoute(String start, String end) {
        if (waypoints.isEmpty()) {
            // Find route directly from start to end
            return findRouteBFS(start, end);
        } else {
            // Handle waypoints by finding routes between sequential pairs
            List<String> allPoints = new ArrayList<>();
            allPoints.add(start);
            allPoints.addAll(waypoints);
            allPoints.add(end);

            // Combine routes through all waypoints
            List<Connection> fullRoute = new ArrayList<>();
            for (int i = 0; i < allPoints.size() - 1; i++) {
                List<Connection> segment = findRouteBFS(allPoints.get(i), allPoints.get(i + 1));
                fullRoute.addAll(segment);
            }
            return fullRoute;
        }
    }

    private List<Connection> findRouteBFS(String start, String end) {
        Station startStation = graph.getStation(start);
        Station endStation = graph.getStation(end);

        if (startStation == null || endStation == null) {
            return Collections.emptyList();
        }

        Queue<Station> queue = new LinkedList<>();
        Map<String, Connection> previousConnection = new HashMap<>();
        Set<String> visited = new HashSet<>();

        // Add stations to avoid to visited set
        visited.addAll(stationsToAvoid);

        // Add start station to queue
        queue.add(startStation);
        visited.add(startStation.getName());

        while (!queue.isEmpty()) {
            Station current = queue.poll();

            // If we've reached the destination, reconstruct the path
            if (current.equals(endStation)) {
                return reconstructPath(startStation, endStation, previousConnection);
            }

            // Check all connections from current station
            for (Connection connection : current.getConnections()) {
                Station next = connection.getDestination();

                if (!visited.contains(next.getName())) {
                    queue.add(next);
                    visited.add(next.getName());
                    previousConnection.put(next.getName(), connection);
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
            current = graph.getStation(findSourceStation(connection));
        }

        return path;
    }

    private String findSourceStation(Connection connection) {
        // This finds the station that is the source of this connection
        // We need this because connections are stored with their destination
        for (Station station : graph.getAllStations()) {
            for (Connection conn : station.getConnections()) {
                if (conn.equals(connection)) {
                    return station.getName();
                }
            }
        }
        return null;
    }
}