package us.hassu.entity;

import lombok.Data;
import us.hassu.graphs.maze.MazeNode;

import java.util.List;

@Data
public class ResponseMaze {
    private List<List<ResponseMazeNode>> grid;

    private int height;

    private int width;

    private NodeCoordinates entrance;

    private NodeCoordinates exit;

    public ResponseMaze(List<List<ResponseMazeNode>> grid, int height, int width, MazeNode entrance, MazeNode exit) {
        this.grid = grid;
        this.height = height;
        this.width = width;

        this.entrance = new NodeCoordinates();
        this.entrance.add(entrance.getRow());
        this.entrance.add(entrance.getCol());

        this.exit = new NodeCoordinates();
        this.exit.add(exit.getRow());
        this.exit.add(exit.getCol());
    }
}
