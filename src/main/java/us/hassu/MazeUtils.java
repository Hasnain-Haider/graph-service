package us.hassu;



import us.hassu.entity.maze.Maze;
import us.hassu.entity.maze.MazeNode;
import us.hassu.graphs.Pair;
import us.hassu.graphs.graph.Edge;

import java.util.*;

public class MazeUtils {
    public String drawMaze(Maze maze) {
        return drawMaze(maze, null);
    }

    public String drawMaze(Maze maze, Pair<Integer, Integer> marker) {
        return drawMaze(
                maze.getGrid(),
                maze.getHeight(),
                maze.getWidth(),
                maze.getEntrance(),
                maze.getExit(),
                maze.isTopEntrance(),
                marker
        );
    }

    public Map<String, Set<String>> asIdMap(Maze maze) {
        Map<String, Set<String>> idMap = new HashMap<>();
        for (Map.Entry<MazeNode, List<Edge<MazeNode>>> entry: maze.entrySet()) {
            MazeNode node = entry.getKey();
            List<Edge<MazeNode>> edges = entry.getValue();
            String currentId = node.getId();
            idMap.computeIfAbsent(currentId, k -> new HashSet<>());
            for (Edge<MazeNode> edge: edges) {
                idMap.get(currentId).add(edge.getTo().getId());
            }
        }
        return idMap;
    }


    String drawMaze(List<List<MazeNode>> grid, int height, int width,
                    MazeNode entrance, MazeNode exit, boolean isTopEntrance, Pair<Integer, Integer> marker) {
        StringBuilder mazeRepresentation = new StringBuilder();

        // Draw the top boundary
        for (int col = 0; col < width; col++) {
            if (isTopEntrance && col == entrance.getCol()) {
                mazeRepresentation.append("+   ");
            } else {
                mazeRepresentation.append("+---");
            }
        }
        mazeRepresentation.append("+\n");

        for (int row = 0; row < height; row++) {
            StringBuilder topRow = new StringBuilder("|");
            StringBuilder bottomRow = new StringBuilder("+");

            for (int col = 0; col < width; col++) {
                MazeNode cell = grid.get(row).get(col);
                Set<MazeNode.Boundary> boundaries = cell.getBoundaries();

                // Check if this is the entrance or exit
                if (marker != null && row == marker.getFirst() && col == marker.getSecond()) {
                    topRow.append(" M ");
                } else if (row == entrance.getRow() && col == entrance.getCol()) {
                    topRow.append(" E ");
                } else if (row == exit.getRow() && col == exit.getCol()) {
                    topRow.append(" X ");
                } else {
                    topRow.append("   ");
                }

                if (boundaries.contains(MazeNode.Boundary.RIGHT)) {
                    topRow.append("|");
                } else {
                    topRow.append(" ");
                }

                if (boundaries.contains(MazeNode.Boundary.BOTTOM)) {
                    bottomRow.append("---+");
                } else {
                    bottomRow.append("   +");
                }
            }

            mazeRepresentation.append(topRow).append("\n");
            mazeRepresentation.append(bottomRow).append("\n");
        }

        return mazeRepresentation.toString();
    }
}
