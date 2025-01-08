package us.hassu.entity;

import lombok.Data;

import java.util.List;

@Data
public class ResponseMazeNode {

    List<String> boundaries;

    String id;

    public ResponseMazeNode(String id, List<String> boundaries) {
        this.id = id;
        this.boundaries = boundaries;
    }
}
