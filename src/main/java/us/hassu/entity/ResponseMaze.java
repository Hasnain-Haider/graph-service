package us.hassu.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import us.hassu.graphs.graph.Edge;
import us.hassu.graphs.maze.Grid;
import us.hassu.graphs.maze.Maze;
import us.hassu.graphs.maze.MazeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Data
public class ResponseMaze {
    private List<List<ResponseMazeNode>> grid;

    private int height;

    private int width;

    private NodeCoordinates entrance;

    private NodeCoordinates exit;

    @JsonSerialize(using = GraphSerializer.class)
    private HashMap<MazeNode, List<Edge<MazeNode>>> graph;

    public ResponseMaze(Maze maze) {
        this(maze, maze.getGrid(), maze.getHeight(), maze.getWidth(), maze.getEntrance(), maze.getExit());
    }

    public ResponseMaze(Maze maze, Grid<MazeNode> grid, int height, int width, MazeNode entrance, MazeNode exit) {
        this.graph = new Maze(maze, grid, height, width, entrance, exit);
        this.grid = gridToList(grid);
        this.height = height;
        this.width = width;

        this.entrance = new NodeCoordinates(entrance.getRow(), entrance.getCol());
        this.exit = new NodeCoordinates(exit.getRow(), exit.getCol());
    }

    List<List<ResponseMazeNode>> gridToList(Grid<MazeNode> grid) {
        List<List<ResponseMazeNode>> gridList = new ArrayList<>();
        for (List<MazeNode> nodes: grid){
            List<ResponseMazeNode> responseGridNodeList = new ArrayList<>();
            gridList.add(responseGridNodeList);
            for (MazeNode node: nodes) {
                responseGridNodeList.add(getResponseMazeNode(node));
            }
        }
        return gridList;
    }

    ResponseMazeNode getResponseMazeNode(MazeNode node) {
        List<String> responseGridNodeBoundaries = new ArrayList<>();
        Set<MazeNode.Boundary> boundaries = node.getBoundaries();
        for (MazeNode.Boundary boundary: boundaries) {
            if (boundary == MazeNode.Boundary.BOTTOM) {
                responseGridNodeBoundaries.add(MazeNode.Boundary.BOTTOM.name().toLowerCase());
            } else if (boundary == MazeNode.Boundary.RIGHT) {
                responseGridNodeBoundaries.add(MazeNode.Boundary.RIGHT.name().toLowerCase());
            }
        }
        return new ResponseMazeNode(node.getId(), responseGridNodeBoundaries);
    }
}
