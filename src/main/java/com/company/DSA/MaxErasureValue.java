package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #1695 — Maximum Erasure Value
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return the maximum possible sum of a SUBARRAY of nums where all elements
 *  are unique.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [4,2,4,5,6] → 17   ([2,4,5,6])
 *  Ex2: [5,2,1,2,5,2,1,2,5] → 8 ([5,2,1] or [1,2,5])
 *  Ex3: [1,1,1,1] → 1
 *
 *  CONSTRAINTS:  1 <= n <= 10^5;  1 <= val <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. Sliding window with a HashSet of values.
 *   2. Expand right; if duplicate, shrink left until duplicate removed.
 *   3. Maintain running sum and best.
 * ============================================================ */
public class MaxErasureValue {
    public int maximumUniqueSubarray(int[] nums) {
        Set<Integer> set = new HashSet<>();
        int l = 0, sum = 0, best = 0;
        for (int r = 0; r < nums.length; r++) {
            while (set.contains(nums[r])) { set.remove(nums[l]); sum -= nums[l++]; }
            set.add(nums[r]);
            sum += nums[r];
            best = Math.max(best, sum);
        }
        return best;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Sliding window over distinct values. Whenever the right side wants to add
 *  a duplicate, shrink the left side until the duplicate is removed. Sum &
 *  set move with the window. The maximum sum across all valid windows is the answer.
 *
 *  Complexity: Time O(n) — each element enters/leaves at most once.
 *  Space: O(n) — set worst case.
 *  Edge cases: all same → answer is one element; strictly increasing → sum of all.
 *  Pattern: variable window + uniqueness invariant. Same as #3.
 * ============================================================ */
