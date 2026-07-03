package com.aryan.rate_limiter.algorithm.tokenBucket;

public class TokenBucket {
    private final long capacity; // the total capacity of bucket
    private final double refillRate; // the rate  through refiller refill the bucket
    // capacity and refillRate both are constant
    private double currentTokens;// this state the currentTokens exist in the bucket
    private long lastRefillTime; // the previous refill time so when we allow request first refill the bucket based
    // on that previous refill time and calculate the token need to fill in bucket

    public TokenBucket(long capacity, double refillRate){
        this.capacity=capacity;
        this.refillRate=refillRate;
        this.currentTokens=capacity;
        this.lastRefillTime=System.nanoTime();
    }
    // this allowRequest fun decide whether the request enter in server or we return the 429 response
    // we make it synchronized because we have to make sure its thread safe and didn't go for race condition
    public synchronized boolean allowRequest(){
        // before allowing the request we have to refill the bucket based on the lastRefillTime and current time
        refill();
        if(currentTokens>=1){
            currentTokens--;
            return true;
        }
        return false;

    }
    public void refill(){
        long currTime=System.nanoTime();
        // this time is in nanoseconds like eg:100ns
        // we also have the prevTime like eg:50ns
        // we have to find the diff so can calc the refill quant -> currTime-prevTime in ns
        // out refill rate is let say 2tokens/sec so convert that ns to second
        // now the result can be in decimal so make it double that's it
        double gap= (currTime-lastRefillTime)/1_000_000_000.0;

        double refillTokens=gap*refillRate;

        // current tokens + refillToken is actual but cannot exceed the capacity so take the min of them
        currentTokens=Math.min(capacity,currentTokens+refillTokens);

        lastRefillTime=currTime;
    }
}
