package com.krzysiek.recruiting.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krzysiek.recruiting.dto.ErrorResponseDTO;
import com.krzysiek.recruiting.exception.GlobalFilterExceptionHandler;
import com.krzysiek.recruiting.exception.RateLimitingException;
import io.jsonwebtoken.JwtException;
import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@NonNullApi
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final GlobalFilterExceptionHandler globalFilterExceptionHandler;
    private final ObjectMapper objectMapper;

    public ExceptionHandlerFilter(GlobalFilterExceptionHandler globalFilterExceptionHandler, ObjectMapper objectMapper) {
        this.globalFilterExceptionHandler = globalFilterExceptionHandler;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException ex) {
            handleException(response, ex, HttpStatus.UNAUTHORIZED, request);
        } catch (RateLimitingException ex){
            handleException(response, ex, HttpStatus.TOO_MANY_REQUESTS, request);
        } catch (Exception ex) {
            handleException(response, ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }

    private void handleException(HttpServletResponse response, Exception ex, HttpStatus status, HttpServletRequest request) throws IOException {
        ErrorResponseDTO errorResponseDTO = globalFilterExceptionHandler.getErrorResponseDTO(
                ex,
                request,
                status
        );

        response.setStatus(status.value());
        response.setContentType("application/json");
        String jsonResponse = objectMapper.writeValueAsString(errorResponseDTO);
        response.getWriter().write(jsonResponse);
    }
}
