package com.company.DSA.recursion;

import java.util.*;

/* ============================================================
 *  LeetCode #216 — Combination Sum III
 * ============================================================
 *  PROBLEM
 *  -------
 *  Pick k distinct numbers from 1..9 summing to n.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: k=3, n=7 → [[1,2,4]]
 *  Ex2: k=3, n=9 → [[1,2,6],[1,3,5],[2,3,4]]
 *  Ex3: k=4, n=1 → []
 *
 *  CONSTRAINTS:  2 <= k <= 9; 1 <= n <= 60.
 *
 *  HINTS
 *  -----
 *   1. Backtrack with start index from 1..9. Prune by remaining target.
 * ============================================================ */
public class CombinationSumIII {

    private static final int MAX_DIGIT = 9;

    public List<List<Integer>> combinationSum3(final int pickCount, final int target) {
        final List<List<Integer>> results = new ArrayList<>();
        backtrack(1, pickCount, target, new ArrayList<>(), results);
        return results;
    }

    private void backtrack(final int startDigit,
                           final int pickCount,
                           final int remainingTarget,
                           final List<Integer> currentCombination,
                           final List<List<Integer>> results) {
        if (currentCombination.size() == pickCount) {
            if (remainingTarget == 0) {
                results.add(new ArrayList<>(currentCombination));
            }
            return;
        }
        for (int candidate = startDigit; candidate <= MAX_DIGIT; candidate++) {
            if (candidate > remainingTarget) {
                break;
            }
            currentCombination.add(candidate);
            backtrack(candidate + 1, pickCount,
                      remainingTarget - candidate,
                      currentCombination, results);
            currentCombination.remove(currentCombination.size() - 1);
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Backtracking on 1..9 with monotonic start.
 *
 *  Complexity: Time bounded by C(9, k).
 *  Pattern: bounded subset enumeration.
 * ============================================================ */
