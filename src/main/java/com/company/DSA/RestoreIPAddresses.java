package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #93 — Restore IP Addresses
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given a string s of digits, return all valid IPv4 addresses you can form by
 *  inserting 3 dots, with each segment in [0..255] and no leading zeros (except "0").
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "25525511135" → ["255.255.11.135","255.255.111.35"]
 *  Ex2: "0000"        → ["0.0.0.0"]
 *  Ex3: "101023"      → ["1.0.10.23","1.0.102.3","10.1.0.23","10.10.2.3","101.0.2.3"]
 *  Ex4: "1111"        → ["1.1.1.1"]
 *
 *  CONSTRAINTS:  1 <= s.length <= 20; digits only.
 *
 *  HINTS
 *  -----
 *   1. Backtrack choosing segment lengths 1, 2, or 3.
 *   2. Reject segments with leading zero (unless segment is "0") or value > 255.
 *   3. Stop when 4 parts have been placed AND we've consumed all chars.
 * ============================================================ */
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

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Backtracking with bounded depth (4) and bounded branching (1..3 chars per
 *  segment). Build a candidate IP using StringBuilder. Reject invalid segments
 *  early (leading zero, > 255).
 *
 *  Trailing dot trick:
 *    We append "." after each segment; on success we drop the last dot via
 *    `cur.substring(0, length-1)`. Clean and avoids special-casing the last segment.
 *
 *  Complexity: Time O(1) bounded; <= 3^4 = 81 branches; lots of pruning.
 *  Edge cases: too short (<4) or too long (>12) → no answers.
 *  Pattern: small fixed-depth backtracking with parsing.
 * ============================================================ */
