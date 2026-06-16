package com.company.DSA;

public class FindPivotIndex {

    public int pivotIndex(int[] nums) {

        int total = 0;

        for (int n : nums) total += n;

        int left = 0;

        for (int i = 0; i < nums.length; i++) {

            int right = total - left - nums[i];

            if (left == right) return i;

            left += nums[i];
        }

        return -1;
    } //[1,1]
}
