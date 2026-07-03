package com.aryan.rate_limiter.algorithm.tokenBucket;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        TokenBucket tb = new TokenBucket(5, 2);

        // ===========================
        // Test 1 : Normal Requests
        // ===========================

        /*
        for (int i = 1; i <= 10; i++) {

            System.out.println("Request " + i + " -> " + tb.allowRequest());

            Thread.sleep(500);
        }

        Expected:
        All requests should be true because
        every 500 ms one token gets refilled.
        */


        // ===========================
        // Test 2 : Burst Requests
        // ===========================

        /*
        for (int i = 1; i <= 10; i++) {

            System.out.println("Request " + i + " -> " + tb.allowRequest());
        }

        Expected:
        First 5 -> true
        Next 5  -> false
        */


        // ===========================
        // Test 3 : Bucket Refill
        // ===========================

         //Consume all tokens
        /*
        for (int i = 1; i <= 5; i++) {
            System.out.println("Request " + i + " -> " + tb.allowRequest());
        }

        // Bucket is empty now
        System.out.println("Request 6 -> " + tb.allowRequest());

        System.out.println("\nWaiting for 3 seconds...\n");

        Thread.sleep(3000);

        // Bucket should be refilled
        for (int i = 7; i <= 12; i++) {
            System.out.println("Request " + i + " -> " + tb.allowRequest());
        }

        */


        // for multiple users


        RateLimiter limiter = new RateLimiter(5, 2);

        // Dholu exhausts his bucket
        System.out.println("Dholu Requests");
        for (int i = 1; i <= 6; i++) {
            System.out.println(
                    "Request " + i + " -> " +
                            limiter.allowRequest("Dholu")
            );
        }

        System.out.println();

        // Bholu has a fresh bucket
        System.out.println("Bholu Requests");
        for (int i = 1; i <= 6; i++) {
            System.out.println(
                    "Request " + i + " -> " +
                            limiter.allowRequest("Bholu")
            );
        }

        System.out.println();

        // Dholu still has no tokens
        System.out.println("Dholu Again");
        System.out.println(
                "Request -> " +
                        limiter.allowRequest("Dholu")
        );

        System.out.println();

        // Kalia is a completely new user
        System.out.println("Kalia Requests");
        for (int i = 1; i <= 3; i++) {
            System.out.println(
                    "Request " + i + " -> " +
                            limiter.allowRequest("Kalia")
            );
        }
    }
}

