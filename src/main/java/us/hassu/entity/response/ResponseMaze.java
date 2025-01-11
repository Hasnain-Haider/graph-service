package us.hassu.entity.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import us.hassu.entity.GraphSerializer;
import us.hassu.entity.NodeCoordinates;
import us.hassu.entity.ResponseMazeNode;
import us.hassu.entity.maze.Grid;
import us.hassu.entity.maze.Maze;
import us.hassu.entity.maze.MazeNode;
import us.hassu.graphs.graph.Edge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class ResponseMaze {
    private List<List<ResponseMazeNode>> grid;

    private int height;

    private int width;

    private long seed;

    private NodeCoordinates entrance;

    private NodeCoordinates exit;

    @JsonSerialize(using = GraphSerializer.class)
    private HashMap<MazeNode, List<Edge<MazeNode>>> graph;

    public ResponseMaze(Maze maze) {
        this(maze, maze.getGrid(), maze.getWidth(), maze.getHeight(), maze.getEntrance(), maze.getExit(), maze.getSeed());
    }

    public ResponseMaze(Maze maze, Grid<MazeNode> grid, int width, int height, MazeNode entrance, MazeNode exit, long seed) {
        this.graph = new Maze(maze, grid, width, height, entrance, exit, seed);
        this.grid = convertGridToList(grid);
        this.height = height;
        this.width = width;
        this.seed = seed;

        this.entrance = new NodeCoordinates(entrance.getRow(), entrance.getCol());
        this.exit = new NodeCoordinates(exit.getRow(), exit.getCol());
    }

    List<List<ResponseMazeNode>> convertGridToList(Grid<MazeNode> grid) {
        List<List<ResponseMazeNode>> gridList = new ArrayList<>();
        for (List<MazeNode> nodes: grid) {
            List<ResponseMazeNode> responseGridNodeList = new ArrayList<>();
            gridList.add(responseGridNodeList);
            for (MazeNode node: nodes) {
                responseGridNodeList.add(createResponseMazeNode(node));
            }
        }
        return gridList;
    }

    ResponseMazeNode createResponseMazeNode(MazeNode node) {
        List<String> responseGridNodeBoundaries = new ArrayList<>();

        for (MazeNode.Boundary boundary: node.getBoundaries()) {
            responseGridNodeBoundaries.add(boundary.name().toLowerCase());
        }

        return new ResponseMazeNode(node.getId(), responseGridNodeBoundaries);
    }
}
