package com.company.ratelimite;

/**
 * Per-client state for the token bucket algorithm.
 * Fixed: capacity, refillRatePerSecond.  Mutable: tokens, lastRefillTime.
 */
public class TokenBucket {
    private final double capacity;
    private final long refillRatePerSecond;
    private double tokens;
    private long lastRefillTime;

    public TokenBucket(double capacity, long refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
        this.tokens = capacity;                          // start full
        this.lastRefillTime = System.currentTimeMillis();
    }

    public double getCapacity()          { return capacity; }
    public long getRefillRatePerSecond() { return refillRatePerSecond; }
    public double getTokens()            { return tokens; }
    public void setTokens(double t)      { this.tokens = t; }
    public long getLastRefillTime()      { return lastRefillTime; }
    public void setLastRefillTime(long t){ this.lastRefillTime = t; }
}
