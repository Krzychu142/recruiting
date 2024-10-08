package com.krzysiek.recruiting.exception;

import io.jsonwebtoken.JwtException;
import jakarta.validation.ValidationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Component;

@Component
public class ThrowCorrectException {

    public RuntimeException handleException(Exception ex) {
        if (ex instanceof ValidationException) {
            return new ValidationException(ex.getMessage(), ex);
        } else if (ex instanceof UserNotFoundException) {
            return new UserNotFoundException(ex.getMessage());
        } else if (ex instanceof JpaSystemException) {
            return new RuntimeException("JPA system error: " + ex.getMessage(), ex);
        } else if (ex instanceof JwtException) {
            return new JwtException(ex.getMessage(), ex);
        }
        return new RuntimeException("Error occurred: " + ex.getMessage());
    }

}
