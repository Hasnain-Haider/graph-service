package us.hassu.entity.maze;

import lombok.Getter;
import lombok.Setter;
import us.hassu.graphs.Pair;
import us.hassu.graphs.graph.AbstractGraph;
import us.hassu.graphs.graph.Edge;
import java.util.*;

import static us.hassu.entity.maze.MazeNode.Boundary.BOTTOM;
import static us.hassu.entity.maze.MazeNode.Boundary.RIGHT;



@Setter
@Getter
public class Maze extends AbstractGraph<MazeNode> {
    // grid is technically not needed, but it helps with mental model
    Grid<MazeNode> grid;
    int width;
    int height;
    MazeNode entrance;
    MazeNode exit;
    boolean isTopEntrance;
    long seed;

    public Maze(Maze maze) {
        this(maze, maze.getGrid(), maze.getHeight(), maze.getWidth(), maze.getEntrance(), maze.getExit(), maze.getSeed());
    }

    public Maze(HashMap<MazeNode, List<Edge<MazeNode>>> edges, Grid<MazeNode> grid,
                int width, int height, MazeNode entrance, MazeNode exit, long seed) {

        super(edges);
        this.grid = grid;
        this.width = width;
        this.height = height;
        this.entrance = entrance;
        this.exit = exit;
        this.seed = seed;
        this.isTopEntrance = height >= width;
        System.out.println("maze has top entrance: " + isTopEntrance + " maze height: "
                + height + " maze width: " + width + " entrance: " + entrance + " exit: " + exit);

    }

    @Override
    public void print() {
        printGrid();
        super.print();
    }

    public void printGrid() {
        System.out.println("Grid:");
        for (List<MazeNode> row : grid) {
            System.out.println(row);
        }
    }

    public static class Builder {
        private final int DEFAULT_WIDTH = 2;
        private final int DEFAULT_HEIGHT = 2;
        Integer width;
        Integer height;
        boolean createEdges;
        boolean debug;
        Long seed;

        public Builder randomSeed(Long seed) {
            this.seed = seed;
            return this;
        }

        // builder methods for setting property
        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder debug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder createEdges(boolean createEdges) {
            this.createEdges = createEdges;
            return this;
        }

        // build method to deal with outer class
        // to return outer instance
        public Maze build() {
            int mazeWidth = Optional.ofNullable(width).orElse(DEFAULT_WIDTH);
            int mazeHeight = Optional.ofNullable(height).orElse(DEFAULT_HEIGHT);
            long seed = Optional.ofNullable(this.seed).orElseGet(() -> new Random().nextLong());
            Random random = new Random(seed);

            if (mazeHeight < 2 || mazeWidth < 2) {
                throw new IllegalArgumentException("Maze must be at least 2x2");
            }
            Grid<MazeNode> nodes = createNodesGrid(mazeWidth, mazeHeight);
            HashMap<MazeNode, List<Edge<MazeNode>>> adjacencyList = new HashMap<>();
            Pair<MazeNode, MazeNode> ingressEgress = generateMaze(nodes, adjacencyList, mazeWidth, mazeHeight, random);
            MazeNode entrance = ingressEgress.getFirst();
            MazeNode exit = ingressEgress.getSecond();
            return new Maze(adjacencyList, nodes, mazeWidth, mazeHeight, entrance, exit, seed);
        }

        /*
            || y  - >  +
            x
            |
            v
            +

            (x,y)     (x, y+1)
            (x+1,y)  (x+1,y+1)

            i = x = row = limited by height
            j = y = col = limited by width
         */
        private Grid<MazeNode> createNodesGrid(int width, int height) {
            Grid<MazeNode> grid = new Grid(width);
            for (int i = 0; i < height; i++) {
                List<MazeNode> col = new ArrayList<>();
                grid.add(col);
                for (int j = 0; j < width; j++) {
                    // create row
                    col.add(new MazeNode(i + "," + j, i, j, EnumSet.allOf(MazeNode.Boundary.class)));
                }
            }

            return grid;
        }

