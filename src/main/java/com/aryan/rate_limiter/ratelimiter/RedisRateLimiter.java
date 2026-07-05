package com.aryan.rate_limiter.ratelimiter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class RedisRateLimiter {

    private final RedisTemplate<String, Object> redisTemplate;

    private final DefaultRedisScript<Long> tokenBucketScript;

    public boolean allowRequest(String key,
                                long capacity,
                                double refillRatePerSecond) {

        Long result = redisTemplate.execute(
                tokenBucketScript,
                Collections.singletonList(key),
                String.valueOf(capacity),
                String.valueOf(refillRatePerSecond),
                String.valueOf(System.currentTimeMillis())
        );

        return result != null && result == 1L;
    }
}