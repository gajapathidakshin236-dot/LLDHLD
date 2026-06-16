package com.company.DSA;

/**
 * LeetCode #844 - Backspace String Compare
 * Two pointers from end; count skips. Compare effective characters.
 * Time: O(n+m)  Space: O(1)
 */
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
