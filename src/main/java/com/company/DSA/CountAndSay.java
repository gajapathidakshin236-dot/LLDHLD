package com.company.DSA;

/* ============================================================
 *  LeetCode #38 — Count and Say
 * ============================================================
 *  PROBLEM
 *  -------
 *  countAndSay(1) = "1". countAndSay(n) is the run-length encoding of countAndSay(n-1):
 *  e.g. "1211" → "one 1, one 2, two 1s" → "111221".
 *  Return countAndSay(n).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: n=1 → "1"
 *  Ex2: n=2 → "11"
 *  Ex3: n=3 → "21"
 *  Ex4: n=4 → "1211"
 *  Ex5: n=5 → "111221"
 *  Ex6: n=6 → "312211"
 *
 *  CONSTRAINTS:  1 <= n <= 30.
 *
 *  HINTS
 *  -----
 *   1. Build iteratively. Don't recurse — n up to 30 produces long strings.
 *   2. Two-pointer run scan: count identical chars in a row, append count + char.
 * ============================================================ */
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

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Repeated run-length encoding. Each iteration: walk runs in `s`, append
 *  "<count><digit>" to a new buffer, then assign back to `s`.
 *
 *  Complexity: Time O(2^n) worst (string roughly doubles), Space O(2^n).
 *  Edge cases: n=1 base case "1".
 *  Pattern: classic RLE; same skill needed for #443 String Compression.
 * ============================================================ */
