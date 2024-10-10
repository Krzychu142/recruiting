package com.krzysiek.recruiting.filter;

import com.krzysiek.recruiting.enums.Role;
import com.krzysiek.recruiting.enums.TokensType;
import com.krzysiek.recruiting.service.JWTService;
import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@NonNullApi
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    public JwtAuthFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.replaceFirst("Bearer ", "");
        TokensType tokensType = jwtService.extractType(token);
        if (tokensType != TokensType.ACCESS) {
            filterChain.doFilter(request, response);
        }
        String email = jwtService.extractEmail(token);
        Role role = jwtService.extractRole(token);
        List<SimpleGrantedAuthority> authorities = role != null
                ? Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()))
                : Collections.emptyList();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, null, authorities);
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
