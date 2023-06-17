package us.hassu.spring;

import lombok.Data;

@Data
public class RestError {
    private String errorMessage;

    public RestError(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
