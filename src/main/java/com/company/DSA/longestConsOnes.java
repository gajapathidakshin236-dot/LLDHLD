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
}
