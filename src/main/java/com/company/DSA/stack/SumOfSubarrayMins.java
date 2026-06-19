package com.company.DSA.stack;

import java.util.*;

/* ============================================================
 *  LeetCode #907 — Sum of Subarray Minimums
 * ============================================================
 *  PROBLEM
 *  -------
 *  Sum of min(b) for every contiguous subarray b, mod 1e9+7.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [3,1,2,4] → 17
 *  Ex2: [11,81,94,43,3] → 444
 *
 *  CONSTRAINTS:  1 <= n <= 3*10^4;  1 <= val <= 3*10^4.
 *
 *  HINTS
 *  -----
 *   1. Count CONTRIBUTION of each element via prev/next "less" stacks.
 *   2. Use strict on one side, non-strict on the other to break ties.
 * ============================================================ */
public class SumOfSubarrayMins {

    private static final int MODULUS = 1_000_000_007;

    public int sumSubarrayMins(final int[] numbers) {
        final int length = numbers.length;

        final int[] previousLessIndex = findPreviousLessOrEqualIndex(numbers);
        final int[] nextLessIndex     = findNextLessIndex(numbers);

        long totalSum = 0;
        for (int index = 0; index < length; index++) {
            final long leftSpan  = index - previousLessIndex[index];
            final long rightSpan = nextLessIndex[index] - index;
            totalSum = (totalSum + (long) numbers[index] * leftSpan % MODULUS * rightSpan) % MODULUS;
        }
        return (int) totalSum;
    }

    private int[] findPreviousLessOrEqualIndex(final int[] numbers) {
        final int[] previousIndex = new int[numbers.length];
        final Deque<Integer> stack = new ArrayDeque<>();

        for (int index = 0; index < numbers.length; index++) {
            while (!stack.isEmpty() && numbers[stack.peek()] >= numbers[index]) {
                stack.pop();
            }
            previousIndex[index] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(index);
        }
        return previousIndex;
    }

    private int[] findNextLessIndex(final int[] numbers) {
        final int[] nextIndex = new int[numbers.length];
        final Deque<Integer> stack = new ArrayDeque<>();

        for (int index = numbers.length - 1; index >= 0; index--) {
            while (!stack.isEmpty() && numbers[stack.peek()] > numbers[index]) {
                stack.pop();
            }
            nextIndex[index] = stack.isEmpty() ? numbers.length : stack.peek();
            stack.push(index);
        }
        return nextIndex;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Contribution counting: each element is the min of subarrays in a span
 *  determined by previous/next "less" indices. Compute spans with monotonic stacks.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: "contribution by element" + monotonic stack.
 * ============================================================ */
