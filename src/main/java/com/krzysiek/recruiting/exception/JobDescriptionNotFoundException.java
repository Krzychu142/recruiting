package com.krzysiek.recruiting.exception;

public class JobDescriptionNotFoundException extends RuntimeException {
    public JobDescriptionNotFoundException(String message) {
        super(message);
    }
}
