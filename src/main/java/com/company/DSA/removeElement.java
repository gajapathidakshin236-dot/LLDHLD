package com.company.DSA;

public class removeElement {

//nums = [2,2,3,3]
    public int removeElement(int[] nums, int val) {

        int n = nums.length;
        int j =0;
        int count=0;
        for (int i = 0; i < n; i++) {
            if(nums[i]!=val) {
                count++;
                int temp=nums[i];//2
                nums[i]=nums[j];
                nums[j]=temp;
                j++;


            }

        }
        return count;
    }


}
