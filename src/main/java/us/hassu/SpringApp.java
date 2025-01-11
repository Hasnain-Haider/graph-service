package us.hassu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import us.hassu.entity.maze.MazeNode;
import us.hassu.graphs.GraphUtils;
import us.hassu.service.GraphService;

@SpringBootApplication
public class SpringApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringApp.class, args);
    }

    @Bean
    GraphService graphService() {
        return new GraphService();
    }

    @Bean
    GraphUtils<MazeNode> graphUtils() {
        return new GraphUtils<>();
    }

    @Bean
    MazeUtils mazeUtils() {
        return new MazeUtils();
    }
}
