package com.company.DSA.recursion;

import java.util.*;

/* ============================================================
 *  LeetCode #39 — Combination Sum
 * ============================================================
 *  PROBLEM
 *  -------
 *  All unique combinations summing to target. Each candidate may be used unlimited.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: candidates=[2,3,6,7], target=7 → [[2,2,3],[7]]
 *  Ex2: candidates=[2,3,5],  target=8 → [[2,2,2,2],[2,3,3],[3,5]]
 *  Ex3: candidates=[2],      target=1 → []
 *
 *  CONSTRAINTS:  1 <= n <= 30.
 *
 *  HINTS
 *  -----
 *   1. Backtrack with start index; reuse same index for repeats.
 * ============================================================ */
public class CombinationSum {

    public List<List<Integer>> combinationSum(final int[] candidates, final int target) {
        Arrays.sort(candidates);
        final List<List<Integer>> results = new ArrayList<>();
        backtrack(candidates, 0, target, new ArrayList<>(), results);
        return results;
    }

    private void backtrack(final int[] candidates,
                           final int startIndex,
                           final int remainingTarget,
                           final List<Integer> currentCombination,
                           final List<List<Integer>> results) {
        if (remainingTarget == 0) {
            results.add(new ArrayList<>(currentCombination));
            return;
        }
        for (int candidateIndex = startIndex; candidateIndex < candidates.length; candidateIndex++) {
            if (candidates[candidateIndex] > remainingTarget) {
                break;
            }
            currentCombination.add(candidates[candidateIndex]);
            backtrack(candidates, candidateIndex,
                      remainingTarget - candidates[candidateIndex],
                      currentCombination, results);
            currentCombination.remove(currentCombination.size() - 1);
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Backtracking with reusable index (canonical non-decreasing order eliminates
 *  duplicates). Sort enables early break on exceeded remainder.
 *
 *  Complexity: Time O(2^t), Space O(target / min(c)).
 *  Pattern: backtracking with reuse + canonical order.
 * ============================================================ */
