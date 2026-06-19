package com.company.DSA.recursion;

import java.util.*;

/* ============================================================
 *  LeetCode #131 — Palindrome Partitioning
 * ============================================================
 *  PROBLEM
 *  -------
 *  All partitions of s where every substring is a palindrome.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "aab" → [["a","a","b"],["aa","b"]]
 *  Ex2: "a"   → [["a"]]
 *  Ex3: "abc" → [["a","b","c"]]
 *
 *  CONSTRAINTS:  1 <= s.length <= 16.
 *
 *  HINTS
 *  -----
 *   1. Backtrack. For each palindromic prefix, recurse on the rest.
 * ============================================================ */
public class PalindromePartitioning {

    public List<List<String>> partition(final String input) {
        final List<List<String>> results = new ArrayList<>();
        backtrack(input, 0, new ArrayList<>(), results);
        return results;
    }

    private void backtrack(final String input,
                           final int startIndex,
                           final List<String> currentPartition,
                           final List<List<String>> results) {
        if (startIndex == input.length()) {
            results.add(new ArrayList<>(currentPartition));
            return;
        }
        for (int endIndex = startIndex; endIndex < input.length(); endIndex++) {
            if (isPalindromeRange(input, startIndex, endIndex)) {
                currentPartition.add(input.substring(startIndex, endIndex + 1));
                backtrack(input, endIndex + 1, currentPartition, results);
                currentPartition.remove(currentPartition.size() - 1);
            }
        }
    }

    private boolean isPalindromeRange(final String input, int leftCursor, int rightCursor) {
        while (leftCursor < rightCursor) {
            if (input.charAt(leftCursor) != input.charAt(rightCursor)) {
                return false;
            }
            leftCursor++;
            rightCursor--;
        }
        return true;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Backtrack: at index i, try every j >= i; if s[i..j] is a palindrome,
 *  recurse from j+1.
 *
 *  Complexity: Time O(n * 2^n), Space O(n) recursion.
 *  Pattern: backtracking + decision-per-split.
 * ============================================================ */
