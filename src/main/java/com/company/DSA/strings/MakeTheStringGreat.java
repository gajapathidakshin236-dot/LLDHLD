package com.company.DSA.strings;

/* ============================================================
 *  LeetCode #1544 — Make The String Great
 * ============================================================
 *  PROBLEM
 *  -------
 *  Adjacent letters with opposite case cancel. Iterate until stable.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "leEeetcode" → "leetcode"
 *  Ex2: "abBAcC"     → ""
 *  Ex3: "s"          → "s"
 *
 *  CONSTRAINTS:  1 <= s.length <= 100.
 *
 *  HINTS
 *  -----
 *   1. Stack pattern; same letter opposite case differs by 32 in ASCII.
 * ============================================================ */
public class MakeTheStringGreat {

    private static final int ASCII_CASE_DIFFERENCE = 32;

    public String makeGood(final String input) {
        final StringBuilder stack = new StringBuilder();
        for (final char character : input.toCharArray()) {
            final int stackSize = stack.length();
            final boolean cancelsWithTop = stackSize > 0
                    && Math.abs(stack.charAt(stackSize - 1) - character) == ASCII_CASE_DIFFERENCE;

            if (cancelsWithTop) {
                stack.deleteCharAt(stackSize - 1);
            } else {
                stack.append(character);
            }
        }
        return stack.toString();
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Stack via StringBuilder. Pop if top differs from current by exactly 32 ASCII;
 *  else push.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: cancellation stack.
 * ============================================================ */
