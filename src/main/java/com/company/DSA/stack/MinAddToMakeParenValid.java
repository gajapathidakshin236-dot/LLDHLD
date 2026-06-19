package com.company.DSA.stack;

/* ============================================================
 *  LeetCode #921 — Minimum Add to Make Parentheses Valid
 * ============================================================
 *  PROBLEM
 *  -------
 *  Min number of brackets to add to make valid.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "())"   → 1
 *  Ex2: "((("   → 3
 *  Ex3: "()"    → 0
 *  Ex4: "()))((" → 4
 *
 *  CONSTRAINTS:  1 <= s.length <= 1000.
 *
 *  HINTS
 *  -----
 *   1. Track open and need. Final = open + need.
 * ============================================================ */
public class MinAddToMakeParenValid {

    public int minAddToMakeValid(final String brackets) {
        int unmatchedOpens         = 0;
        int unmatchedClosesNeeded  = 0;

        for (final char character : brackets.toCharArray()) {
            if (character == '(') {
                unmatchedOpens++;
            } else if (unmatchedOpens > 0) {
                unmatchedOpens--;
            } else {
                unmatchedClosesNeeded++;
            }
        }
        return unmatchedOpens + unmatchedClosesNeeded;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Balance counters: each unmatched paren needs its partner inserted.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: balance counters.
 * ============================================================ */
