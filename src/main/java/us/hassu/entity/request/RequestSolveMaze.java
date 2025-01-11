package us.hassu.entity.request;

import lombok.Getter;
import lombok.Setter;
import us.hassu.entity.maze.MazeNode;

import java.util.Map;
import java.util.Set;

@Getter @Setter
public class RequestSolveMaze {
    Set<MazeNode> nodes;
    Map<String, Set<String>> graph;
    Long seed;
}
