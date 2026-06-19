package com.company.DSA.recursion;

import java.util.*;

/* ============================================================
 *  LeetCode #77 — Combinations
 * ============================================================
 *  PROBLEM
 *  -------
 *  All combinations of k numbers chosen from 1..n.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: n=4, k=2 → 6 combinations
 *  Ex2: n=1, k=1 → [[1]]
 *
 *  CONSTRAINTS:  1 <= k <= n <= 20.
 *
 *  HINTS
 *  -----
 *   1. Backtrack with `startIndex`. Prune by remaining slots needed.
 * ============================================================ */
public class Combinations {

    public List<List<Integer>> combine(final int upperBound, final int pickCount) {
        final List<List<Integer>> results = new ArrayList<>();
        backtrack(1, upperBound, pickCount, new ArrayList<>(), results);
        return results;
    }

    private void backtrack(final int startNumber,
                           final int upperBound,
                           final int pickCount,
                           final List<Integer> currentCombination,
                           final List<List<Integer>> results) {
        if (currentCombination.size() == pickCount) {
            results.add(new ArrayList<>(currentCombination));
            return;
        }
        final int picksRemaining   = pickCount - currentCombination.size();
        final int lastUsableNumber = upperBound - picksRemaining + 1;

        for (int candidate = startNumber; candidate <= lastUsableNumber; candidate++) {
            currentCombination.add(candidate);
            backtrack(candidate + 1, upperBound, pickCount, currentCombination, results);
            currentCombination.remove(currentCombination.size() - 1);
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Backtrack with monotonic start; prune upper bound so remaining numbers
 *  can fill the combination.
 *
 *  Complexity: Time O(C(n,k) * k), Space O(k).
 *  Pattern: backtracking with combinatorial pruning.
 * ============================================================ */
