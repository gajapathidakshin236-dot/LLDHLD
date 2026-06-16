package com.company.DSA;

/**
 * LeetCode #1208 - Get Equal Substrings Within Budget
 * Sliding window over absolute cost differences; shrink while sum > maxCost.
 * Time: O(n)  Space: O(1)
 */
public class EqualSubstringsBudget {
    public int equalSubstring(String s, String t, int maxCost) {
        int l = 0, sum = 0, best = 0;
        for (int r = 0; r < s.length(); r++) {
            sum += Math.abs(s.charAt(r) - t.charAt(r));
            while (sum > maxCost) sum -= Math.abs(s.charAt(l) - t.charAt(l++));
            best = Math.max(best, r - l + 1);
        }
        return best;
    }
}
