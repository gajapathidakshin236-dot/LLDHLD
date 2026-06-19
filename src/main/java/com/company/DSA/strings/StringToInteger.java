package com.company.DSA.strings;

/* ============================================================
 *  LeetCode #8 — String to Integer (atoi)
 * ============================================================
 *  PROBLEM
 *  -------
 *  Convert a string to a 32-bit int: skip leading spaces, optional sign,
 *  read digits, clamp to int range, return 0 if no digits.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "42"               → 42
 *  Ex2: "   -42"           → -42
 *  Ex3: "4193 with words"  → 4193
 *  Ex4: "words and 987"    → 0
 *  Ex5: "-91283472332"     → Integer.MIN_VALUE  (clamped)
 *
 *  CONSTRAINTS:  0 <= s.length <= 200.
 *
 *  HINTS
 *  -----
 *   1. State machine: spaces → sign → digits → stop.
 *   2. Use long to detect overflow before cast.
 * ============================================================ */
public class StringToInteger {

    public int myAtoi(final String input) {
        final int length = input.length();
        int cursor       = 0;

        cursor = skipLeadingSpaces(input, cursor);
        if (cursor == length) {
            return 0;
        }

        final int sign = readSign(input, cursor);
        if (input.charAt(cursor) == '+' || input.charAt(cursor) == '-') {
            cursor++;
        }
        return readClampedNumber(input, cursor, sign);
    }

    private int skipLeadingSpaces(final String input, int cursor) {
        while (cursor < input.length() && input.charAt(cursor) == ' ') {
            cursor++;
        }
        return cursor;
    }

    private int readSign(final String input, final int cursor) {
        if (cursor < input.length() && input.charAt(cursor) == '-') {
            return -1;
        }
        return 1;
    }

    private int readClampedNumber(final String input, int cursor, final int sign) {
        long accumulated = 0;
        while (cursor < input.length() && Character.isDigit(input.charAt(cursor))) {
            accumulated = accumulated * 10 + (input.charAt(cursor) - '0');
            if (sign * accumulated > Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            }
            if (sign * accumulated < Integer.MIN_VALUE) {
                return Integer.MIN_VALUE;
            }
            cursor++;
        }
        return (int) (sign * accumulated);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Linear pass with three small phases extracted into helpers. Long arithmetic
 *  during accumulation lets us clamp before any int overflow.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: hand-written parser / state machine.
 * ============================================================ */
