package com.company.DSA.strings;

/* ============================================================
 *  LeetCode #1768 — Merge Strings Alternately
 * ============================================================
 *  PROBLEM
 *  -------
 *  Take chars alternately from word1 and word2, starting with word1; append
 *  any leftover at the end.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "abc",  "pqr"   → "apbqcr"
 *  Ex2: "ab",   "pqrs"  → "apbqrs"
 *  Ex3: "abcd", "pq"    → "apbqcd"
 *
 *  CONSTRAINTS:  1 <= len <= 100.
 *
 *  HINTS
 *  -----
 *   1. Two pointers, alternate, then append remainder.
 * ============================================================ */
public class MergeStringsAlternately {

    public String mergeAlternately(final String firstWord, final String secondWord) {
        final StringBuilder merged = new StringBuilder(firstWord.length() + secondWord.length());
        int firstCursor  = 0;
        int secondCursor = 0;

        while (firstCursor < firstWord.length() && secondCursor < secondWord.length()) {
            merged.append(firstWord.charAt(firstCursor++))
                  .append(secondWord.charAt(secondCursor++));
        }
        merged.append(firstWord,  firstCursor,  firstWord.length());
        merged.append(secondWord, secondCursor, secondWord.length());
        return merged.toString();
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Walk both strings together; append a pair each step. Then append the
 *  leftover of the longer one via StringBuilder.append(seq, start, end).
 *
 *  Complexity: Time O(n+m), Space O(n+m).
 *  Pattern: zip-merge.
 * ============================================================ */
