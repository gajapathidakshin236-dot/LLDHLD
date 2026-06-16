package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #933 — Number of Recent Calls
 * ============================================================
 *  PROBLEM
 *  -------
 *  Implement RecentCounter where ping(t) records a request at time t and
 *  returns the number of requests in the inclusive range [t-3000, t].
 *  Calls are strictly increasing in time.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: ping(1) → 1
 *       ping(100) → 2
 *       ping(3001) → 3
 *       ping(3002) → 3   (drops ping(1) since 3002 - 3000 = 2 > 1)
 *
 *  CONSTRAINTS:  1 <= t <= 10^9; calls increasing; up to 10^4 calls.
 *
 *  HINTS
 *  -----
 *   1. Queue of timestamps. Push new ones at the back.
 *   2. Pop from the front while front < t - 3000.
 *   3. Size of queue is the answer.
 * ============================================================ */
public class RecentCounter {
    Deque<Integer> q = new ArrayDeque<>();
    public int ping(int t) {
        q.offer(t);
        while (q.peek() < t - 3000) q.poll();
        return q.size();
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  A sliding window keyed on timestamps. New ping enters the back; expired
 *  pings (older than t-3000) leave the front. Each ping is enqueued and
 *  dequeued at most once → amortized O(1) per call.
 *
 *  Complexity: Time O(1) amortized, Space O(n) worst.
 *  Edge cases: very small t (e.g., 1) → window [1-3000, 1] = [-2999, 1]; nothing expires.
 *  Pattern: time-sliding window with queue. Same: API rate limiter, stream stats.
 * ============================================================ */
