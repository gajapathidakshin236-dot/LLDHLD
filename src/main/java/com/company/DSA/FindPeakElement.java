package com.company.DSA;

/* ============================================================
 *  LeetCode #162 — Find Peak Element
 * ============================================================
 *  PROBLEM
 *  -------
 *  Find ANY peak (greater than its neighbors). nums[-1] = nums[n] = -infinity.
 *  O(log n).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[1,2,3,1]         → 2 (val 3, peak)
 *  Ex2: nums=[1,2,1,3,5,6,4]   → 1 or 5 (either peak ok)
 *  Ex3: nums=[1]               → 0
 *  Ex4: nums=[1,2]             → 1 (second element, since virtual -inf to right)
 *
 *  CONSTRAINTS:  1 <= n <= 1000;  -2^31 <= val <= 2^31 - 1;  adjacent differ.
 *
 *  HINTS
 *  -----
 *   1. If nums[m] < nums[m+1], peak lies in [m+1, r]. Else in [l, m].
 *   2. The function never "stays flat" (constraint), so this always converges.
 * ============================================================ */
public class FindPeakElement {
    public int findPeakElement(int[] nums) {
        int l = 0, r = nums.length - 1;
        while (l < r) {
            int m = l + (r - l) / 2;
            if (nums[m] > nums[m + 1]) r = m;
            else l = m + 1;
        }
        return l;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Climb toward the higher neighbor:
 *    nums[m] vs nums[m+1]:
 *      If nums[m] > nums[m+1] → going downhill to the right → a peak lies in [l..m].
 *      Else                   → uphill → a peak lies in [m+1..r].
 *  Loop until l == r → that index is a peak.
 *
 *  Why this is guaranteed to find SOME peak:
 *    With -infinity boundaries and strictly distinct neighbors, walking uphill
 *    on either side must terminate at a peak.
 *
 *  Complexity: Time O(log n), Space O(1).
 *  Edge cases: single element → peak; two elements → larger is peak.
 *  Pattern: binary search on slope direction. Used in unimodal optimization,
 *           ternary search, golden-section search.
 * ============================================================ */
