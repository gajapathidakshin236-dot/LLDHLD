package com.company.DSA;

public class productArray {

    //Here's the version that builds separate prefix and suffix arrays first — it's the clearest way to see the structure, and a fine answer to write live before optimizing.


    public static int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] prefix = new int[n];
        int[] suffix = new int[n];
        int[] output = new int[n];

        // prefix[i] = product of everything LEFT of i
        prefix[0] = 1;
        for (int i = 1; i < n; i++) {
            prefix[i] = prefix[i - 1] * nums[i - 1];
        }

        // suffix[i] = product of everything RIGHT of i
        suffix[n - 1] = 1;
        for (int i = n - 2; i >= 0; i--) {
            suffix[i] = suffix[i + 1] * nums[i + 1];
        }

        for(int i=0;i<n;i++){
        output[i]=prefix[i]*suffix[i];}

        return output;
    }
}
