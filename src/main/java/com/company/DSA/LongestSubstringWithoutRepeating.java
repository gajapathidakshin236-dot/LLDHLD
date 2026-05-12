package com.company.DSA;


import java.util.*;

public class LongestSubstringWithoutRepeating {

    public static int lengthOfLongestSubstring(String s) {

        int[] hash = new int[256];
        Arrays.fill(hash, -1);

        int left = 0;
        int right = 0;
        int maxLen = 0;
        int n = s.length();

        while (right < n) {

            char ch = s.charAt(right);

            if (hash[ch] >= left) {
                left = hash[ch] + 1;
            }

            hash[ch] = right;

            int len = right - left + 1;

            maxLen = Math.max(maxLen, len);

            right++;
        }

        return maxLen;
    }

    public static void main(String[] args) {

        String s = "cadbzabcd";

        System.out.println(lengthOfLongestSubstring(s));
    }
}