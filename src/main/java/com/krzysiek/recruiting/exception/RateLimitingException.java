package com.krzysiek.recruiting.exception;

public class RateLimitingException extends RuntimeException {
    public RateLimitingException(String message) {
        super(message);
    }
}
