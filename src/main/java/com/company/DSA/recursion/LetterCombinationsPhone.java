package com.company.DSA.recursion;

import java.util.*;

/* ============================================================
 *  LeetCode #17 — Letter Combinations of a Phone Number
 * ============================================================
 *  PROBLEM
 *  -------
 *  All letter combinations a digit string could represent on a phone keypad.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "23" → 9 combos
 *  Ex2: ""   → []
 *  Ex3: "2"  → ["a","b","c"]
 *  Ex4: "7"  → ["p","q","r","s"]
 *
 *  CONSTRAINTS:  0 <= digits.length <= 4.
 *
 *  HINTS
 *  -----
 *   1. Map each digit to its letters; DFS picking one letter per digit position.
 * ============================================================ */
public class LetterCombinationsPhone {

    private static final String[] LETTERS_BY_DIGIT = {
            "", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"
    };

    public List<String> letterCombinations(final String digits) {
        final List<String> results = new ArrayList<>();
        if (digits.isEmpty()) {
            return results;
        }
        backtrack(digits, 0, new StringBuilder(), results);
        return results;
    }

    private void backtrack(final String digits,
                           final int digitIndex,
                           final StringBuilder currentBuffer,
                           final List<String> results) {
        if (digitIndex == digits.length()) {
            results.add(currentBuffer.toString());
            return;
        }
        final String lettersForDigit = LETTERS_BY_DIGIT[digits.charAt(digitIndex) - '0'];

        for (final char letter : lettersForDigit.toCharArray()) {
            currentBuffer.append(letter);
            backtrack(digits, digitIndex + 1, currentBuffer, results);
            currentBuffer.deleteCharAt(currentBuffer.length() - 1);
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Cartesian-product backtracking: 3-4 choices per digit.
 *
 *  Complexity: Time O(3^n * 4^m), Space O(n).
 *  Pattern: small-branching backtracking.
 * ============================================================ */
