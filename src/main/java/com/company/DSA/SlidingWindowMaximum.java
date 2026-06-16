package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #239 — Sliding Window Maximum
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given an array nums and integer k, return the maximum of each window of
 *  size k as it slides from left to right.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[1,3,-1,-3,5,3,6,7], k=3 → [3,3,5,5,6,7]
 *  Ex2: nums=[1],                k=1 → [1]
 *  Ex3: nums=[1,-1],             k=1 → [1,-1]
 *  Ex4: nums=[9,11], k=2          → [11]
 *
 *  CONSTRAINTS:  1 <= n <= 10^5;  -10^4 <= val <= 10^4;  1 <= k <= n.
 *
 *  HINTS
 *  -----
 *   1. Monotonic DECREASING deque of INDICES.
 *   2. Pop indices from BACK if their value <= incoming (they can't be max).
 *   3. Pop from FRONT if it falls out of the window (idx < i-k+1).
 *   4. Front of the deque is the max of the current window.
 * ============================================================ */
public class SlidingWindowMaximum {
    public int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        int[] res = new int[n - k + 1];
        Deque<Integer> dq = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!dq.isEmpty() && dq.peek() < i - k + 1) dq.poll();
            while (!dq.isEmpty() && nums[dq.peekLast()] < nums[i]) dq.pollLast();
            dq.offer(i);
            if (i >= k - 1) res[i - k + 1] = nums[dq.peek()];
        }
        return res;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Monotonic deque of INDICES with values in decreasing order from front to back.
 *  - Front index is the current window's maximum.
 *  - Before pushing i, drop indices at the back with values <= nums[i] (they
 *    can never be the max while nums[i] is in the window).
 *  - Drop from front if it has fallen out of the window's left edge.
 *  Each index is added & removed at most once → O(n).
 *
 *  Why store INDICES not values:
 *    To check whether the front has expired (left the window), we need its index.
 *
 *  Complexity: Time O(n), Space O(k).
 *  Edge cases: k=1 → returns nums itself; k=n → one max.
 *  Pattern: monotonic deque — used in sliding minimum, jump game IV, etc.
 * ============================================================ */
