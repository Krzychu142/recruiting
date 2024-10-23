package com.krzysiek.recruiting.exception;

import com.krzysiek.recruiting.exception.customExceptions.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ValidationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Component;

import java.security.SignatureException;

@Component
public class ThrowCorrectException {

    public RuntimeException handleException(Exception ex) {
        if (ex instanceof ValidationException) {
            throw new ValidationException(ex.getMessage(), ex);
        } else if (ex instanceof UserNotFoundException) {
            throw new UserNotFoundException(ex.getMessage());
        } else if (ex instanceof JobDescriptionAlreadyExistsException) {
            throw new JobDescriptionAlreadyExistsException(ex.getMessage());
        } else if (ex instanceof RecruitmentProcessNotFoundException) {
            throw new RecruitmentProcessNotFoundException(ex.getMessage());
        } else if (ex instanceof JpaSystemException) {
            throw new RuntimeException("JPA system error: " + ex.getMessage(), ex);
        } else if (ex instanceof SignatureException){
            throw new JwtException("SignatureException: " + ex.getMessage(), ex);
        } else if (ex instanceof ExpiredJwtException){
            throw new JwtException("JWT expired: " + ex.getMessage(), ex);
        } else if (ex instanceof JwtException) {
            throw new JwtException(ex.getMessage(), ex);
        } else if (ex instanceof IllegalArgumentException) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        } else if (ex instanceof HttpMessageNotReadableException) {
            throw new ValidationException(ex.getMessage(), ex);
        } else if (ex instanceof IllegalStateException) {
            throw new IllegalStateException(ex.getMessage(), ex);
        } else if (ex instanceof StorageFileNotFoundException) {
            throw new StorageFileNotFoundException(ex.getMessage(), ex);
        } else if (ex instanceof StorageException) {
            throw new StorageException(ex.getMessage(), ex);
        } else if (ex instanceof AccessDeniedException) {
            throw new AccessDeniedException(ex.getMessage());
        }
        return new RuntimeException("Error occurred: " + ex.getMessage());
    }

}
