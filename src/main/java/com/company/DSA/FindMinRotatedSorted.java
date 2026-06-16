package com.company.DSA;

/* ============================================================
 *  LeetCode #153 — Find Minimum in Rotated Sorted Array
 * ============================================================
 *  PROBLEM
 *  -------
 *  Sorted array rotated at some pivot. Return the minimum value. O(log n).
 *  All values unique.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[3,4,5,1,2]   → 1
 *  Ex2: nums=[4,5,6,7,0,1,2] → 0
 *  Ex3: nums=[11,13,15,17] → 11   (not rotated)
 *  Ex4: nums=[2,1]         → 1
 *
 *  CONSTRAINTS:  n in [1, 5000]; unique values; rotated 1..n times.
 *
 *  HINTS
 *  -----
 *   1. Compare nums[m] with nums[r] (NOT nums[l] — that fails on rotation=0).
 *   2. If nums[m] > nums[r] → min strictly right of m.
 *   3. Else min is at m or to its left.
 * ============================================================ */
public class FindMinRotatedSorted {
    public int findMin(int[] nums) {
        int l = 0, r = nums.length - 1;
        while (l < r) {
            int m = l + (r - l) / 2;
            if (nums[m] > nums[r]) l = m + 1;
            else r = m;
        }
        return nums[l];
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  In a rotated sorted array, the minimum is at the rotation pivot. Comparing
 *  with the RIGHT element decides which half contains it.
 *
 *  Why nums[m] vs nums[r] (not nums[l]):
 *    Using nums[l] fails for the not-rotated case: e.g. [1,2,3]; l=0, m=1, nums[l]=1, nums[m]=2;
 *    nums[m] > nums[l] would push us right although min is at the LEFT.
 *    Comparing with nums[r] avoids this edge.
 *
 *  Complexity: Time O(log n), Space O(1).
 *  Edge cases: not rotated → already sorted, first element is min.
 *  Pattern: half-eliminate via right-anchor. #154 extends to duplicates.
 * ============================================================ */
