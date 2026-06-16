package com.company.DSA;

/**
 * LeetCode #977 - Squares of a Sorted Array
 * Two pointers from ends; the bigger |value| goes to the end of output.
 * Time: O(n)  Space: O(n)
 */
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
