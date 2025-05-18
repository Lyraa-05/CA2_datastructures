package org.example.demo;

import org.example.demo.Connection;
import java.util.ArrayList;
import java.util.List;

public class Station {
    private final String name;
    private final List<Connection> connections = new ArrayList<>();

    public Station(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
    public List<Connection> getConnections(){
        return connections;
    }
    public void addConnection(Connection connection){
        connections.add(connection);
    }
}
