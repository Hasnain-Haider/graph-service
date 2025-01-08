package us.hassu.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import us.hassu.graphs.graph.Edge;
import us.hassu.graphs.graph.Node;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GraphSerializer extends JsonSerializer<Map<Node, List<Edge<Node>>>> {

    @Override
    public void serialize(Map<Node, List<Edge<Node>>> graph, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        for (Map.Entry<Node, List<Edge<Node>>> entry : graph.entrySet()) {
            Node node = entry.getKey();
            List<Edge<Node>> edges = entry.getValue();
            gen.writeFieldName(node.getId());
            List<String> tos = edges.stream().map(Edge::getTo).map(Node::getId).collect(Collectors.toList());
            gen.writeObject(tos);
        }
        gen.writeEndObject();
    }
}