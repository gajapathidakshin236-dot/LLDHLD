package com.company.DSA;

import java.util.List;

public class longestConsOnes {

    public static int func(List<Integer> nums,int k) {
        int maxlen = 0,l = 0,r=0, zero=0;
        int n=nums.size();
        while (r<n) {
            if(nums.get(r)==0) {
                zero++;
            }
            while (zero>k) {
                if(nums.get(l)==0){zero--;} // missed to add this line but no issues
                l++;
            }

            maxlen = Math.max(r - l, maxlen);
            r++;
        }
        return maxlen;
    }
    public static void main(String[] args) {

    }


    public static int longestOnes(int[] nums, int k) {

        int left = 0;
        int right = 0;
        int zeros = 0;
        int maxLen = 0;

        while (right < nums.length) {

            // Count zeros
            if (nums[right] == 0) {
                zeros++;
            }

            // Shrink window if zeros exceed k
            while (zeros > k) {

                if (nums[left] == 0) {
                    zeros--;
                }

                left++;
            }

            // Update maximum length
            int len = right - left + 1;
            maxLen = Math.max(maxLen, len);

            right++;
        }

        return maxLen;
    }
}
