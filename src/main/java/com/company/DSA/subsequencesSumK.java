package com.company.DSA;

public class subsequencesSumK {

    public static int func(int ind , int target, int[] nums ) {
        if(target==0) {
            return 1;
        }

        if(target<0 ) {
            return 0;
        }

        // 0
        if(ind<nums.length) {
           return func(ind+1,target-nums[ind],nums) + func(ind+1,target,nums);
        } else {
            return 0;
        }

    }


    // Function to start counting subsequences
    public static int countSubsequenceWithTargetSum(int[] nums, int target) {
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
