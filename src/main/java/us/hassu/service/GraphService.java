package us.hassu.service;

import org.w3c.dom.Node;
import us.hassu.entity.NodeCoordinates;
import us.hassu.entity.ResponseMaze;
import us.hassu.entity.ResponseMazeNode;
import us.hassu.graphs.maze.Grid;
import us.hassu.graphs.maze.Maze;
import us.hassu.graphs.maze.MazeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GraphService {
    private int DEFAULT_HEIGHT = 10;
    private int DEFAULT_WIDTH = 10;

    private volatile Maze lastMaze;

    public GraphService() {
    }

    public ResponseMaze generateMaze(Integer height, Integer width) {
        Maze.Builder builder = new Maze.Builder();
        Maze maze = builder
                .height(Optional.ofNullable(height).orElse(DEFAULT_HEIGHT))
                .width(Optional.ofNullable(width).orElse(DEFAULT_WIDTH))
                .build();
        setLastMaze(maze);
        return new ResponseMaze(maze);
    }

    ResponseMazeNode getResponseMazeNode(MazeNode node) {
        List<String> responseGridNodeBoundaries = new ArrayList<>();
        Set<MazeNode.Boundary> boundaries = node.getBoundaries();
        for (MazeNode.Boundary boundary: boundaries) {
            if (boundary == MazeNode.Boundary.BOTTOM) {
                responseGridNodeBoundaries.add("B");
            } else if (boundary == MazeNode.Boundary.RIGHT) {
                responseGridNodeBoundaries.add("R");
            }
        }
        return new ResponseMazeNode(node.getId(), responseGridNodeBoundaries);
    }

    synchronized void setLastMaze(Maze maze) {
        this.lastMaze = maze;
    }

    synchronized Maze getLastMaze() {
        return lastMaze;
    }
}
