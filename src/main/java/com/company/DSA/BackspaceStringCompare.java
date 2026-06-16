package com.company.DSA;

/* ============================================================
 *  LeetCode #844 — Backspace String Compare
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given two strings, return true if they become equal after backspacing the
 *  '#' characters. '#' means "delete previous character".
 *
 *  EXAMPLES
 *  --------
 *  Ex1: s="ab#c", t="ad#c" → true   (both "ac")
 *  Ex2: s="ab##", t="c#d#" → true   (both "")
 *  Ex3: s="a#c",  t="b"     → false
 *  Ex4: s="bxj##tw", t="bxo#j##tw" → true
 *
 *  CONSTRAINTS:  1 <= len <= 200; lowercase letters or '#'.
 *
 *  HINTS
 *  -----
 *   1. Easy version: build the processed strings with a stack and compare.
 *   2. O(1) extra space: walk from the END with two pointers; skip pairs of
 *      ('#', char) by tracking a "skip" counter.
 * ============================================================ */
public class BackspaceStringCompare {
    public boolean backspaceCompare(String s, String t) {
        int i = s.length() - 1, j = t.length() - 1;
        while (i >= 0 || j >= 0) {
            i = nextChar(s, i);
            j = nextChar(t, j);
            char a = i >= 0 ? s.charAt(i) : 0;
            char b = j >= 0 ? t.charAt(j) : 0;
            if (a != b) return false;
            i--; j--;
        }
        return true;
    }
    private int nextChar(String s, int i) {
        int skip = 0;
        while (i >= 0) {
            if (s.charAt(i) == '#') { skip++; i--; }
            else if (skip > 0)      { skip--; i--; }
            else break;
        }
        return i;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Walk both strings right-to-left.  `nextChar` returns the index of the next
 *  EFFECTIVE character (one not erased by '#'). When we see '#', we increment
 *  `skip` and keep scanning; each non-# while skip>0 consumes one skip.
 *  Compare the effective chars at i and j.
 *
 *  Why right-to-left:
 *    '#' removes characters BEFORE it; walking from the right lets us know
 *    immediately whether each char survives.
 *
 *  Complexity: Time O(n+m), Space O(1).
 *  Edge cases: only '#'s → both empty; equal length but different real text.
 *  Pattern: lazy evaluation from end using skip counter.
 * ============================================================ */
