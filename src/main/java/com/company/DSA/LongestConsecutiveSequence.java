package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #128 — Longest Consecutive Sequence
 * ============================================================
 *
 *  PROBLEM
 *  -------
 *  Given an unsorted array of integers nums, return the length of the longest
 *  consecutive elements sequence. You must write an algorithm that runs in
 *  O(n) time.
 *
 *  EXAMPLES
 *  --------
 *  Example 1:
 *    Input:  [100,4,200,1,3,2]
 *    Output: 4   ([1,2,3,4])
 *  Example 2:
 *    Input:  [0,3,7,2,5,8,4,6,0,1]
 *    Output: 9   ([0..8])
 *  Example 3:
 *    Input:  []
 *    Output: 0
 *  Example 4:
 *    Input:  [1,2,0,1]
 *    Output: 3   (duplicates don't extend the run further)
 *
 *  CONSTRAINTS
 *  -----------
 *   0 <= nums.length <= 10^5
 *   -10^9 <= nums[i] <= 10^9
 *
 *  HINTS
 *  -----
 *   1. Sorting is O(n log n) — not allowed by the constraint.
 *   2. Put all numbers in a HashSet for O(1) lookup.
 *   3. To avoid restarting a run multiple times, only START counting from a
 *      number x such that x-1 is NOT in the set. That makes each element
 *      visited at most twice → O(n) overall.
 * ============================================================ */
public class LongestConsecutiveSequence {
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int x : nums) set.add(x);
        int best = 0;
        for (int x : set) {
            if (!set.contains(x - 1)) {
                int cur = x, run = 1;
                while (set.contains(cur + 1)) { cur++; run++; }
                best = Math.max(best, run);
            }
        }
        return best;
    }
}

/* ============================================================
 *  APPROACH EXPLAINED
 * ============================================================
 *
 *  Why HashSet + "only from sequence-start":
 *    HashSet gives O(1) membership. If we naively walked forward from every
 *    element, we'd re-count the same runs many times → O(n^2).
 *    The fix: only kick off the walk from the SMALLEST number of each run.
 *    A number x is the smallest of its run iff x-1 is not in the set.
 *
 *  Amortized O(n) reasoning:
 *    Each number is visited at most twice in total:
 *      - once when we check whether x-1 exists (skip if it does);
 *      - once during the forward walk that includes it.
 *    Total operations ≤ 2n.
 *
 *  Step-by-step:
 *    1. Put all nums in a HashSet (dedupes too).
 *    2. For each x in the set:
 *         if (!set.contains(x - 1)):
 *           walk cur = x; cur++; run++ while cur+1 is in set.
 *           update best.
 *    3. Return best.
 *
 *  Why iterate over the set, not over nums?
 *    nums may have duplicates. Iterating the set guarantees each value visited once.
 *
 *  Complexity:
 *    Time:  O(n)
 *    Space: O(n) for the set.
 *
 *  Edge cases:
 *    - Empty array → 0.
 *    - Duplicates → ignored by set.
 *    - All identical → length 1.
 *    - Negatives or large ints → no special handling needed.
 *
 *  Pattern:
 *    "HashSet pivot + walk only from anchors." Generalizes to longest
 *    run / island detection problems where you want O(n) instead of sort.
 * ============================================================ */
