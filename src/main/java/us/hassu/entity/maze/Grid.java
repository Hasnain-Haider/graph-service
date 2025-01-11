package us.hassu.entity.maze;

import us.hassu.graphs.graph.Node;

import java.util.ArrayList;
import java.util.List;

public class Grid<T extends Node> extends ArrayList<List<T>> {
    public Grid(int initialCapacity) {
        super(initialCapacity);
    }
}
