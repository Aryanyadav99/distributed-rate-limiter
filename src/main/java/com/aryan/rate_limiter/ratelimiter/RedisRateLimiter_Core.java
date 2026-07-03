package com.aryan.rate_limiter.ratelimiter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RedisRateLimiter_Core {

    private final RedisTemplate<String, Object> redisTemplate;

    public boolean allowRequest(String key,
                                long capacity,
                                double refillRatePerSecond) {

        HashOperations<String, Object, Object> hashOps =
                redisTemplate.opsForHash();

        long currentTime = System.currentTimeMillis();

        // Read bucket from Redis
        Map<Object, Object> bucket = hashOps.entries(key);

        double currentTokens;
        long lastRefillTime;

        // First request
        if (bucket.isEmpty()) {
            currentTokens = capacity;
            lastRefillTime = currentTime;
        } else {
            currentTokens = Double.parseDouble(
                    bucket.get("tokens").toString());

            lastRefillTime = Long.parseLong(
                    bucket.get("lastRefillTime").toString());
        }

        // Refill Logic
        double elapsedTime =
                (currentTime - lastRefillTime) / 1000.0;

        double refillTokens =
                elapsedTime * refillRatePerSecond;

        currentTokens = Math.min(
                capacity,
                currentTokens + refillTokens
        );

        lastRefillTime = currentTime;

        // Consume token
        if (currentTokens >= 1) {

            currentTokens--;

            hashOps.put(key, "tokens", String.valueOf(currentTokens));
            hashOps.put(key, "lastRefillTime", String.valueOf(lastRefillTime));

            return true;
        }

        // Save latest state
        hashOps.put(key, "tokens", String.valueOf(currentTokens));
        hashOps.put(key, "lastRefillTime", String.valueOf(lastRefillTime));

        return false;
    }
    // got race condition
}