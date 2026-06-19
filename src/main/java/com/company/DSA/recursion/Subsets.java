package com.company.DSA.recursion;

import java.util.*;

/* ============================================================
 *  LeetCode #78 — Subsets
 * ============================================================
 *  PROBLEM
 *  -------
 *  All 2^n subsets of a distinct integer array.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,2,3] → 8 subsets
 *  Ex2: [0]     → [[],[0]]
 *  Ex3: [1,2]   → [[],[1],[2],[1,2]]
 *
 *  CONSTRAINTS:  1 <= n <= 10.
 *
 *  HINTS
 *  -----
 *   1. Each element: include or skip.
 * ============================================================ */
public class Subsets {

    public List<List<Integer>> subsets(final int[] numbers) {
        final List<List<Integer>> results = new ArrayList<>();
        backtrack(numbers, 0, new ArrayList<>(), results);
        return results;
    }

    private void backtrack(final int[] numbers,
                           final int decisionIndex,
                           final List<Integer> currentSubset,
                           final List<List<Integer>> results) {
        if (decisionIndex == numbers.length) {
            results.add(new ArrayList<>(currentSubset));
            return;
        }

        currentSubset.add(numbers[decisionIndex]);
        backtrack(numbers, decisionIndex + 1, currentSubset, results);
        currentSubset.remove(currentSubset.size() - 1);

        backtrack(numbers, decisionIndex + 1, currentSubset, results);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Decision-tree recursion: at each index branch INCLUDE / SKIP.
 *
 *  Complexity: Time O(n * 2^n), Space O(n).
 *  Pattern: include/exclude backtracking.
 * ============================================================ */
