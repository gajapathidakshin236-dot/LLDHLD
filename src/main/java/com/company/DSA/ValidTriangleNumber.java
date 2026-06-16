package com.company.DSA;

import java.util.*;

/**
 * LeetCode #611 - Valid Triangle Number
 * Sort. Fix largest side k (descending); two pointers l, r for the other two.
 * If a[l]+a[r] > a[k] all between count; r--. Else l++.
 * Time: O(n^2)  Space: O(1)
 */
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
