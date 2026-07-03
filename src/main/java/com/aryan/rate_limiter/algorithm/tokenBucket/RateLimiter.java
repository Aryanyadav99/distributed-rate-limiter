package com.aryan.rate_limiter.algorithm.tokenBucket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter {
    private final Map<String,TokenBucket> buckets=new ConcurrentHashMap<>();
    private final long capacity;
    private final double refillRate;

    public RateLimiter(long capacity, double refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
    }
    public boolean allowRequest(String userId) {

        TokenBucket bucket = buckets.computeIfAbsent(
                userId,
                key -> new TokenBucket(capacity, refillRate)
        );

        return bucket.allowRequest();
    }
}
