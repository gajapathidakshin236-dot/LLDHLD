package com.company.DSA;

/**
 * LeetCode #1456 - Maximum Number of Vowels in Substring of Given Length
 * Fixed-size sliding window of length k; maintain vowel count.
 * Time: O(n)  Space: O(1)
 */
public class MaxVowelsSubstring {
    public int maxVowels(String s, int k) {
        int cnt = 0, best = 0;
        for (int i = 0; i < s.length(); i++) {
            if (isV(s.charAt(i))) cnt++;
            if (i >= k && isV(s.charAt(i - k))) cnt--;
            if (i >= k - 1) best = Math.max(best, cnt);
        }
        return best;
    }
    private boolean isV(char c) { return c=='a'||c=='e'||c=='i'||c=='o'||c=='u'; }
}
