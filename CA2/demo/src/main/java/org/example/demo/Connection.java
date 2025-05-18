package org.example.demo;

public class Connection {
    private final Station destination;
    private final String line;
    private final String colour;
    private final double distance; //leave as 1 for now, improve later

    public Connection(final Station destination, final String line, final String colour, final double distance) {
        this.destination = destination;
        this.line = line;
        this.colour = colour;
        this.distance = distance;
    }

    public Station getDestination(){
        return destination;
    }
    public String getLine(){
        return line;
    }
    public String getColour(){
        return colour;
    }
    public double getDistance(){
        return distance;
    }
}

