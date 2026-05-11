package com.company.DSA;

public class subsequencesSumCheck {
    public static boolean func(int ind , int target, int[] nums ) {
        if(target==0) {
            return true;
        }

        if(target<0 ) {
            return false;
        }

        // 0
        if(ind<nums.length) {
            return func(ind+1,target-nums[ind],nums) || func(ind+1,target,nums);
        } else {
            return false;
        }

    }


    // Function to start counting subsequences
    public static boolean countSubsequenceWithTargetSum(int[] nums, int target) {
        return func(0, target, nums);
    }

    // Main function to test the solution
    public static void main(String[] args) {

        int[] nums1 = {1, 2, 3, 4};
        int target = 5;
        System.out.println("Number of subsequences with target sum " + target + ": "
                + countSubsequenceWithTargetSum(nums1, target));
    }
}

