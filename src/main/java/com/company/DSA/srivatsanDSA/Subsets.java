package com.company.DSA.srivatsanDSA;

import java.util.ArrayList;
import java.util.List;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 83
 *  Problem: Subsets — all 2^n subsets of nums (distinct values).
 *
 *  APPROACH (from notes):
 *    Backtrack with start index.
 *    At each call: snapshot current subset into result.
 *    Then for j in [start, n): pick nums[j], recurse with j + 1, pop.
 *
 *  Alternative shown: iterative — start with [[]], for each x duplicate every
 *  existing subset and append x.
 * ============================================================ */
public class Subsets {

    public List<List<Integer>> subsets(final int[] numbers) {
        final List<List<Integer>> allSubsets = new ArrayList<>();
        backtrack(numbers, 0, new ArrayList<>(), allSubsets);
        return allSubsets;
    }

    private void backtrack(final int[] numbers,
                           final int startIndex,
                           final List<Integer> currentSubset,
                           final List<List<Integer>> allSubsets) {
        allSubsets.add(new ArrayList<>(currentSubset));

        for (int candidateIndex = startIndex; candidateIndex < numbers.length; candidateIndex++) {
            currentSubset.add(numbers[candidateIndex]);
            backtrack(numbers, candidateIndex + 1, currentSubset, allSubsets);
            currentSubset.remove(currentSubset.size() - 1);
        }
    }
}
