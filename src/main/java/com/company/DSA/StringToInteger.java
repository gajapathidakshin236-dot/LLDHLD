package com.company.DSA;

/* ============================================================
 *  LeetCode #8 — String to Integer (atoi)
 * ============================================================
 *  PROBLEM
 *  -------
 *  Convert a string to a 32-bit integer following these rules:
 *    1) Ignore leading spaces.
 *    2) Optional '+' or '-' sign.
 *    3) Read digits until non-digit.
 *    4) Clamp to [INT_MIN, INT_MAX].
 *    5) Return 0 if no digits read.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "42"             → 42
 *  Ex2: "   -42"         → -42
 *  Ex3: "4193 with words" → 4193
 *  Ex4: "words and 987"  → 0
 *  Ex5: "-91283472332"   → -2147483648 (INT_MIN, clamped)
 *  Ex6: "+1"             → 1
 *  Ex7: "  -+12"         → 0  (second sign breaks parsing)
 *
 *  CONSTRAINTS:  0 <= s.length <= 200; ASCII.
 *
 *  HINTS
 *  -----
 *   1. Four phases: spaces → sign → digits → clamp.
 *   2. Use a long to detect overflow before the cast.
 *   3. Stop the moment you hit a non-digit.
 * ============================================================ */
public class StringToInteger {
    public int myAtoi(String s) {
        int i = 0, n = s.length(), sign = 1;
        long res = 0;
        while (i < n && s.charAt(i) == ' ') i++;
        if (i < n && (s.charAt(i) == '+' || s.charAt(i) == '-')) {
            sign = s.charAt(i) == '-' ? -1 : 1;
            i++;
        }
        while (i < n && Character.isDigit(s.charAt(i))) {
            res = res * 10 + (s.charAt(i) - '0');
            if (sign * res >  Integer.MAX_VALUE) return Integer.MAX_VALUE;
            if (sign * res <  Integer.MIN_VALUE) return Integer.MIN_VALUE;
            i++;
        }
        return (int)(sign * res);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Linear state-machine pass: SPACE → SIGN → DIGITS → STOP.
 *  Use long during accumulation to safely detect 32-bit overflow.
 *  Clamp BEFORE writing into int.
 *
 *  Why we don't allow second sign:
 *    Once sign phase is done, the very next char must be a digit; otherwise
 *    we stop. So "+-2" returns 0 because after '+' the next char is '-'.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Edge cases:
 *    - All spaces → 0.
 *    - Sign with no digit → 0.
 *    - Overflow → clamped.
 *    - Leading zeros allowed: "0032" → 32.
 *  Pattern: hand-written parsing / state machine.
 * ============================================================ */
