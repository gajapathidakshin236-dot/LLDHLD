package com.company.ratelimite;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * PLAIN UNIT TESTS - no Spring at all. Your LLD classes are framework-free
 * plain Java, so testing them needs nothing but JUnit: construct, call,
 * assert. Fast (milliseconds) and precise. Compare with TaskApiTests,
 * which boots the whole application - both kinds matter:
 *   unit tests        -> is my LOGIC right?
 *   integration tests -> is everything WIRED right?
 *
 * Trick used here: refillRatePerSecond = 0 makes the bucket never refill,
 * so outcomes don't depend on timing. Deterministic tests never flake.
 */
class RateLimiterStrategyTests {

    @Test
    void tokenBucket_allowsBurstUpToCapacity_thenRejects() {
        TokenBucketStrategy strategy = new TokenBucketStrategy(3, 0);

        assertTrue(strategy.allowRequest("client-A"));
        assertTrue(strategy.allowRequest("client-A"));
        assertTrue(strategy.allowRequest("client-A"));
        assertFalse(strategy.allowRequest("client-A"), "4th request must be rejected - bucket empty");
    }

    @Test
    void tokenBucket_isolatesClients_eachGetsOwnBucket() {
        TokenBucketStrategy strategy = new TokenBucketStrategy(1, 0);

        assertTrue(strategy.allowRequest("client-A"));
        assertFalse(strategy.allowRequest("client-A"), "client-A exhausted its bucket");
        assertTrue(strategy.allowRequest("client-B"), "client-B has a fresh bucket of its own");
    }

    @Test
    void slidingWindowLog_capsRequestsInsideTheWindow() {
        SlidingWindowLogStrategy strategy = new SlidingWindowLogStrategy(2, 60_000);

        assertTrue(strategy.allowRequest("client-A"));
        assertTrue(strategy.allowRequest("client-A"));
        assertFalse(strategy.allowRequest("client-A"), "3rd request inside the window must be rejected");
    }

    @Test
    void controller_delegatesToWhicheverStrategyItHolds() {
        RateLimiterController controller =
                new RateLimiterController(new TokenBucketStrategy(1, 0));

        assertTrue(controller.isAllowed("x"));
        assertFalse(controller.isAllowed("x"));
    }
}
