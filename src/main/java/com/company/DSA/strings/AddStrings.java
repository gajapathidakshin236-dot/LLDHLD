package com.company.DSA.strings;

/* ============================================================
 *  LeetCode #415 — Add Strings
 * ============================================================
 *  PROBLEM
 *  -------
 *  Sum two non-negative integers represented as strings; return as string.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "11"  + "123"  → "134"
 *  Ex2: "456" + "77"   → "533"
 *  Ex3: "0"   + "0"    → "0"
 *  Ex4: "9999"+ "1"    → "10000"
 *
 *  CONSTRAINTS:  1 <= len <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. Right-to-left, carry forward, append digits to a buffer, reverse.
 * ============================================================ */
public class AddStrings {

    public String addStrings(final String firstNumber, final String secondNumber) {
        final StringBuilder reversedResult = new StringBuilder();
        int firstCursor  = firstNumber.length()  - 1;
        int secondCursor = secondNumber.length() - 1;
        int carryOver    = 0;

        while (firstCursor >= 0 || secondCursor >= 0 || carryOver > 0) {
            final int firstDigit  = (firstCursor  >= 0) ? firstNumber.charAt(firstCursor)   - '0' : 0;
            final int secondDigit = (secondCursor >= 0) ? secondNumber.charAt(secondCursor) - '0' : 0;

            final int digitSum = firstDigit + secondDigit + carryOver;
            reversedResult.append(digitSum % 10);
            carryOver = digitSum / 10;

            firstCursor--;
            secondCursor--;
        }
        return reversedResult.reverse().toString();
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  School addition right-to-left. Continue while EITHER number has digits OR
 *  a pending carry. Build reversed; reverse once at end.
 *
 *  Complexity: Time O(max(n,m)), Space O(max(n,m)).
 *  Pattern: arbitrary-precision arithmetic.
 * ============================================================ */
