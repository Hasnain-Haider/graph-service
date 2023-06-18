package us.hassu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import us.hassu.service.GraphService;
import us.hassu.spring.Secrets;

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
    Secrets secrets() {
        String secretsFile = "secret.properties";
        return new Secrets(secretsFile);
    }
}
