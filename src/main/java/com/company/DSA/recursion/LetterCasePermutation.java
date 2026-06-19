package com.company.DSA.recursion;

import java.util.*;

/* ============================================================
 *  LeetCode #784 — Letter Case Permutation
 * ============================================================
 *  PROBLEM
 *  -------
 *  Toggle case on each letter; digits stay.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "a1b2" → 4 strings
 *  Ex2: "3z4"  → ["3z4","3Z4"]
 *  Ex3: "12345" → ["12345"]
 *
 *  CONSTRAINTS:  1 <= s.length <= 12.
 *
 *  HINTS
 *  -----
 *   1. DFS, branch lower/upper on letters; XOR 32 toggles case.
 * ============================================================ */
public class LetterCasePermutation {

    private static final int ASCII_CASE_TOGGLE_BIT = 32;

    public List<String> letterCasePermutation(final String input) {
        final List<String> results = new ArrayList<>();
        depthFirst(input.toCharArray(), 0, results);
        return results;
    }

    private void depthFirst(final char[] buffer, final int index, final List<String> results) {
        if (index == buffer.length) {
            results.add(new String(buffer));
            return;
        }
        depthFirst(buffer, index + 1, results);

        if (Character.isLetter(buffer[index])) {
            buffer[index] ^= ASCII_CASE_TOGGLE_BIT;
            depthFirst(buffer, index + 1, results);
            buffer[index] ^= ASCII_CASE_TOGGLE_BIT;
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  DFS in-place on a mutable char[]. At each index, recurse unchanged; if it's
 *  a letter, toggle case (XOR 32), recurse, then toggle back.
 *
 *  Complexity: Time O(2^L * n), Space O(n).
 *  Pattern: branchy backtracking with cheap state toggling.
 * ============================================================ */
