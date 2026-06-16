package com.company.DSA;

/* ============================================================
 *  LeetCode #209 — Minimum Size Subarray Sum
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given positive integers nums and target, return the minimal length of a
 *  contiguous subarray whose sum is >= target. If none exists, return 0.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: target=7,  nums=[2,3,1,2,4,3]    → 2  ([4,3])
 *  Ex2: target=4,  nums=[1,4,4]          → 1
 *  Ex3: target=11, nums=[1,1,1,1,1,1,1,1] → 0
 *  Ex4: target=15, nums=[5,1,3,5,10,7,4,9,2,8] → 2
 *
 *  CONSTRAINTS:  1 <= n <= 10^5;  1 <= val <= 10^4;  1 <= target <= 10^9.
 *
 *  HINTS
 *  -----
 *   1. All values positive → sliding window is monotonic → works perfectly.
 *   2. Expand right; once sum >= target, shrink left while still satisfying.
 *   3. Track best window length.
 * ============================================================ */
public class MinSizeSubarraySum {
    public int minSubArrayLen(int target, int[] nums) {
        int l = 0, sum = 0, best = Integer.MAX_VALUE;
        for (int r = 0; r < nums.length; r++) {
            sum += nums[r];
            while (sum >= target) {
                best = Math.min(best, r - l + 1);
                sum -= nums[l++];
            }
        }
        return best == Integer.MAX_VALUE ? 0 : best;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Sliding window with monotonic shrink. Because every element is positive,
 *  adding extends sum and removing shrinks it — predictable behavior.
 *
 *  Each element enters and exits the window AT MOST ONCE → O(n).
 *
 *  Won't work for negatives:
 *    Negatives break monotonicity. Use prefix sum + monotonic deque (#862) instead.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Edge cases: no valid subarray → 0; single element >= target → 1.
 *  Pattern: shrinking window for "minimum size satisfying constraint."
 * ============================================================ */
