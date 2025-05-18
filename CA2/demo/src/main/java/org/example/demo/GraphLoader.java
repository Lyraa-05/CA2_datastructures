package org.example.demo;

import java.io.*;
import java.util.*;

public class GraphLoader {
    public static SubwayGraph loadGraph(String csvFilePath) throws IOException {
        SubwayGraph graph = new SubwayGraph();

        InputStream inputStream = GraphLoader.class.getResourceAsStream(csvFilePath);
        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found: " + csvFilePath);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        reader.readLine(); // skip header
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\t");
            if (parts.length == 4) {
                graph.addConnection(parts[0], parts[1], parts[2], parts[3]);
            }
        }

        return graph;
    }
}
