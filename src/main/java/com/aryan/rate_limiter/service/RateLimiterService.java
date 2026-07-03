package com.aryan.rate_limiter.service;

public interface RateLimiterService {

    boolean allowRequest(String userId);
}
