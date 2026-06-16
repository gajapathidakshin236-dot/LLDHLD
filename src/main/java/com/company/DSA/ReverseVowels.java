package com.company.DSA;

/**
 * LeetCode #345 - Reverse Vowels of a String
 * Two pointers; swap on both vowels.
 * Time: O(n)  Space: O(n)
 */
public class ReverseVowels {
    public String reverseVowels(String s) {
        char[] a = s.toCharArray();
        int l = 0, r = a.length - 1;
        while (l < r) {
            while (l < r && !isV(a[l])) l++;
            while (l < r && !isV(a[r])) r--;
            char t = a[l]; a[l] = a[r]; a[r] = t;
            l++; r--;
        }
        return new String(a);
    }
    private boolean isV(char c) {
        c = Character.toLowerCase(c);
        return c=='a'||c=='e'||c=='i'||c=='o'||c=='u';
    }
}
