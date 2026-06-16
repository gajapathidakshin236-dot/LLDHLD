package com.company.DSA;

import java.util.*;

/**
 * LeetCode #933 - Number of Recent Calls
 * Queue of timestamps; pop while front < t-3000.
 * Time: amortized O(1)  Space: O(n)
 */
public class RecentCounter {
    Deque<Integer> q = new ArrayDeque<>();
    public int ping(int t) {
        q.offer(t);
        while (q.peek() < t - 3000) q.poll();
        return q.size();
    }
}
