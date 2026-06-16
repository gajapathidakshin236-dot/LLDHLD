package com.company.ratelimite;

/**
 * Strategy contract. Every algorithm answers one question:
 * "should this client's request be allowed right now?"
 * The controller depends only on this interface, so algorithms are swappable.
 */
public interface RateLimitingStrategy {
    boolean allowRequest(String clientId);
}
