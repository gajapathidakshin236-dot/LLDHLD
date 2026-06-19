package com.company.DSA.slidingwindow;

/* ============================================================
 *  LeetCode #76 — Minimum Window Substring
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return the shortest substring of s that contains every char of t (with
 *  duplicates). Return "" if none exists.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: s="ADOBECODEBANC", t="ABC" → "BANC"
 *  Ex2: s="a", t="a" → "a"
 *  Ex3: s="a", t="aa" → ""
 *  Ex4: s="ab", t="b" → "b"
 *
 *  CONSTRAINTS:  1 <= s.length, t.length <= 10^5.
 *
 *  HINTS
 *  -----
 *   1. Variable-size sliding window with required/formed counter.
 *   2. Expand right; shrink left whenever window covers t.
 * ============================================================ */
public class MinimumWindowSubstring {

    private static final int ASCII_TABLE_SIZE = 128;

    public String minWindow(final String source, final String pattern) {
        if (source.length() < pattern.length()) {
            return "";
        }

        final int[] requiredCountPerChar = new int[ASCII_TABLE_SIZE];
        int distinctRequiredChars = 0;
        for (final char character : pattern.toCharArray()) {
            if (requiredCountPerChar[character]++ == 0) {
                distinctRequiredChars++;
            }
        }

        final int[] windowCountPerChar = new int[ASCII_TABLE_SIZE];
        int leftCursor          = 0;
        int distinctSatisfied   = 0;
        int bestStart           = 0;
        int bestLength          = Integer.MAX_VALUE;

        for (int rightCursor = 0; rightCursor < source.length(); rightCursor++) {
            final char enteringChar = source.charAt(rightCursor);
            windowCountPerChar[enteringChar]++;

            if (requiredCountPerChar[enteringChar] > 0
                    && windowCountPerChar[enteringChar] == requiredCountPerChar[enteringChar]) {
                distinctSatisfied++;
            }

            while (distinctSatisfied == distinctRequiredChars) {
                final int currentWindowLength = rightCursor - leftCursor + 1;
                if (currentWindowLength < bestLength) {
                    bestLength = currentWindowLength;
                    bestStart  = leftCursor;
                }

                final char leavingChar = source.charAt(leftCursor++);
                windowCountPerChar[leavingChar]--;

                if (requiredCountPerChar[leavingChar] > 0
                        && windowCountPerChar[leavingChar] < requiredCountPerChar[leavingChar]) {
                    distinctSatisfied--;
                }
            }
        }
        return bestLength == Integer.MAX_VALUE ? "" : source.substring(bestStart, bestStart + bestLength);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Sliding window with required/formed counter:
 *    distinctRequiredChars = unique chars in pattern.
 *    distinctSatisfied     = chars in current window meeting their need.
 *  Window valid iff satisfied == required. Each char enters and leaves once.
 *
 *  Complexity: Time O(|s| + |t|), Space O(1) (bounded alphabet).
 *  Pattern: variable-size sliding window with required/formed counter.
 * ============================================================ */
