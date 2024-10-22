package com.krzysiek.recruiting.filter;


import com.krzysiek.recruiting.exception.customExceptions.RateLimitingException;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import com.github.benmanes.caffeine.cache.Cache;
import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.github.bucket4j.Bandwidth;


import java.io.IOException;

@NonNullApi
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Cache<String, Bucket> cache;
    private final Bandwidth limit;

    public RateLimitingFilter(Cache<String, Bucket> cache, Bandwidth limit){
        super();
        this.cache = cache;
        this.limit = limit;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = "anonymous";
        if (authentication != null && authentication.isAuthenticated()) {
            userId = authentication.getName();
        }

        String endpoint = request.getRequestURI();
        String key = userId + ":" + endpoint;

        Bucket bucket = cache.get(key, k -> Bucket.builder()
                .addLimit(limit)
                .build());

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            filterChain.doFilter(request, response);
        } else {
            throw new RateLimitingException("Too many requests.");
        }
    }
}
