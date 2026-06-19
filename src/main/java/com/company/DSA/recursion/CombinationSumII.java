package com.company.DSA.recursion;

import java.util.*;

/* ============================================================
 *  LeetCode #40 — Combination Sum II
 * ============================================================
 *  PROBLEM
 *  -------
 *  Unique combinations summing to target. Each candidate used at most once.
 *  Duplicates may exist in input.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [10,1,2,7,6,1,5], target=8 → [[1,1,6],[1,2,5],[1,7],[2,6]]
 *  Ex2: [2,5,2,1,2],      target=5 → [[1,2,2],[5]]
 *  Ex3: [1,1,1],          target=2 → [[1,1]]
 *
 *  CONSTRAINTS:  1 <= n <= 100;  1 <= val <= 50.
 *
 *  HINTS
 *  -----
 *   1. Sort. Backtrack with i+1. Skip equal duplicates at same depth.
 * ============================================================ */
public class CombinationSumII {

    public List<List<Integer>> combinationSum2(final int[] candidates, final int target) {
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
            final boolean isDuplicateAtSameDepth = candidateIndex > startIndex
                    && candidates[candidateIndex] == candidates[candidateIndex - 1];
            if (isDuplicateAtSameDepth) {
                continue;
            }
            if (candidates[candidateIndex] > remainingTarget) {
                break;
            }

            currentCombination.add(candidates[candidateIndex]);
            backtrack(candidates, candidateIndex + 1,
                      remainingTarget - candidates[candidateIndex],
                      currentCombination, results);
            currentCombination.remove(currentCombination.size() - 1);
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Same as #39 but advance to i+1; sort + skip same-depth duplicates.
 *
 *  Complexity: Time O(2^n) worst, Space O(n).
 *  Pattern: backtracking with dedupe-by-position trick.
 * ============================================================ */
