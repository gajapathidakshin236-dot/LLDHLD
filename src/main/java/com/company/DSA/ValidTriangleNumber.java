package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #611 — Valid Triangle Number
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given a non-negative int array, count triples (i<j<k) that form a valid
 *  triangle (each pair sum > third side).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [2,2,3,4]    → 3
 *      (2,3,4), (2,3,4), (2,2,3) — count both 2s.
 *  Ex2: [4,2,3,4]    → 4
 *  Ex3: [0,0,0]      → 0
 *
 *  CONSTRAINTS:  1 <= n <= 1000;  0 <= val <= 1000.
 *
 *  HINTS
 *  -----
 *   1. Sort. With a sorted triple a <= b <= c, valid iff a + b > c.
 *   2. Fix the largest side c. Use two-pointer on the remaining range to count.
 *   3. If a + b > c, ALL pairs from l..r-1 paired with r are valid → ans += r-l.
 * ============================================================ */
public class ValidTriangleNumber {
    public int triangleNumber(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length, ans = 0;
        for (int k = n - 1; k >= 2; k--) {
            int l = 0, r = k - 1;
            while (l < r) {
                if (nums[l] + nums[r] > nums[k]) { ans += r - l; r--; }
                else l++;
            }
        }
        return ans;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Sort first. Fix the LARGEST side and use two pointers on smaller sides.
 *  Triangle inequality with sorted sides: only check a + b > c.
 *
 *  Why ans += (r - l) on success:
 *    Sides at positions [l..r-1] paired with nums[r] all satisfy because
 *    nums[i] (>= nums[l]) + nums[r] > nums[k]. So r-l pairs at once.
 *
 *  Why not 3 nested loops:
 *    O(n^3) blows past TLE.
 *
 *  Complexity: Time O(n^2), Space O(1) extra (sort O(log n) stack).
 *  Edge cases: zeros (can't form triangle); fewer than 3 elements → 0.
 *  Pattern: sort + two pointers on smaller pair. Same: 3-sum smaller (#259).
 * ============================================================ */
