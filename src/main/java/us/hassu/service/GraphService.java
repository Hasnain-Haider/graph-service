package us.hassu.service;

import org.springframework.beans.factory.annotation.Autowired;
import us.hassu.MazeUtils;
import us.hassu.entity.ResponseMazeNode;
import us.hassu.entity.maze.Maze;
import us.hassu.entity.maze.MazeNode;
import us.hassu.entity.request.RequestSolveMaze;
import us.hassu.entity.response.ResponseMaze;
import us.hassu.graphs.GraphUtils;
import us.hassu.graphs.Pair;
import us.hassu.graphs.trace.BfsTrace;

import java.util.*;
import java.util.stream.Collectors;

public class GraphService {
    private int DEFAULT_HEIGHT = 10;
    private int DEFAULT_WIDTH = 10;
    private volatile Maze lastMaze;

    @Autowired
    GraphUtils<MazeNode> graphUtils;

    @Autowired
    MazeUtils mazeUtils;

    public GraphService() {
    }

    public ResponseMaze generateMaze(Integer height, Integer width) {
        Maze.Builder builder = new Maze.Builder();
        long seed = new Random().nextLong();
        Maze maze = builder
                .height(Optional.ofNullable(height).orElse(DEFAULT_HEIGHT))
                .width(Optional.ofNullable(width).orElse(DEFAULT_WIDTH))
                .createEdges(true)
                .randomSeed(seed)
                .build();
        setLastMaze(maze);
        return new ResponseMaze(maze);
    }


    /*
    * TODO: also other paths to the exit that are equally short as the bfs path should be accepted
     */
    public boolean solveMaze(RequestSolveMaze attempt) {
        Maze mazeToSolve = getLastMaze();

        if (mazeToSolve == null) return false;

        BfsTrace<MazeNode> trace = graphUtils.bfsTrace(mazeToSolve, mazeToSolve.getEntrance(), mazeToSolve.getExit());
        if (!trace.isFoundPath()) {
            throw new RuntimeException("No path found");
        }

        Set<MazeNode> submittedAttempt = attempt.getNodes();
        List<String> submittedAttemptIds = submittedAttempt.stream().map(MazeNode::getId).collect(Collectors.toList());
        List<String> bfsIds = trace.getPath().stream().map(MazeNode::getId).collect(Collectors.toList());

        System.out.println("bfsIds: " + bfsIds);
        System.out.println("submittedAttemptIds: " + submittedAttemptIds);
        String entranceId = mazeToSolve.getEntrance().getId();
        if (!bfsIds.contains(entranceId)) {
            System.out.println("entranceId: " + entranceId + " bfIds.get(0): " + bfsIds.get(0));
            return false;
        }

        Map<String, Set<String>> mazeIdMap = mazeUtils.asIdMap(mazeToSolve);
        System.out.println("mazeIdMap: " + mazeIdMap);
        if (submittedAttempt.size() != trace.getPath().size()) {
            return false;
        }

        String prev = entranceId;
        submittedAttemptIds.remove(prev);


        for (int i = 1; i < bfsIds.size(); i++) {
            String current = bfsIds.get(i);
            String[] coords = current.split(",");
            Integer row = Integer.parseInt(coords[0]);
            Integer col = Integer.parseInt(coords[1]);
            System.out.println(mazeUtils.drawMaze(mazeToSolve, Pair.of(row, col)));
            if (mazeIdMap.get(prev).contains(current)) {
                submittedAttemptIds.remove(current);
                prev = current;
            } else {
                System.out.println("node:" +prev + " does not have an edge to node: " + current);
                return false;
            }
        }
        System.out.println("submittedAttemptIds: " + submittedAttemptIds);
        return submittedAttemptIds.isEmpty();
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
