package com.company.DSA;

/**
 * LeetCode #925 - Long Pressed Name
 * Two pointers; if mismatch and typed[j]!=typed[j-1] -> false.
 * Time: O(n+m)  Space: O(1)
 */
public class LongPressedName {
    public boolean isLongPressedName(String name, String typed) {
        int i = 0, j = 0;
        while (j < typed.length()) {
            if (i < name.length() && name.charAt(i) == typed.charAt(j)) { i++; j++; }
            else if (j > 0 && typed.charAt(j) == typed.charAt(j - 1)) j++;
            else return false;
        }
        return i == name.length();
    }
}
