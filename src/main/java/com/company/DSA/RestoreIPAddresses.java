package com.company.DSA;

import java.util.*;

/**
 * LeetCode #93 - Restore IP Addresses
 * Backtrack 4 segments; each segment 1-3 digits, value <=255, no leading zero unless "0".
 * Time: O(1) (bounded)  Space: O(1)
 */
public class RestoreIPAddresses {
    public List<String> restoreIpAddresses(String s) {
        List<String> res = new ArrayList<>();
        bt(s, 0, 0, new StringBuilder(), res);
        return res;
    }
    private void bt(String s, int i, int parts, StringBuilder cur, List<String> res) {
        if (parts == 4) { if (i == s.length()) res.add(cur.substring(0, cur.length() - 1)); return; }
        for (int len = 1; len <= 3 && i + len <= s.length(); len++) {
            String seg = s.substring(i, i + len);
            if ((seg.length() > 1 && seg.charAt(0) == '0') || Integer.parseInt(seg) > 255) continue;
            int prev = cur.length();
            cur.append(seg).append('.');
            bt(s, i + len, parts + 1, cur, res);
            cur.setLength(prev);
        }
    }
}
