package com.krzysiek.recruiting.exception.customExceptions;

public class RateLimitingException extends RuntimeException {
    public RateLimitingException(String message) {
        super(message);
    }
}
