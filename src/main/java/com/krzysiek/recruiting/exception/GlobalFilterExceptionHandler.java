package com.krzysiek.recruiting.exception;

import com.krzysiek.recruiting.dto.responsDTOs.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class GlobalFilterExceptionHandler extends BaseExceptionHandler {

    @Override
    public ErrorResponseDTO getErrorResponseDTO(Exception ex, HttpServletRequest servletRequest, HttpStatus status) {
        return super.getErrorResponseDTO(ex, servletRequest, status);
    }
}
