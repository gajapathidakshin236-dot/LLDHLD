package com.company.ratelimite;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SLIDING WINDOW LOG: keep a log of recent request timestamps per client.
 * On each request: evict timestamps older than the window, then allow only
 * if the count still inside the window is below the limit.
 */
public class SlidingWindowLogStrategy implements RateLimitingStrategy {

    private final int maxRequests;
    private final long windowSizeMs;
    private final Map<String, Deque<Long>> requestLogs = new ConcurrentHashMap<>();

    public SlidingWindowLogStrategy(int maxRequests, long windowSizeMs) {
        this.maxRequests = maxRequests;
        this.windowSizeMs = windowSizeMs;
    }

    @Override
    public boolean allowRequest(String clientId) {
        Deque<Long> log = requestLogs.computeIfAbsent(clientId, k -> new ArrayDeque<>());
        long now = System.currentTimeMillis();
        long windowStart = now - windowSizeMs;

        synchronized (log) {
            while (!log.isEmpty() && log.peekFirst() <= windowStart) {
                log.pollFirst();
            }
            if (log.size() < maxRequests) {
                log.addLast(now);
                return true;
            }
            return false;
        }
    }
}
