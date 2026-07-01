package com.company.DSA.srivatsanDSA;

import java.util.ArrayDeque;
import java.util.Deque;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 38
 *  Problem: Sliding Window Maximum (window size k)
 *
 *  APPROACH (from notes):
 *    Monotonic DECREASING deque holding INDICES.
 *    Front of deque holds the index of the current max.
 *    For each i:
 *      - while deque non-empty and front index < i - k + 1 → poll front (out of window).
 *      - while deque non-empty and nums[peekLast] < nums[i] → pollLast (smaller than incoming).
 *      - offer(i).
 *      - once i >= k - 1, record nums[front] in result.
 * ============================================================ */
public class SlidingWindowMaximum {

    public int[] maxSlidingWindow(final int[] numbers, final int windowSize) {
        final int[] windowMaxima = new int[numbers.length - windowSize + 1];
        final Deque<Integer> indicesInDecreasingValue = new ArrayDeque<>();

        for (int rightCursor = 0; rightCursor < numbers.length; rightCursor++) {
            while (!indicesInDecreasingValue.isEmpty()
                    && indicesInDecreasingValue.peek() < rightCursor - windowSize + 1) {
                indicesInDecreasingValue.poll();
            }
            while (!indicesInDecreasingValue.isEmpty()
                    && numbers[indicesInDecreasingValue.peekLast()] < numbers[rightCursor]) {
                indicesInDecreasingValue.pollLast();
            }
            indicesInDecreasingValue.offer(rightCursor);

            if (rightCursor >= windowSize - 1) {
                windowMaxima[rightCursor - windowSize + 1] = numbers[indicesInDecreasingValue.peek()];
            }
        }
        return windowMaxima;
    }
}
