package com.company.DSA;

/* ============================================================
 *  LeetCode #704 — Binary Search (template)
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given a sorted ascending nums and target, return its index or -1.
 *  Must be O(log n).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[-1,0,3,5,9,12], target=9 → 4
 *  Ex2: nums=[-1,0,3,5,9,12], target=2 → -1
 *  Ex3: nums=[5],             target=5 → 0
 *  Ex4: nums=[],              target=5 → -1
 *
 *  CONSTRAINTS:  1 <= n <= 10^4;  unique values; sorted ascending.
 *
 *  HINTS
 *  -----
 *   1. Closed interval [l, r] template: while (l <= r) ...
 *   2. mid = l + (r-l)/2 to avoid (l+r) overflow.
 *   3. Eliminate exactly one half each step.
 * ============================================================ */
public class BinarySearch {
    public int search(int[] nums, int target) {
        int l = 0, r = nums.length - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            if (nums[m] == target) return m;
            if (nums[m] < target) l = m + 1;
            else r = m - 1;
        }
        return -1;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Search space is closed [l, r]. Each iteration we check the middle.
 *  If mid is too small, target is to the right → l = m+1 (skip mid).
 *  If too big, r = m-1. We always shrink → can't infinite loop.
 *
 *  Why l + (r-l)/2:
 *    In other languages, (l + r) can overflow. l + (r-l)/2 is safer.
 *
 *  Two common templates:
 *    A) Closed [l,r], while (l<=r), step over mid with ±1.
 *    B) Half-open [l,r), while (l<r), r=m without -1. Used for boundary searches.
 *
 *  Complexity: Time O(log n), Space O(1).
 *  Edge cases: empty (r=-1 → loop doesn't run); single element matches or not.
 *  Pattern: foundation for all search-on-sorted-data problems.
 * ============================================================ */
