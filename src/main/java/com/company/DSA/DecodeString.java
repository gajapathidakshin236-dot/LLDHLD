package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #394 — Decode String
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given encoded form k[encoded_string], decode it. The encoded string is
 *  repeated k times. Brackets nest.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "3[a]2[bc]"      → "aaabcbc"
 *  Ex2: "3[a2[c]]"       → "accaccacc"
 *  Ex3: "2[abc]3[cd]ef"  → "abcabccdcdcdef"
 *  Ex4: "abc3[cd]xyz"    → "abccdcdcdxyz"
 *
 *  CONSTRAINTS:  1 <= s.length <= 30; k <= 300; digits + letters + brackets only.
 *
 *  HINTS
 *  -----
 *   1. Brackets nest → stack of frames.
 *   2. Two stacks: counts and previously-built strings.
 *   3. On '[', push current count and current string; reset both.
 *   4. On ']', pop count and previous string; previous + (current * count).
 * ============================================================ */
public class DecodeString {
    public String decodeString(String s) {
        Deque<Integer> counts = new ArrayDeque<>();
        Deque<StringBuilder> strs = new ArrayDeque<>();
        StringBuilder cur = new StringBuilder();
        int k = 0;
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) k = k * 10 + (c - '0');
            else if (c == '[') { counts.push(k); strs.push(cur); k = 0; cur = new StringBuilder(); }
            else if (c == ']') {
                int rep = counts.pop();
                StringBuilder prev = strs.pop();
                for (int i = 0; i < rep; i++) prev.append(cur);
                cur = prev;
            } else cur.append(c);
        }
        return cur.toString();
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Iterative with two stacks because brackets can NEST. At '[', stash the
 *  current half-built outer string AND the multiplier; start fresh. At ']',
 *  expand the inner and concatenate to the popped outer.
 *
 *  Why k can be multi-digit:
 *    Numbers like "30[a]" appear; build k = k*10 + digit until first non-digit.
 *
 *  Recursive alt:
 *    A single function with an index pointer; recurse on '['. Same complexity,
 *    elegant when comfortable with recursion-with-shared-state.
 *
 *  Complexity: Time O(maxK * n), Space O(n).
 *  Edge cases: no brackets ("abc") → straight copy; deeply nested ok.
 *  Pattern: stack-of-frames for nestable structures. Same idea as Basic Calculator
 *           (#224) and expression evaluation.
 * ============================================================ */
