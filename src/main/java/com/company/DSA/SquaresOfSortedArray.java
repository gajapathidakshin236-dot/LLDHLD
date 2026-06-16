package com.company.DSA;

/* ============================================================
 *  LeetCode #977 — Squares of a Sorted Array
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given a sorted (ascending) array nums (may contain negatives), return an
 *  array of the SQUARES of each number, sorted ascending. O(n).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [-4,-1,0,3,10]    → [0,1,9,16,100]
 *  Ex2: [-7,-3,2,3,11]    → [4,9,9,49,121]
 *  Ex3: [1,2,3,4]         → [1,4,9,16]
 *  Ex4: [-5,-3,-1]        → [1,9,25]
 *
 *  CONSTRAINTS:  1 <= n <= 10^4;  -10^4 <= val <= 10^4; sorted ascending.
 *
 *  HINTS
 *  -----
 *   1. Sorting squares of negatives produces small values; sorted positives produce small values.
 *   2. The LARGEST square is always at one of the two ends (most negative or most positive).
 *   3. Two pointers from both ends, fill output from the end.
 * ============================================================ */
public class SquaresOfSortedArray {
    public int[] sortedSquares(int[] nums) {
        int n = nums.length, l = 0, r = n - 1, k = n - 1;
        int[] res = new int[n];
        while (l <= r) {
            int a = nums[l] * nums[l], b = nums[r] * nums[r];
            if (a > b) { res[k--] = a; l++; }
            else       { res[k--] = b; r--; }
        }
        return res;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Two pointers from both ends. The bigger-square element gets placed at the
 *  CURRENT END of the output and that pointer advances inward.
 *
 *  Why this is O(n):
 *    Each pointer moves at most n times. Output is filled exactly once.
 *
 *  Why "sort the squares" is not optimal:
 *    Sorting is O(n log n) — fine but loses the structural advantage of input
 *    being sorted.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Edge cases: all positives → fill from right; all negatives → also from right;
 *              mixed → meets at zero.
 *  Pattern: two-pointer merge of two virtual sorted streams.
 * ============================================================ */
