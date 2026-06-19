package com.company.DSA.design;

import java.util.*;

/* ============================================================
 *  LeetCode #933 — Number of Recent Calls
 * ============================================================
 *  PROBLEM
 *  -------
 *  ping(t) records time; returns # pings in [t-3000, t]. Times strictly increasing.
 *
 *  EXAMPLES
 *  --------
 *  ping(1) → 1; ping(100) → 2; ping(3001) → 3; ping(3002) → 3.
 *
 *  CONSTRAINTS:  1 <= t <= 10^9; up to 10^4 calls.
 *
 *  HINTS
 *  -----
 *   1. Queue of timestamps. Pop expired from front.
 * ============================================================ */
public class RecentCounter {

    private static final int WINDOW_MILLIS = 3000;
    private final Deque<Integer> recentTimestamps = new ArrayDeque<>();

    public int ping(final int currentTime) {
        recentTimestamps.offer(currentTime);
        final int earliestAllowedTime = currentTime - WINDOW_MILLIS;
        while (recentTimestamps.peek() < earliestAllowedTime) {
            recentTimestamps.poll();
        }
        return recentTimestamps.size();
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Time-sliding window with a queue. Amortized O(1) per call.
 *
 *  Complexity: Time amortized O(1), Space O(n).
 *  Pattern: time-sliding window with queue.
 * ============================================================ */
