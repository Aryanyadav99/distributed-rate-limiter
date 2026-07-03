package com.aryan.rate_limiter.service.serviceImpl;

import com.aryan.rate_limiter.algorithm.tokenBucket.RateLimiter;
import com.aryan.rate_limiter.service.RateLimiterService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterImpl implements RateLimiterService {

    @Value("${rate-limit.capacity}")
    private long capacity;

    @Value("${rate-limit.refill-rate}")
    private double refillRate;

    private RateLimiter rateLimiter;

    @PostConstruct
    public void init() {
        rateLimiter = new RateLimiter(capacity, refillRate);
    }

    @Override
    public boolean allowRequest(String userId) {
        return rateLimiter.allowRequest(userId);
    }
}