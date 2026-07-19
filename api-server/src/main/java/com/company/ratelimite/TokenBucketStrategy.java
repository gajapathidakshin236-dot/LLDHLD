package com.company.ratelimite;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TOKEN BUCKET: each client has a bucket that refills over time (lazily,
 * computed on read). A request consumes one token; empty bucket = reject.
 * Allows bursts up to capacity.
 */
public class TokenBucketStrategy implements RateLimitingStrategy {

    private final double capacity;
    private final long refillRatePerSecond;
    private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    public TokenBucketStrategy(double capacity, long refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
    }

    @Override
    public boolean allowRequest(String clientId) {
        TokenBucket bucket = buckets.computeIfAbsent(clientId,
                k -> new TokenBucket(capacity, refillRatePerSecond));

        synchronized (bucket) {
            long now = System.currentTimeMillis();
            double elapsedSeconds = (now - bucket.getLastRefillTime()) / 1000.0;

            // refill: add tokens, capped at capacity (the key lesson)
            double refilled = bucket.getTokens() + elapsedSeconds * bucket.getRefillRatePerSecond();
            bucket.setTokens(Math.min(bucket.getCapacity(), refilled));
            bucket.setLastRefillTime(now);

            if (bucket.getTokens() >= 1) {
                bucket.setTokens(bucket.getTokens() - 1);   // consume
                return true;
            }
            return false;
        }
    }
}
