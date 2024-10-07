package com.krzysiek.recruiting.exception;

import io.jsonwebtoken.JwtException;
import jakarta.validation.ValidationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Component;

@Component
public class ThrowCorrectException {

    public void handleException(Exception ex) {
        if (ex instanceof ValidationException) {
            throw new ValidationException(ex.getMessage(), ex);
        } else if (ex instanceof UserNotFoundException) {
            throw new UserNotFoundException(ex.getMessage());
        } else if (ex instanceof JpaSystemException) {
            throw new RuntimeException("JPA system error: " + ex.getMessage(), ex);
        } else if (ex instanceof JwtException) {
            throw new JwtException(ex.getMessage(), ex);
        }
        throw new RuntimeException("Error occurred: " + ex.getMessage());
    }

}
