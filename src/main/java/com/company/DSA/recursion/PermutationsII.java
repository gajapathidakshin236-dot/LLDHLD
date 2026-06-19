package com.company.DSA.recursion;

import java.util.*;

/* ============================================================
 *  LeetCode #47 — Permutations II
 * ============================================================
 *  PROBLEM
 *  -------
 *  Unique permutations of an array with duplicates.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,1,2] → [[1,1,2],[1,2,1],[2,1,1]]
 *  Ex2: [1,2,3] → 6 permutations
 *  Ex3: [2,2,2] → [[2,2,2]]
 *
 *  CONSTRAINTS:  1 <= n <= 8.
 *
 *  HINTS
 *  -----
 *   1. Sort. Skip equal-and-not-previously-used to dedupe.
 * ============================================================ */
public class PermutationsII {

    public List<List<Integer>> permuteUnique(final int[] numbers) {
        Arrays.sort(numbers);
        final List<List<Integer>> results = new ArrayList<>();
        final boolean[]           usedMap = new boolean[numbers.length];
        backtrack(numbers, usedMap, new ArrayList<>(), results);
        return results;
    }

    private void backtrack(final int[] numbers,
                           final boolean[] usedMap,
                           final List<Integer> currentPermutation,
                           final List<List<Integer>> results) {
        if (currentPermutation.size() == numbers.length) {
            results.add(new ArrayList<>(currentPermutation));
            return;
        }
        for (int candidateIndex = 0; candidateIndex < numbers.length; candidateIndex++) {
            if (usedMap[candidateIndex]) {
                continue;
            }
            final boolean isDuplicateAtSameDepth = candidateIndex > 0
                    && numbers[candidateIndex] == numbers[candidateIndex - 1]
                    && !usedMap[candidateIndex - 1];
            if (isDuplicateAtSameDepth) {
                continue;
            }

            usedMap[candidateIndex] = true;
            currentPermutation.add(numbers[candidateIndex]);

            backtrack(numbers, usedMap, currentPermutation, results);

            usedMap[candidateIndex] = false;
            currentPermutation.remove(currentPermutation.size() - 1);
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Sort + canonical-order dedupe: skip duplicates whose previous occurrence
 *  hasn't been used at this depth.
 *
 *  Complexity: Time O(n * n!), Space O(n).
 *  Pattern: backtracking + canonical-order dedupe.
 * ============================================================ */
