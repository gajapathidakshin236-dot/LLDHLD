package com.company.DSA.slidingwindow;

import java.util.*;

/* ============================================================
 *  LeetCode #239 — Sliding Window Maximum
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return the maximum of each window of size k as it slides through nums.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[1,3,-1,-3,5,3,6,7], k=3 → [3,3,5,5,6,7]
 *  Ex2: nums=[1], k=1 → [1]
 *  Ex3: nums=[1,-1], k=1 → [1,-1]
 *
 *  CONSTRAINTS:  1 <= n <= 10^5; -10^4 <= val <= 10^4; 1 <= k <= n.
 *
 *  HINTS
 *  -----
 *   1. Monotonic decreasing deque of INDICES.
 *   2. Front of deque = index of current max.
 * ============================================================ */
public class SlidingWindowMaximum {

    public int[] maxSlidingWindow(final int[] numbers, final int windowSize) {
        final int[] windowMaxima = new int[numbers.length - windowSize + 1];
        final Deque<Integer> indicesInDecreasingValue = new ArrayDeque<>();

        for (int rightCursor = 0; rightCursor < numbers.length; rightCursor++) {
            evictIndicesOutsideWindow(indicesInDecreasingValue, rightCursor - windowSize + 1);
            evictTrailingSmallerIndices(indicesInDecreasingValue, numbers, rightCursor);
            indicesInDecreasingValue.offer(rightCursor);

            if (rightCursor >= windowSize - 1) {
                windowMaxima[rightCursor - windowSize + 1] = numbers[indicesInDecreasingValue.peek()];
            }
        }
        return windowMaxima;
    }

    private void evictIndicesOutsideWindow(final Deque<Integer> deque, final int earliestValidIndex) {
        while (!deque.isEmpty() && deque.peek() < earliestValidIndex) {
            deque.poll();
        }
    }

    private void evictTrailingSmallerIndices(final Deque<Integer> deque,
                                             final int[] numbers,
                                             final int rightCursor) {
        while (!deque.isEmpty() && numbers[deque.peekLast()] < numbers[rightCursor]) {
            deque.pollLast();
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Monotonic decreasing deque of indices. Front is the current window max.
 *  Evict from back when smaller than incoming; evict from front when out of
 *  window. Each index is added and removed at most once → O(n).
 *
 *  Complexity: Time O(n), Space O(k).
 *  Pattern: monotonic deque.
 * ============================================================ */
