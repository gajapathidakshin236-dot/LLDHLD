package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #525 — Contiguous Array
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given binary array nums, return the maximum length of a contiguous subarray
 *  with an equal number of 0s and 1s.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [0,1]       → 2
 *  Ex2: [0,1,0]     → 2
 *  Ex3: [0,0,1,0,1,1] → 6
 *  Ex4: [1,1,1,0,0,0,1,1] → 8
 *
 *  CONSTRAINTS:  1 <= n <= 10^5; nums[i] in {0,1}.
 *
 *  HINTS
 *  -----
 *   1. Map 0 → -1. Then equal 0s/1s ⇔ subarray sum is 0.
 *   2. Track running sum. If same sum reoccurs at index j → segment (first+1..j) sums to 0.
 *   3. Keep first occurrence of each sum in a HashMap.
 * ============================================================ */
public class ContiguousArray {
    public int findMaxLength(int[] nums) {
        Map<Integer,Integer> first = new HashMap<>();
        first.put(0, -1);
        int sum = 0, best = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += (nums[i] == 0 ? -1 : 1);
            if (first.containsKey(sum)) best = Math.max(best, i - first.get(sum));
            else first.put(sum, i);
        }
        return best;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Substitute 0 → -1. Then subarrays with equal 0/1 count are exactly subarrays
 *  with sum 0. To find the LONGEST such subarray, remember the FIRST index where
 *  each prefix sum occurred; when the same prefix sum reappears, the in-between
 *  segment sums to zero, and its length = i - first_index.
 *
 *  Seed first.put(0, -1):
 *    Represents an empty prefix BEFORE index 0, so a subarray starting at 0
 *    is also counted.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Edge cases: all 1s or all 0s → 0; alternating → length is full n.
 *  Pattern: "prefix sum + first occurrence map." Same: #560, #523, #1248.
 * ============================================================ */
