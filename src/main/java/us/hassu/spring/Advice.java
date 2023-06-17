package us.hassu.spring;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import us.hassu.exception.RateLimitExceededException;

@RestControllerAdvice
public class Advice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = { RateLimitExceededException.class })
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        RestError limitExceeded = new RestError("Rate limit exceeded");
        return handleExceptionInternal(ex, limitExceeded, new HttpHeaders(), HttpStatus.TOO_MANY_REQUESTS, request);
    }
}
