package com.aryan.rate_limiter.service.serviceImpl;

import com.aryan.rate_limiter.ratelimiter.RedisRateLimiter_Core;
import com.aryan.rate_limiter.service.RateLimiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateLimiterImpl implements RateLimiterService {

    private final RedisRateLimiter_Core rateLimiter;

    @Value("${rate-limit.capacity}")
    private long capacity;

    @Value("${rate-limit.refill-rate}")
    private double refillRate;

    @Override
    public boolean allowRequest(String userId) {
        return rateLimiter.allowRequest(
                "rate-limit:" + userId,
                capacity,
                refillRate
        );
    }
}