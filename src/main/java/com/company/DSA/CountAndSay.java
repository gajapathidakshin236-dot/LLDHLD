package com.company.DSA;

/**
 * LeetCode #38 - Count and Say
 * Iteratively describe the previous string by run-length encoding.
 * Time: O(2^n) in length growth  Space: O(2^n)
 */
public class CountAndSay {
    public String countAndSay(int n) {
        String s = "1";
        for (int i = 2; i <= n; i++) {
            StringBuilder sb = new StringBuilder();
            int j = 0;
            while (j < s.length()) {
                int k = j;
                while (k < s.length() && s.charAt(k) == s.charAt(j)) k++;
                sb.append(k - j).append(s.charAt(j));
                j = k;
            }
            s = sb.toString();
        }
        return s;
    }
}
