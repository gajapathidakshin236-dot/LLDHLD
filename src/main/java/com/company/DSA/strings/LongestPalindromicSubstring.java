package com.company.DSA.strings;

/* ============================================================
 *  LeetCode #5 — Longest Palindromic Substring
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return the longest substring of s that is a palindrome.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "babad" → "bab" or "aba"
 *  Ex2: "cbbd"  → "bb"
 *  Ex3: "a"     → "a"
 *  Ex4: "forgeeksskeegfor" → "geeksskeeg"
 *
 *  CONSTRAINTS:  1 <= s.length <= 1000.
 *
 *  HINTS
 *  -----
 *   1. Every palindrome has a center: char OR gap between chars.
 *   2. From each center, expand while chars match.
 * ============================================================ */
public class LongestPalindromicSubstring {

    public String longestPalindrome(final String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        int bestStart  = 0;
        int bestLength = 1;

        for (int center = 0; center < input.length(); center++) {
            final int lengthOddCenter  = expandAroundCenter(input, center, center);
            final int lengthEvenCenter = expandAroundCenter(input, center, center + 1);
            final int longestHere      = Math.max(lengthOddCenter, lengthEvenCenter);

            if (longestHere > bestLength) {
                bestLength = longestHere;
                bestStart  = center - (longestHere - 1) / 2;
            }
        }
        return input.substring(bestStart, bestStart + bestLength);
    }

    private int expandAroundCenter(final String input, int leftCursor, int rightCursor) {
        while (leftCursor >= 0
                && rightCursor < input.length()
                && input.charAt(leftCursor) == input.charAt(rightCursor)) {
            leftCursor--;
            rightCursor++;
        }
        return rightCursor - leftCursor - 1;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Try every possible center: 2n - 1 of them (n single chars + n-1 gaps).
 *  Expand around each center while chars on both sides match. Track the
 *  longest seen.
 *
 *  Complexity: Time O(n^2), Space O(1).
 *  Pattern: expand-around-center.
 * ============================================================ */
