package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #18 — 4Sum
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return all UNIQUE quadruples [a,b,c,d] in nums such that a+b+c+d == target.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[1,0,-1,0,-2,2], target=0 → [[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]
 *  Ex2: nums=[2,2,2,2,2],    target=8 → [[2,2,2,2]]
 *  Ex3: nums=[], target=0             → []
 *
 *  CONSTRAINTS:  1 <= n <= 200;  -10^9 <= val/target <= 10^9.
 *
 *  HINTS
 *  -----
 *   1. Sort.
 *   2. Two nested loops over a, b; two pointers for c, d.
 *   3. Skip duplicates at every level to avoid repeats.
 *   4. Use long for sum comparison to avoid int overflow.
 * ============================================================ */
public class FourSum {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        Arrays.sort(nums);
        int n = nums.length;
        List<List<Integer>> res = new ArrayList<>();
        for (int a = 0; a < n - 3; a++) {
            if (a > 0 && nums[a] == nums[a - 1]) continue;
            for (int b = a + 1; b < n - 2; b++) {
                if (b > a + 1 && nums[b] == nums[b - 1]) continue;
                long need = (long) target - nums[a] - nums[b];
                int l = b + 1, r = n - 1;
                while (l < r) {
                    int sum = nums[l] + nums[r];
                    if (sum < need) l++;
                    else if (sum > need) r--;
                    else {
                        res.add(Arrays.asList(nums[a], nums[b], nums[l], nums[r]));
                        while (l < r && nums[l] == nums[l + 1]) l++;
                        while (l < r && nums[r] == nums[r - 1]) r--;
                        l++; r--;
                    }
                }
            }
        }
        return res;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Generalize 3Sum to 4Sum. Sort, then fix a, b in two outer loops; for the
 *  remaining pair use two pointers. Dedupe by skipping duplicates at every
 *  level.
 *
 *  Long arithmetic:
 *    Values can be up to 1e9; sums of four ints can overflow int → use long.
 *
 *  Complexity: Time O(n^3), Space O(1) extra.
 *  Edge cases: n<4 → []; many duplicates → dedupe critical.
 *  Pattern: k-sum recursion. Generalize to k-Sum with recursion + 2-pointer base.
 * ============================================================ */
