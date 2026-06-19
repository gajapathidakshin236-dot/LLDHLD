package com.company.DSA.arrays;

import java.util.List;

public class moveZeros {

    public static List<Integer> sss(List<Integer> nums) {
        int j = nums.size()-1;
        int n = nums.size()-1;

        //[0,1,0,1,2,3,4,0] [1,0]
        for(int i=0;i<n;i++) {
            if(nums.get(i)==0) {
                while(nums.get(j)==0) {
                    j=j-1;
                }

                int temp = nums.get(j);
                nums.set(j, 0);
                nums.set(i, temp);
            }
        }

        return nums;

    }



    public void moveZeroes(int[] nums) {

        int j = 0;

        for (int i = 0; i < nums.length; i++) {

            if (nums[i] != 0) {

                int temp = nums[i];
                nums[i] = nums[j];
                nums[j] = temp;

                j++;
            }
        }
    }
}
