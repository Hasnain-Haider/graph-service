package us.hassu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import us.hassu.entity.ResponseMaze;
import us.hassu.entity.ResponseMazeNode;
import us.hassu.graphs.graph.Graph;
import us.hassu.graphs.graph.Grid;
import us.hassu.graphs.maze.Maze;
import us.hassu.graphs.maze.MazeNode;
import us.hassu.service.GraphService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//put in many origins just to get the CORS error to go away
@CrossOrigin(origins = {"*", "localhost:80"})
@RestController
public class GraphController {
    @Autowired
    GraphService graphService;

    @GetMapping("/generatemaze")
    public ResponseMaze generateMaze(@RequestParam(required = false) Integer height, @RequestParam(required = false) Integer width) {
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
        return new ResponseMaze(gridList, grid.size(), grid.get(0).size(), maze.getStart(), maze.getEnd());
    }

    @GetMapping("/mazetest")
    public Graph graph() {
        return graphService.generateMaze(null, null);
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
}
