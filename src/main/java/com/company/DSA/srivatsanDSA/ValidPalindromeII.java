package com.company.DSA.srivatsanDSA;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 64
 *  Problem: Valid Palindrome II — allow AT MOST one deletion.
 *
 *  APPROACH (from notes):
 *    Two pointers from both ends.
 *    On mismatch, try skipping the LEFT char OR the RIGHT char — if either of
 *    the resulting substrings is a palindrome → return true.
 *    Otherwise → false.
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
