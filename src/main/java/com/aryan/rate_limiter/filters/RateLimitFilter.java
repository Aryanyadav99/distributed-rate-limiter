package com.aryan.rate_limiter.filters;

import com.aryan.rate_limiter.algorithm.tokenBucket.RateLimiter;
import com.aryan.rate_limiter.service.RateLimiterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(1)
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {
    private final RateLimiterService rateLimiter;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("RateLimitFilter Hit");

        String userId=request.getHeader("X-User-Id");

        if(userId==null || userId.isBlank()){
            userId=request.getRemoteAddr();
        }

        if(!rateLimiter.allowRequest(userId)){
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("Rate Limit Exceeded");
            return;
        }
        filterChain.doFilter(request,response);
    }
}
