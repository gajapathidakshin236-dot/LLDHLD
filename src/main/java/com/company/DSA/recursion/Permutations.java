package com.company.DSA.recursion;

import java.util.*;

/* ============================================================
 *  LeetCode #46 — Permutations
 * ============================================================
 *  PROBLEM
 *  -------
 *  All n! permutations of a distinct integer array.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,2,3] → 6 permutations
 *  Ex2: [0,1]   → [[0,1],[1,0]]
 *  Ex3: [1]     → [[1]]
 *
 *  CONSTRAINTS:  1 <= n <= 6.
 *
 *  HINTS
 *  -----
 *   1. Backtrack with a `used` boolean array.
 * ============================================================ */
public class Permutations {

    public List<List<Integer>> permute(final int[] numbers) {
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
 *  Backtrack with used-array. Snapshot when length == n; undo on the way back.
 *
 *  Complexity: Time O(n * n!), Space O(n).
 *  Pattern: backtracking with state + undo.
 * ============================================================ */
