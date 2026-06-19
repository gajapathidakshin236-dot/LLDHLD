package com.company.DSA.strings;

/* ============================================================
 *  LeetCode #2390 — Removing Stars From a String
 * ============================================================
 *  PROBLEM
 *  -------
 *  Each '*' removes the previous non-'*' char and itself.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "leet**cod*e" → "lecoe"
 *  Ex2: "erase*****"  → ""
 *  Ex3: "abc"         → "abc"
 *
 *  CONSTRAINTS:  1 <= s.length <= 10^5.
 *
 *  HINTS
 *  -----
 *   1. StringBuilder as a stack: append letter, on '*' delete last.
 * ============================================================ */
public class RemovingStarsFromString {

    private static final char STAR_MARKER = '*';

    public String removeStars(final String input) {
        final StringBuilder stack = new StringBuilder();
        for (final char character : input.toCharArray()) {
            if (character == STAR_MARKER) {
                stack.deleteCharAt(stack.length() - 1);
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
 *  Single pass with a StringBuilder as a stack.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: char-stack simplification.
 * ============================================================ */
