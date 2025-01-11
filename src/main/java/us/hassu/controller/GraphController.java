package us.hassu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.hassu.entity.request.RequestSolveMaze;
import us.hassu.entity.response.ResponseMaze;

import us.hassu.service.GraphService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

//put in many origins just to get the CORS error to go away
@CrossOrigin(origins = {"*", "localhost:80", "localhost:3000"})
@RestController
@RequestMapping("/maze")
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

    @GetMapping("/generate")
    public ResponseEntity<ResponseMaze> generateMaze(@RequestParam(required = false) Integer height,
                                                     @RequestParam(required = false) Integer width) {
//        if (!shouldProcessRequest()) {
//            throw new RateLimitExceededException("Too many requests");
//        }

        if (height != null) {
            height = Math.min(height, MAX_HEIGHT);
        }

        if (width != null) {
            width = Math.min(width, MAX_WIDTH);
        }

        ResponseMaze maze = graphService.generateMaze(height, width);
        return new ResponseEntity<>(maze, HttpStatus.OK);
    }

    @PostMapping("/solve")
    public ResponseEntity<Boolean> solveMaze(@RequestBody RequestSolveMaze requestSolveMaze) {
        boolean solvedMaze = graphService.solveMaze(requestSolveMaze);
        System.out.println("solvedMaze?:" + solvedMaze);
        return new ResponseEntity<>(solvedMaze, HttpStatus.OK);
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
