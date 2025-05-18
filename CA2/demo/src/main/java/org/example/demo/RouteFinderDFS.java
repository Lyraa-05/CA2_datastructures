package org.example.demo;

import java.util.*;

public class RouteFinderDFS {
    private final SubwayGraph graph;
    private final Set<String> stationsToAvoid;
    private final List<String> waypoints;
    private final List<List<Connection>> allRoutes;

    public RouteFinderDFS(SubwayGraph graph, Set<String> stationsToAvoid, List<String> waypoints) {
        this.graph = graph;
        this.stationsToAvoid = stationsToAvoid;
        this.waypoints = waypoints;
        this.allRoutes = new ArrayList<>();
    }

    public List<List<Connection>> findRoutes(String start, String end) {
        allRoutes.clear();

        if (waypoints.isEmpty()) {
            // Find routes directly from start to end
            findRoutesHelper(graph.getStation(start), graph.getStation(end), new ArrayList<>(), new HashSet<>());
        } else {
            // Handle waypoints by finding routes between sequential pairs
            List<String> allPoints = new ArrayList<>();
            allPoints.add(start);
            allPoints.addAll(waypoints);
            allPoints.add(end);

            // Find routes through all waypoints
            List<List<Connection>> waypointRoutes = new ArrayList<>();
            findWaypointRoutes(allPoints, 0, waypointRoutes, new ArrayList<>());
        }

        return allRoutes;
    }

    private void findRoutesHelper(Station current, Station destination, List<Connection> path, Set<String> visited) {
        // Add current station to visited
        visited.add(current.getName());

        // If we've reached the destination, add the path to our results
        if (current.equals(destination)) {
            allRoutes.add(new ArrayList<>(path));
            visited.remove(current.getName()); // Backtrack
            return;
        }

        // Try all possible connections from the current station
        for (Connection connection : current.getConnections()) {
            Station next = connection.getDestination();

            // Skip if we've already visited this station or if it's in the avoid list
            if (visited.contains(next.getName()) || stationsToAvoid.contains(next.getName())) {
                continue;
            }

            // Add this connection to our path
            path.add(connection);

            // Recursively find routes from the next station
            findRoutesHelper(next, destination, path, visited);

            // Backtrack: remove the last connection
            path.remove(path.size() - 1);
        }

        // Backtrack: remove current station from visited
        visited.remove(current.getName());
    }

    private void findWaypointRoutes(List<String> points, int currentIdx, List<List<Connection>> currentRoutes, List<Connection> accumulatedPath) {
        if (currentIdx == points.size() - 1) {
            // We've completed a full path through all waypoints
            allRoutes.add(new ArrayList<>(accumulatedPath));
            return;
        }

        // Find routes between current pair of points
        String start = points.get(currentIdx);
        String end = points.get(currentIdx + 1);

        RouteFinderDFS segmentFinder = new RouteFinderDFS(graph, stationsToAvoid, Collections.emptyList());
        List<List<Connection>> segmentRoutes = segmentFinder.findRoutes(start, end);

        // For each route found between this pair of points
        for (List<Connection> route : segmentRoutes) {
            // Add this segment to our accumulated path
            accumulatedPath.addAll(route);

            // Continue to the next segment
            findWaypointRoutes(points, currentIdx + 1, currentRoutes, accumulatedPath);

            // Backtrack by removing this segment
            for (int i = 0; i < route.size(); i++) {
                accumulatedPath.remove(accumulatedPath.size() - 1);
            }
        }
    }
}