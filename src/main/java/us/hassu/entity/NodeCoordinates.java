package us.hassu.entity;

import lombok.Data;

import java.util.ArrayList;

@Data
public class NodeCoordinates extends ArrayList<Integer> {
    public NodeCoordinates() {
        super(2);
    }

    public NodeCoordinates(int row, int col) {
        super(2);
        add(row);
        add(col);
    }
}
