package com.company.DSA;

/* ============================================================
 *  LeetCode #35 — Search Insert Position
 * ============================================================
 *  PROBLEM
 *  -------
 *  Sorted array, return index of target. If absent, return where it would be
 *  inserted to keep sorted. O(log n).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[1,3,5,6], target=5 → 2
 *  Ex2: nums=[1,3,5,6], target=2 → 1
 *  Ex3: nums=[1,3,5,6], target=7 → 4   (insert at end)
 *  Ex4: nums=[1,3,5,6], target=0 → 0   (insert at front)
 *
 *  CONSTRAINTS:  1 <= n <= 10^4; sorted distinct; -10^4 <= val <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. This is literally lower bound: first index with nums[i] >= target.
 *   2. Half-open [l, r) template; final l is the answer.
 * ============================================================ */
public class SearchInsertPosition {
    public int searchInsert(int[] nums, int target) {
        int l = 0, r = nums.length;
        while (l < r) {
            int m = l + (r - l) / 2;
            if (nums[m] < target) l = m + 1;
            else r = m;
        }
        return l;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Lower bound = first i s.t. nums[i] >= target.
 *  If target exists → returns its index.
 *  Else → returns the position where it would be inserted to remain sorted.
 *
 *  Why r = nums.length (not length-1):
 *    Allows the answer to be == n when target larger than all elements.
 *
 *  Complexity: Time O(log n), Space O(1).
 *  Edge cases: insert at end (target > max); insert at front; single element.
 *  Pattern: building block for any "first index satisfying predicate" search.
 * ============================================================ */
