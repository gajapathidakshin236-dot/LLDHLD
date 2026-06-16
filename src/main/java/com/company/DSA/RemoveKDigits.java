package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #402 — Remove K Digits
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given non-negative integer num (as string) and int k, remove k digits so
 *  the resulting number is the smallest possible. Return it as a string.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: num="1432219", k=3 → "1219"
 *  Ex2: num="10200",   k=1 → "200"  (strip leading zero)
 *  Ex3: num="10",      k=2 → "0"   (everything removed → 0)
 *  Ex4: num="9",       k=1 → "0"
 *
 *  CONSTRAINTS:  1 <= k <= num.length <= 10^5; non-negative; no leading zero except "0".
 *
 *  HINTS
 *  -----
 *   1. To minimize, prefer SMALLER digits earlier. Remove a digit that's bigger than the NEXT.
 *   2. Monotonic INCREASING stack — pop while top > current and k > 0.
 *   3. After scan, if k > 0 still, pop from top (remove largest trailing digits).
 *   4. Trim leading zeros. If empty → "0".
 * ============================================================ */
public class RemoveKDigits {
    public String removeKdigits(String num, int k) {
        Deque<Character> st = new ArrayDeque<>();
        for (char c : num.toCharArray()) {
            while (!st.isEmpty() && k > 0 && st.peek() > c) { st.pop(); k--; }
            st.push(c);
        }
        while (k-- > 0 && !st.isEmpty()) st.pop();
        StringBuilder sb = new StringBuilder();
        while (!st.isEmpty()) sb.append(st.pollLast());
        int i = 0;
        while (i < sb.length() - 1 && sb.charAt(i) == '0') i++;
        String res = sb.substring(i);
        return res.isEmpty() ? "0" : res;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Greedy + monotonic stack:
 *    To make the number smallest, prefer removing a digit that's followed by
 *    a smaller one (it makes the high-order position smaller).
 *    Maintain an increasing stack. Each time top > current, pop while k>0.
 *    Any remaining removals at end → pop from top (trailing larger digits).
 *
 *  Trim leading zeros AT END (not during): popping might expose new leading zeros.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Edge cases: k == n.length → "0"; "112" k=1 → "11" (no inversion, pop top).
 *  Pattern: "monotonic stack for smallest sequence." Same: #316, #321, #1081.
 * ============================================================ */
