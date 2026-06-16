package com.company.DSA;
public class RemoveDuplicatesFromSortedArrayII {

    public int removeDuplicates(int[] nums) {

        if (nums.length <= 2) return nums.length;

        int write = 2;

        for (int read = 2; read < nums.length; read++) {

            if (nums[read] != nums[write - 2]) {
                nums[write++] = nums[read];
            }
        }

        return write;
    }
//[1,1,2,2,2]
 public int removedup(int[] nums) {
     if (nums.length <= 2) return nums.length;
     int write =2;
     int count=1;
     for (int read = 0; read < nums.length; read++) {
         if(read==0 || nums[read]!=nums[read-1]) {
             count++;
         } else {count=1;}

         if (count <= 2) {
             nums[write] = nums[read];
             write++;
         }
     }

 }
}