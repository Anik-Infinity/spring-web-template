package com.anik.app.common.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("Not Found");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
