package com.sensys.event.exception;

public class ViolationNotFoundException extends RuntimeException{
    public ViolationNotFoundException(String message) {
        super(message);
    }
}
