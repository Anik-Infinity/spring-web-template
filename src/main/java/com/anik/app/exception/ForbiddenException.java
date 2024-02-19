package com.anik.app.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super("Unauthorized Resource Access");
    }

    public ForbiddenException(String message) {
        super(message);
    }
}