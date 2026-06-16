package com.company.DSA;

/* ============================================================
 *  LeetCode #34 — Find First and Last Position of Element in Sorted Array
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return [first, last] indices of target in sorted array, or [-1, -1].
 *  Must run in O(log n).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[5,7,7,8,8,10], target=8 → [3,4]
 *  Ex2: nums=[5,7,7,8,8,10], target=6 → [-1,-1]
 *  Ex3: nums=[],             target=0 → [-1,-1]
 *  Ex4: nums=[2,2],          target=2 → [0,1]
 *
 *  CONSTRAINTS:  0 <= n <= 10^5;  -10^9 <= val <= 10^9; sorted ascending.
 *
 *  HINTS
 *  -----
 *   1. Lower bound search → first index where nums[i] >= target.
 *   2. Upper bound search → first index where nums[i] > target.
 *   3. last = upper - 1.  If lower not pointing to target → return [-1,-1].
 * ============================================================ */
public class FindFirstLastPosition {
    public int[] searchRange(int[] nums, int target) {
        int lo = bound(nums, target, true);
        if (lo == nums.length || nums[lo] != target) return new int[]{-1, -1};
        int hi = bound(nums, target, false) - 1;
        return new int[]{lo, hi};
    }
    private int bound(int[] nums, int t, boolean lower) {
        int l = 0, r = nums.length;
        while (l < r) {
            int m = l + (r - l) / 2;
            if (nums[m] > t || (lower && nums[m] == t)) r = m;
            else l = m + 1;
        }
        return l;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Two binary searches:
 *    lower bound — first index i with nums[i] >= target.
 *    upper bound — first index i with nums[i] >  target. Last occurrence = upper-1.
 *
 *  Unified `bound` flag:
 *    On equality:
 *      lower=true  → move right boundary leftward (look further left for equal).
 *      lower=false → move left boundary rightward (look further right).
 *
 *  Why half-open [l, r):
 *    Avoids the off-by-one mistakes when target isn't present — l finally
 *    equals r and is the "insertion point".
 *
 *  Complexity: Time O(log n), Space O(1).
 *  Edge cases: target not present, all same, empty, single element.
 *  Pattern: lower/upper bound — fundamental in sorted-array problems.
 * ============================================================ */
