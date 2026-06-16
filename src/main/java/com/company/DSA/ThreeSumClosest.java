package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #16 — 3Sum Closest
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given int array nums and target, find three integers in nums whose sum
 *  is closest to target. Return the sum.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[-1,2,1,-4], target=1 → 2  ((-1)+2+1)
 *  Ex2: nums=[0,0,0],   target=1 → 0
 *  Ex3: nums=[1,1,1,1], target=0 → 3
 *
 *  CONSTRAINTS:  3 <= n <= 500;  -1000 <= val <= 1000.
 *
 *  HINTS
 *  -----
 *   1. Sort. Fix i; two pointers l, r.
 *   2. Track running best by |sum - target|.
 *   3. If sum < target → l++; else r--.
 * ============================================================ */
public class ThreeSumClosest {
    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int best = nums[0] + nums[1] + nums[2];
        for (int i = 0; i < nums.length - 2; i++) {
            int l = i + 1, r = nums.length - 1;
            while (l < r) {
                int sum = nums[i] + nums[l] + nums[r];
                if (Math.abs(sum - target) < Math.abs(best - target)) best = sum;
                if (sum < target) l++; else r--;
            }
        }
        return best;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Sort + fix-one + two-pointer. After sort, for each i, walk l/r based on
 *  sum vs target. Track the best |diff|. Two pointers naturally converge.
 *
 *  Why move l/r based on sign of (sum - target):
 *    Sum is too small → need a larger element → l++.
 *    Sum too large → r--.
 *
 *  Complexity: Time O(n^2), Space O(1) extra.
 *  Edge cases: ties broken by first found.
 *  Pattern: 2-pointer optimization on a sorted array. Cousin of 3Sum (#15).
 * ============================================================ */
