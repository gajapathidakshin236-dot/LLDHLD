package com.company.DSA.strings;

/* ============================================================
 *  LeetCode #844 — Backspace String Compare
 * ============================================================
 *  PROBLEM
 *  -------
 *  Two strings; '#' deletes previous char. Are processed strings equal?
 *  O(1) extra space.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: s="ab#c", t="ad#c" → true
 *  Ex2: s="ab##", t="c#d#" → true
 *  Ex3: s="a#c",  t="b"     → false
 *
 *  CONSTRAINTS:  1 <= len <= 200.
 *
 *  HINTS
 *  -----
 *   1. Walk from the END. Use a "skip" counter for '#'.
 * ============================================================ */
public class BackspaceStringCompare {

    private static final char BACKSPACE_MARKER = '#';

    public boolean backspaceCompare(final String firstInput, final String secondInput) {
        int firstCursor  = firstInput.length()  - 1;
        int secondCursor = secondInput.length() - 1;

        while (firstCursor >= 0 || secondCursor >= 0) {
            firstCursor  = nextEffectiveCharIndex(firstInput,  firstCursor);
            secondCursor = nextEffectiveCharIndex(secondInput, secondCursor);

            final char firstChar  = (firstCursor  >= 0) ? firstInput.charAt(firstCursor)   : 0;
            final char secondChar = (secondCursor >= 0) ? secondInput.charAt(secondCursor) : 0;

            if (firstChar != secondChar) {
                return false;
            }
            firstCursor--;
            secondCursor--;
        }
        return true;
    }

    private int nextEffectiveCharIndex(final String input, int cursor) {
        int pendingSkips = 0;
        while (cursor >= 0) {
            if (input.charAt(cursor) == BACKSPACE_MARKER) {
                pendingSkips++;
                cursor--;
            } else if (pendingSkips > 0) {
                pendingSkips--;
                cursor--;
            } else {
                break;
            }
        }
        return cursor;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Walk both strings right-to-left. `nextEffectiveCharIndex` skips over chars
 *  cancelled by '#' counters. Compare the effective chars at each step.
 *
 *  Complexity: Time O(n+m), Space O(1).
 *  Pattern: lazy evaluation from end using skip counter.
 * ============================================================ */