        // depth first maze generation algorithm

        /**
         * Carve out a maze from the grid
         *
         * @param grid
         * @param width
         * @param height
         * @param adjacencyList easier to make new adjacency list than to remove nodesInitialAdjacencyList from existing one
         * @return ingress and egress nodes
         */
        private Pair<MazeNode, MazeNode> generateMaze(
                Grid<MazeNode> grid, HashMap<MazeNode, List<Edge<MazeNode>>> adjacencyList,
                int width, int height, Random random) {
            Set<MazeNode> visited = new HashSet<>();

            Stack<MazeNode> stack = new Stack<>();

            boolean topEntrance = height >= width;

            MazeNode start, exit;
            if (topEntrance) {
                start = grid.get(0).get(random.nextInt(width));
            } else {
                start = grid.get(random.nextInt(height)).get(0);
            }

            stack.push(start);
            while (!stack.isEmpty()) {
                MazeNode current = stack.peek();
                // 0 - 4 nodes
                List<MazeNode> unvisitedNeighbors = getAdjacentMazeNodes(grid, current, width, height).stream().filter(n -> !visited.contains(n)).toList();

                if (!unvisitedNeighbors.isEmpty()) {
                    // choose random neighbor
                    MazeNode neighbor = unvisitedNeighbors.get(random.nextInt(unvisitedNeighbors.size()));
                    stack.push(neighbor);
                    visited.add(neighbor);
                    removeBoundariesAndAddEdges(current, neighbor, adjacencyList);
                } else {
                    stack.pop();
                }
            }

            if (topEntrance) {
                // find opposite column
                exit = grid.get(height - 1).get(width - start.getCol() - 1);
                exit.removeBoundary(BOTTOM);
            } else {
                // find opposite row
                exit = grid.get(height - start.getRow() - 1).get(width - 1);
                exit.removeBoundary(RIGHT);
            }

            return Pair.of(start, exit);
        }

        List<MazeNode> getAdjacentMazeNodes(Grid<MazeNode> grid, MazeNode node, int width, int height) {
            int row = node.getRow();
            int col = node.getCol();

            List<MazeNode> adjacentNodes = new ArrayList<>();
            //up
            if (row > 0) {
                MazeNode up = grid.get(row - 1).get(col);
                adjacentNodes.add(up);
            }
            //down
            if (row < height - 1) {
                MazeNode down = grid.get(row + 1).get(col);
                adjacentNodes.add(down);
            }
            //left
            if (col > 0) {
                MazeNode left = grid.get(row).get(col - 1);
                adjacentNodes.add(left);
            }
            //right
            if (col < width - 1) {
                MazeNode right = grid.get(row).get(col + 1);
                adjacentNodes.add(right);
            }
            return adjacentNodes;
        }

        private void removeBoundariesAndAddEdges(MazeNode current, MazeNode neighbor, HashMap<MazeNode, List<Edge<MazeNode>>> adjacencyList) {
            int diffRow = neighbor.getRow() - current.getRow();
            int diffCol = neighbor.getCol() - current.getCol();

            if (Math.abs(diffRow) +  Math.abs(diffCol) > 1) {
                throw new IllegalStateException("Nodes are not adjacent");
            }

            if (diffRow == -1) {
                neighbor.removeBoundary(BOTTOM);
            } else if (diffRow == 1) {
                current.removeBoundary(BOTTOM);
            } else if (diffCol == -1) {
                neighbor.removeBoundary(RIGHT);
            } else if (diffCol == 1) {
                current.removeBoundary(RIGHT);
            }

            addEdge(adjacencyList, current, neighbor);
            addEdge(adjacencyList, neighbor, current);
        }

        void addEdge(HashMap<MazeNode, List<Edge<MazeNode>>> edges, MazeNode from, MazeNode to) {
            edges.computeIfAbsent(from, k -> new ArrayList<>()).add(new Edge<MazeNode>(from, to));
        }



        void debugLog(String s) {
            if (debug) {
                System.out.println(s);
            }
        }
    }
}
