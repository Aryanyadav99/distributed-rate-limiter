package com.aryan.rate_limiter.controller;

import com.aryan.rate_limiter.ratelimiter.RedisRateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DemoController {

    @GetMapping("/hello")
    public String hello(){
        return "Hello From Chomu";
    }

    private final RedisRateLimiter redisRateLimiter;


}
