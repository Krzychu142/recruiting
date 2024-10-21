package com.krzysiek.recruiting.exception.customExceptions;

public class RecruitmentProcessNotFoundException extends RuntimeException {
    public RecruitmentProcessNotFoundException(String message) {
        super(message);
    }
}
