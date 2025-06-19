package com.example.springrest_refresher.restapi.clockapi.exception;

public class InvalidTimeZoneException extends RuntimeException {
    public InvalidTimeZoneException(String message) {
        super(message);
    }
}
