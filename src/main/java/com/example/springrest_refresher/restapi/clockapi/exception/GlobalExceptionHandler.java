package com.example.springrest_refresher.restapi.clockapi.exception;

import com.example.springrest_refresher.restapi.clockapi.model.response.ApiResponseWrapper;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidTimeZoneException.class)
    public ResponseEntity<?> handleInvalidZoneException(InvalidTimeZoneException e) {
        return ResponseEntity.badRequest().body(new ApiResponseWrapper<>(HttpStatus.BAD_REQUEST.value(), null, List.of(e.getMessage())));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException e) {
        return ResponseEntity.status(404).body(new ApiResponseWrapper<>(HttpStatus.NOT_FOUND.value(), null, List.of(e.getMessage())));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.badRequest().body(new ApiResponseWrapper<>(HttpStatus.BAD_REQUEST.value(), null, List.of(e.getMessage())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.badRequest().body(new ApiResponseWrapper<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, List.of(e.getMessage())));
    }
}
