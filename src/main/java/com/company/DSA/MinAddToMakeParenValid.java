package com.company.DSA;

/**
 * LeetCode #921 - Minimum Add to Make Parentheses Valid
 * Track open count. On ')' if open>0 decrement else need++. At end ans = need + open.
 * Time: O(n)  Space: O(1)
 */
public class MinAddToMakeParenValid {
    public int minAddToMakeValid(String s) {
        int open = 0, need = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') open++;
            else if (open > 0) open--;
            else need++;
        }
        return open + need;
    }
}
