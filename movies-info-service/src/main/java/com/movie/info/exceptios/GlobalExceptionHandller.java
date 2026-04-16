package com.movie.info.exceptios;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandller {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<String> handleRequestBodyException(WebExchangeBindException ex) {
        log.error("An error occurred in handleRequestBodyException : {}", ex.getMessage());
        var errorMessage = ex.getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce("", (msg1, msg2) -> msg1 + ", " + msg2);
        log.error("An error occurred in handleRequestBodyException errorMessage : {}", errorMessage);

        var errorMessage2 = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining(" , "));
        log.error("An error occurred in handleRequestBodyException errorMessage2 : {}", errorMessage2);

        return ResponseEntity.badRequest().body("Validation error: " + errorMessage);
    }

}
