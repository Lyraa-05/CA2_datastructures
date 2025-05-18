package org.example.demo;
import org.example.demo.Connection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SubwayGraph {
    private final Map<String, Station> stations =  new HashMap<>();

    public void addConnection(String start, String end, String line, String colour, double distance) {
        Station s1 = stations.computeIfAbsent(start, Station::new);
        Station s2 = stations.computeIfAbsent(end, Station::new);
        s1.addConnection(new Connection(s2, line, colour, distance));
        s2.addConnection(new Connection(s1, line, colour, distance)); // bidirectional
    }

    // Overload for backward compatibility
    public void addConnection(String start, String end, String line, String colour) {
        // Use approximate distance data or calculate from coordinates
        // For now, using placeholder of 1.0
        addConnection(start, end, line, colour, 1.0);
    }
    public Station getStation(String name){
        return stations.get(name);
    }
    public Collection<Station> getAllStations(){
        return stations.values();
    }
}
