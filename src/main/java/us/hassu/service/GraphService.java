package us.hassu.service;

import org.springframework.stereotype.Component;
import us.hassu.graphs.graph.Graph;
import us.hassu.graphs.graph.Grid;
import us.hassu.graphs.maze.Maze;

import java.util.Optional;

public class GraphService {
    private int DEFAULT_HEIGHT = 10;
    private int DEFAULT_WIDTH = 10;

    public GraphService() {
    }

    public Maze generateMaze(Integer height, Integer width) {
        Maze.Builder builder = new Maze.Builder();
        Maze maze = builder
                .height(Optional.ofNullable(height).orElse(DEFAULT_HEIGHT))
                .width(Optional.ofNullable(width).orElse(DEFAULT_WIDTH))
                .build();
        return maze;
    }
}
