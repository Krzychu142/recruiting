package com.krzysiek.recruiting.exception.customExceptions;

public class JobDescriptionAlreadyExistsException extends RuntimeException {
    public JobDescriptionAlreadyExistsException(String message) {
        super(message);
    }
}
