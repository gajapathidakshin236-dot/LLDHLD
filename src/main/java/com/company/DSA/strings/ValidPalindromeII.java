package com.company.DSA.strings;

/* ============================================================
 *  LeetCode #680 — Valid Palindrome II
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return true if string can become a palindrome by deleting AT MOST one character.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "aba"   → true
 *  Ex2: "abca"  → true (delete 'b' or 'c')
 *  Ex3: "abc"   → false
 *  Ex4: "deeee" → true
 *
 *  CONSTRAINTS:  1 <= s.length <= 10^5; lowercase English.
 *
 *  HINTS
 *  -----
 *   1. Two pointers. On mismatch try skipping LEFT or RIGHT.
 * ============================================================ */
public class ValidPalindromeII {

    public boolean validPalindrome(final String input) {
        int leftCursor  = 0;
        int rightCursor = input.length() - 1;

        while (leftCursor < rightCursor) {
            if (input.charAt(leftCursor) != input.charAt(rightCursor)) {
                return isPalindromeInRange(input, leftCursor + 1, rightCursor)
                    || isPalindromeInRange(input, leftCursor,     rightCursor - 1);
            }
            leftCursor++;
            rightCursor--;
        }
        return true;
    }

    private boolean isPalindromeInRange(final String input, int leftCursor, int rightCursor) {
        while (leftCursor < rightCursor) {
            if (input.charAt(leftCursor) != input.charAt(rightCursor)) {
                return false;
            }
            leftCursor++;
            rightCursor--;
        }
        return true;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Plain two-pointer scan. On first mismatch you have two choices — try
 *  skipping the left char or the right char. Either resulting range must be
 *  a palindrome.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: two pointers + bounded retry.
 * ============================================================ */
