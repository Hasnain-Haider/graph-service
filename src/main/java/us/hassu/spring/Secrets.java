package us.hassu.spring;

import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.InputStream;
import java.util.Properties;

public class Secrets {
    Properties secrets;
    @SneakyThrows
    public Secrets(String secretsFile) {
        InputStream in = getClass().getClassLoader().getResourceAsStream(secretsFile);
        secrets = new Properties();
        secrets.load(in);
    }
}
