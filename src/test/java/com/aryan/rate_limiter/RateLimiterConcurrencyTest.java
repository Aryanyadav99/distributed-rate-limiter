package com.aryan.rate_limiter;

import com.aryan.rate_limiter.service.RateLimiterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
class RateLimiterConcurrencyTest {

    @Autowired
    private RateLimiterService rateLimiterService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void shouldExposeRaceCondition() throws Exception {

        String key = "rate-limit:test-user";

        // Clean bucket
        redisTemplate.delete(key);

        // ----------------------------
        // PRE-CREATE THE BUCKET
        // ----------------------------
        rateLimiterService.allowRequest("test-user");

        // Reset bucket to full capacity
        redisTemplate.opsForHash().put(key, "tokens", "10");
        redisTemplate.opsForHash().put(
                key,
                "lastRefillTime",
                String.valueOf(System.currentTimeMillis())
        );

        int totalRequests = 100;
        int threadPoolSize = 100;

        ExecutorService executor =
                Executors.newFixedThreadPool(threadPoolSize);

        CountDownLatch ready =
                new CountDownLatch(totalRequests);

        CountDownLatch start =
                new CountDownLatch(1);

        CountDownLatch finished =
                new CountDownLatch(totalRequests);

        AtomicInteger allowed = new AtomicInteger();

        AtomicInteger rejected = new AtomicInteger();

        for (int i = 0; i < totalRequests; i++) {

            executor.submit(() -> {

                ready.countDown();

                try {

                    start.await();

                    if (rateLimiterService.allowRequest("test-user")) {
                        allowed.incrementAndGet();
                    } else {
                        rejected.incrementAndGet();
                    }

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();

                } finally {

                    finished.countDown();

                }

            });

        }

        ready.await();

        long startTime = System.currentTimeMillis();

        start.countDown();

        finished.await();

        long endTime = System.currentTimeMillis();

        executor.shutdown();

        System.out.println("==================================");
        System.out.println("Capacity : 10");
        System.out.println("Requests : " + totalRequests);
        System.out.println("----------------------------------");
        System.out.println("Allowed  : " + allowed.get());
        System.out.println("Rejected : " + rejected.get());
        System.out.println("Time(ms) : " + (endTime - startTime));
        System.out.println("==================================");
    }
}