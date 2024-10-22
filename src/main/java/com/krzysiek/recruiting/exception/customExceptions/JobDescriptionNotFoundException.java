package com.krzysiek.recruiting.exception.customExceptions;

public class JobDescriptionNotFoundException extends RuntimeException {
    public JobDescriptionNotFoundException(String message) {
        super(message);
    }
}
