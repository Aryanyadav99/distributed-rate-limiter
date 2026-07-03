package com.aryan.rate_limiter.service.serviceImpl;

import com.aryan.rate_limiter.algorithm.tokenBucket.RateLimiter;
import com.aryan.rate_limiter.service.RateLimiterService;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterImpl implements RateLimiterService {

    private final RateLimiter rateLimiter=new RateLimiter(5,2);
    @Override
    public boolean allowRequest(String userId) {
        return rateLimiter.allowRequest(userId);
    }
}
