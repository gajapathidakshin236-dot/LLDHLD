package com.company.DSA;

public class LongestSubarayLessK {

    public class LongestSubarrayLessThanK {

        public static void main(String[] args) {

            int[] arr = {2, 5, 1, 7, 10};
            int k = 14;

            int left = 0;
            int right = 0;

            int sum = 0;
            int maxLen = 0;

            int n = arr.length;

            while (right < n) {

                // Add current element
                sum += arr[right];

                // Shrink window if sum > k
                while (sum > k) {
                    sum -= arr[left];
                    left++;
                }

                // Update maximum length
                maxLen = Math.max(maxLen, right - left + 1);

                // Expand window
                right++;
            }

            System.out.println("Longest Length = " + maxLen);
        }
    }
}
