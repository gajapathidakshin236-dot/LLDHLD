package com.company.DSA;

public class subarrayProdLessK {
    public int numSubarrayProductLessThanK(int[] nums, int k) {
        if (k <= 1) return 0;

        int left=0;
        int count=0;

        int p=1;
        for(int right=0;right<nums.length;right++) {

          p*=nums[right];

          while (p>=k) {
              p=p/nums[left];
              left++;
          }
          count=right-left+1;
        }

        return count;
    }
}
