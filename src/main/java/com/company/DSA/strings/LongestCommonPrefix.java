package com.company.DSA.strings;

/* ============================================================
 *  LeetCode #14 — Longest Common Prefix
 * ============================================================
 *  PROBLEM
 *  -------
 *  Longest common prefix among an array of strings.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: ["flower","flow","flight"] → "fl"
 *  Ex2: ["dog","racecar","car"]    → ""
 *  Ex3: ["a"]                       → "a"
 *
 *  CONSTRAINTS:  1 <= n <= 200; 0 <= str.length <= 200.
 *
 *  HINTS
 *  -----
 *   1. Vertical scan column by column.
 *   2. Stop at first mismatch OR shortest string end.
 * ============================================================ */
public class LongestCommonPrefix {

    public String longestCommonPrefix(final String[] words) {
        if (words.length == 0) {
            return "";
        }
        final String referenceWord = words[0];

        for (int columnIndex = 0; columnIndex < referenceWord.length(); columnIndex++) {
            final char referenceChar = referenceWord.charAt(columnIndex);

            for (int wordIndex = 1; wordIndex < words.length; wordIndex++) {
                final String otherWord = words[wordIndex];
                final boolean reachedEnd      = columnIndex == otherWord.length();
                final boolean charMismatched  = !reachedEnd && otherWord.charAt(columnIndex) != referenceChar;

                if (reachedEnd || charMismatched) {
                    return referenceWord.substring(0, columnIndex);
                }
            }
        }
        return referenceWord;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Vertical scan against the first word. At each column, every other word
 *  must match (or we've run off its end).
 *
 *  Complexity: Time O(S) total chars, Space O(1).
 *  Pattern: column-wise scan.
 * ============================================================ */
