package com.company.DSA.stack;

import java.util.*;

/* ============================================================
 *  LeetCode #402 — Remove K Digits
 * ============================================================
 *  PROBLEM
 *  -------
 *  Remove k digits to make the number smallest possible (return as string).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: num="1432219", k=3 → "1219"
 *  Ex2: num="10200",   k=1 → "200"
 *  Ex3: num="10",      k=2 → "0"
 *  Ex4: num="9",       k=1 → "0"
 *
 *  CONSTRAINTS:  1 <= k <= num.length <= 10^5.
 *
 *  HINTS
 *  -----
 *   1. Greedy + monotonic INCREASING stack. Pop while top > current and k > 0.
 *   2. Trim leading zeros at end.
 * ============================================================ */
public class RemoveKDigits {

    public String removeKdigits(final String number, int removalsAllowed) {
        final Deque<Character> digitStack = new ArrayDeque<>();

        for (final char digit : number.toCharArray()) {
            while (!digitStack.isEmpty() && removalsAllowed > 0 && digitStack.peek() > digit) {
                digitStack.pop();
                removalsAllowed--;
            }
            digitStack.push(digit);
        }

        while (removalsAllowed-- > 0 && !digitStack.isEmpty()) {
            digitStack.pop();
        }

        return buildTrimmedResult(digitStack);
    }

    private String buildTrimmedResult(final Deque<Character> digitStack) {
        final StringBuilder reversed = new StringBuilder();
        while (!digitStack.isEmpty()) {
            reversed.append(digitStack.pollLast());
        }

        int firstNonZeroIndex = 0;
        while (firstNonZeroIndex < reversed.length() - 1
                && reversed.charAt(firstNonZeroIndex) == '0') {
            firstNonZeroIndex++;
        }
        final String trimmed = reversed.substring(firstNonZeroIndex);
        return trimmed.isEmpty() ? "0" : trimmed;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Monotonic stack: prefer smaller digits at higher positions. After pass,
 *  pop any remaining k from the top. Trim leading zeros.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: monotonic stack for smallest sequence.
 * ============================================================ */
