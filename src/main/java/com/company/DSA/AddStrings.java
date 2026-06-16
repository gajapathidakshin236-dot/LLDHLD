package com.company.DSA;

/**
 * LeetCode #415 - Add Strings
 * Two pointers from end; carry forward. Build result reversed.
 * Time: O(max(n,m))  Space: O(max(n,m))
 */
public class AddStrings {
    public String addStrings(String a, String b) {
        StringBuilder sb = new StringBuilder();
        int i = a.length() - 1, j = b.length() - 1, c = 0;
        while (i >= 0 || j >= 0 || c > 0) {
            int x = i >= 0 ? a.charAt(i--) - '0' : 0;
            int y = j >= 0 ? b.charAt(j--) - '0' : 0;
            int s = x + y + c;
            sb.append(s % 10);
            c = s / 10;
        }
        return sb.reverse().toString();
    }
}
