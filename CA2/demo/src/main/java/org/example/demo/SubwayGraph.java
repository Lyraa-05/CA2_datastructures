package org.example.demo;
import org.example.demo.Connection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SubwayGraph {
    private final Map<String, Station> stations =  new HashMap<>();

    public void addConnection(String start, String end, String line, String colour) {
        Station s1 = stations.computeIfAbsent(start, Station::new);
        Station s2 = stations.computeIfAbsent(end, Station::new);
        s1.addConnection(new Connection(s2,line , colour, 1)); //placeholder distance
        s2.addConnection(new Connection(s1, line, colour,1)); //bidirectional
    }
    public Station getStation(String name){
        return stations.get(name);
    }
    public Collection<Station> getAllStations(){
        return stations.values();
    }
}
