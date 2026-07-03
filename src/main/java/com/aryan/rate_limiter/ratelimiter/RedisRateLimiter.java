package com.aryan.rate_limiter.ratelimiter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;


@Component
@RequiredArgsConstructor
public class RedisRateLimiter {
    private final RedisTemplate<String,Object>redisTemplate;

}
