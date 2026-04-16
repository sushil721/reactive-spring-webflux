package com.movie.service.exceptionHandler;

import com.movie.service.exception.MoviesInfoClientException;
import com.movie.service.exception.ReviewsClientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalErrorHandler {

    @ExceptionHandler({MoviesInfoClientException.class, ReviewsClientException.class})
    public ResponseEntity<String> handleClientException(MoviesInfoClientException exception){

        log.error("Exception caught in handleClientException : {} ", exception.getMessage());
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRunTimeException(RuntimeException exception){

        log.error("Exception caught in handleRunTimeException : {} ", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());

    }
}
