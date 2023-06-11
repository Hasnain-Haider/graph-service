package us.hassu.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ResponseMazeNode {

    @JsonProperty("bounds")
    List<String> boundaries;

    public ResponseMazeNode() {
    }

    public ResponseMazeNode(List<String> boundaries) {
        this.boundaries = boundaries;
    }

    public List<String> getBoundaries() {
        return boundaries;
    }

    public void setBoundaries(List<String> boundaries) {
        this.boundaries = boundaries;
    }
}
