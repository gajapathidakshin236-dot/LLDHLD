package com.company.DSA;

/* ============================================================
 *  LeetCode #33 — Search in Rotated Sorted Array
 * ============================================================
 *  PROBLEM
 *  -------
 *  Sorted ascending array was rotated at some pivot unknown to you. Find
 *  index of target or -1. Must be O(log n). All values unique.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[4,5,6,7,0,1,2], target=0 → 4
 *  Ex2: nums=[4,5,6,7,0,1,2], target=3 → -1
 *  Ex3: nums=[1],             target=0 → -1
 *  Ex4: nums=[5,1,3],         target=3 → 2
 *
 *  CONSTRAINTS:  1 <= n <= 5000;  -10^4 <= val <= 10^4; unique.
 *
 *  HINTS
 *  -----
 *   1. At any mid, AT LEAST ONE side is "fully sorted" (no rotation point inside it).
 *   2. Identify which side is sorted by comparing nums[l] vs nums[m].
 *   3. If target lies in the sorted side's range, go there; else go to other side.
 * ============================================================ */
public class SearchRotatedSortedArray {
    public int search(int[] nums, int t) {
        int l = 0, r = nums.length - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            if (nums[m] == t) return m;
            if (nums[l] <= nums[m]) { // left half sorted
                if (nums[l] <= t && t < nums[m]) r = m - 1;
                else l = m + 1;
            } else {                  // right half sorted
                if (nums[m] < t && t <= nums[r]) l = m + 1;
                else r = m - 1;
            }
        }
        return -1;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Modified binary search using the property "one half is always sorted":
 *  After rotation, the array splits into two ascending runs. The middle
 *  always falls in one of them, so one of [l..m] or [m..r] is sorted.
 *
 *  Decide which:
 *    if nums[l] <= nums[m] → left half [l..m] sorted
 *    else                  → right half [m..r] sorted
 *
 *  Then decide where to go:
 *    If target lies in the sorted side's value range → go there (binary search).
 *    Else go to the other side (which contains the rotation pivot).
 *
 *  Complexity: Time O(log n), Space O(1).
 *  Edge cases: not rotated → standard binary search; rotated at len-1 → same.
 *  Pattern: "binary search with rotation invariant", #81 (dups), #153, #154.
 * ============================================================ */
