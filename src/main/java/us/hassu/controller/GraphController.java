package us.hassu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import us.hassu.entity.ResponseMaze;
import us.hassu.entity.ResponseMazeNode;
import us.hassu.exception.RateLimitExceededException;
import us.hassu.graphs.graph.Graph;
import us.hassu.graphs.graph.Grid;
import us.hassu.graphs.maze.Maze;
import us.hassu.graphs.maze.MazeNode;
import us.hassu.service.GraphService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

//put in many origins just to get the CORS error to go away
@CrossOrigin(origins = {"*", "localhost:80"})
@RestController
public class GraphController {
    @Autowired
    GraphService graphService;
    @Value("${api.generatemaze.max.width:100}")
    private int MAX_WIDTH;

    @Value("${api.generatemaze.max.height:100}")
    private int MAX_HEIGHT;

    @Value("${api.rpm.max:10}")
    private int MAX_RPM;

    private PriorityQueue<Instant> accessTimes = new PriorityQueue<>();

    @GetMapping("/generatemaze")
    public ResponseEntity<ResponseMaze> generateMaze(@RequestParam(required = false) Integer height, @RequestParam(required = false) Integer width) {
        if (!shouldProcessRequest()) {
            throw new RateLimitExceededException("Too many requests");
        }

        if (height != null) {
            height = Math.min(height, MAX_HEIGHT);
        }

        if (width != null) {
            width = Math.min(width, MAX_WIDTH);
        }

        Maze maze = graphService.generateMaze(height, width);
        Grid grid = maze.getGrid();

        List<List<ResponseMazeNode>> gridList = new ArrayList<>();
        for (List<MazeNode> nodes: grid){
            List<ResponseMazeNode> responseGridNodeList = new ArrayList<>();
            gridList.add(responseGridNodeList);
            for (MazeNode node: nodes) {
                responseGridNodeList.add(getRgnFromNode(node));
            }
        }
        return new ResponseEntity<>(new ResponseMaze(gridList, grid.size(), grid.get(0).size(), maze.getStart(), maze.getEnd()), HttpStatus.OK);
//        return new ResponseMaze(gridList, grid.size(), grid.get(0).size(), maze.getStart(), maze.getEnd());
    }


    private ResponseMazeNode getRgnFromNode(MazeNode node) {
        List<String> responseGridNodeBoundaries = new ArrayList<>();
        Set<MazeNode.Boundary> boundaries = node.getBoundaries();
        for (MazeNode.Boundary boundary: boundaries) {
            if (boundary == MazeNode.Boundary.BOTTOM) {
                responseGridNodeBoundaries.add("B");
            } else if (boundary == MazeNode.Boundary.RIGHT) {
                responseGridNodeBoundaries.add("R");
            }
        }
        return new ResponseMazeNode(responseGridNodeBoundaries);
    }

    synchronized boolean shouldProcessRequest() {
        Instant now = Instant.now();
        Instant oneMinuteAgo = now.minus(1, ChronoUnit.MINUTES);
        while (!accessTimes.isEmpty() && accessTimes.peek().isBefore(oneMinuteAgo)) {
            accessTimes.poll();
        }
        if (accessTimes.size() < MAX_RPM) {
            accessTimes.add(now);
            return true;
        } else {
            return false;
        }
    }
}
