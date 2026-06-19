package com.company.DSA.recursion;

import java.util.*;

/* ============================================================
 *  LeetCode #90 — Subsets II
 * ============================================================
 *  PROBLEM
 *  -------
 *  All unique subsets of an array with possible duplicates.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,2,2] → [[],[1],[1,2],[1,2,2],[2],[2,2]]
 *  Ex2: [0]     → [[],[0]]
 *  Ex3: [1,1,1] → [[],[1],[1,1],[1,1,1]]
 *
 *  CONSTRAINTS:  1 <= n <= 10.
 *
 *  HINTS
 *  -----
 *   1. Sort. Skip duplicates at the same depth.
 * ============================================================ */
public class SubsetsII {

    public List<List<Integer>> subsetsWithDup(final int[] numbers) {
        Arrays.sort(numbers);
        final List<List<Integer>> results = new ArrayList<>();
        backtrack(numbers, 0, new ArrayList<>(), results);
        return results;
    }

    private void backtrack(final int[] numbers,
                           final int startIndex,
                           final List<Integer> currentSubset,
                           final List<List<Integer>> results) {
        results.add(new ArrayList<>(currentSubset));

        for (int candidateIndex = startIndex; candidateIndex < numbers.length; candidateIndex++) {
            final boolean isDuplicateAtSameDepth = candidateIndex > startIndex
                    && numbers[candidateIndex] == numbers[candidateIndex - 1];
            if (isDuplicateAtSameDepth) {
                continue;
            }

            currentSubset.add(numbers[candidateIndex]);
            backtrack(numbers, candidateIndex + 1, currentSubset, results);
            currentSubset.remove(currentSubset.size() - 1);
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Sort first so duplicates are adjacent. Within the same recursion level
 *  skip equal-to-previous to avoid duplicate subsets.
 *
 *  Complexity: Time O(n * 2^n), Space O(n).
 *  Pattern: sort + skip equals at same level.
 * ============================================================ */
