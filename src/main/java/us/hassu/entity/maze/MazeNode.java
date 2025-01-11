package us.hassu.entity.maze;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import us.hassu.graphs.graph.Node;

import java.util.EnumSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class MazeNode extends Node {
    @EqualsAndHashCode.Exclude
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Set<Boundary> boundaries;

    private int row;

    private int col;

    public MazeNode(String id) {
        super(id);
    }

    public MazeNode(String id, int row, int col, Set<Boundary> boundaries) {
        this(id);
        this.row = row;
        this.col = col;
        this.boundaries = boundaries;
    }

    public void removeBoundary(Boundary boundary) {
        boundaries.remove(boundary);
    }

    public void addBoundary(Boundary boundary) {
        boundaries.add(boundary);
    }

    public enum Boundary {BOTTOM, RIGHT}
}
