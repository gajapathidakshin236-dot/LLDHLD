package com.company.DSA;

/**
 * LeetCode #28 - Find the Index of the First Occurrence (strStr)
 * Sliding compare. (KMP can speed it up; brute is plenty for interview.)
 * Time: O(n*m) worst  Space: O(1)
 */
public class FindFirstOccurrence {
    public int strStr(String h, String n) {
        int H = h.length(), N = n.length();
        if (N == 0) return 0;
        for (int i = 0; i <= H - N; i++) {
            int j = 0;
            while (j < N && h.charAt(i + j) == n.charAt(j)) j++;
            if (j == N) return i;
        }
        return -1;
    }
}
