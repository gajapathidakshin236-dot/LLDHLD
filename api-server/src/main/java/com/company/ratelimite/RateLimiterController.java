package com.company.ratelimite;

/**
 * The entry point an API gateway / filter would call.
 *
 * Holds ONE strategy and delegates to it. The strategy is injected (DI) and
 * mutable, so it can be swapped at runtime -- e.g., switch to a stricter
 * algorithm during peak hours.
 *
 * Design choice: one strategy for all clients here. If different endpoints or
 * client tiers needed different policies, you'd hold a
 * Map<routeOrTier, RateLimitingStrategy> and pick per request instead.
 */
public class RateLimiterController {

    private RateLimitingStrategy strategy;

    public RateLimiterController(RateLimitingStrategy strategy) {
        this.strategy = strategy;
    }

    /** Swap the algorithm at runtime (e.g., peak-hours tightening). */
    public void setStrategy(RateLimitingStrategy strategy) {
        this.strategy = strategy;
    }

    /** The core call: should we let this client's request through? */
    public boolean isAllowed(String clientId) {
        return strategy.allowRequest(clientId);
    }

    /**
     * Convenience: simulate handling a request. In a real system this would
     * be a servlet filter returning HTTP 429 on rejection.
     */
    public void handleRequest(String clientId) {
        if (isAllowed(clientId)) {
            System.out.println("[200 OK]  request from " + clientId + " served");
        } else {
            System.out.println("[429]     request from " + clientId + " THROTTLED");
        }
    }
}
