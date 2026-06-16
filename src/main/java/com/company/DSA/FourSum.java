package com.company.DSA;

import java.util.*;

/**
 * LeetCode #18 - 4Sum
 * Sort. Two outer loops fix a,b; inner two-pointer for c,d. Skip duplicates carefully.
 * Time: O(n^3)  Space: O(1) extra
 */
public class FourSum {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        Arrays.sort(nums);
        int n = nums.length;
        List<List<Integer>> res = new ArrayList<>();
        for (int a = 0; a < n - 3; a++) {
            if (a > 0 && nums[a] == nums[a - 1]) continue;
            for (int b = a + 1; b < n - 2; b++) {
                if (b > a + 1 && nums[b] == nums[b - 1]) continue;
                long need = (long) target - nums[a] - nums[b];
                int l = b + 1, r = n - 1;
                while (l < r) {
                    int sum = nums[l] + nums[r];
                    if (sum < need) l++;
                    else if (sum > need) r--;
                    else {
                        res.add(Arrays.asList(nums[a], nums[b], nums[l], nums[r]));
                        while (l < r && nums[l] == nums[l + 1]) l++;
                        while (l < r && nums[r] == nums[r - 1]) r--;
                        l++; r--;
                    }
                }
            }
        }
        return res;
    }
}
