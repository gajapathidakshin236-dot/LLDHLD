package com.company.DSA;

/**
 * LeetCode #6 - Zigzag Conversion
 * One StringBuilder per row; walk i, bounce row index between 0..numRows-1.
 * Time: O(n)  Space: O(n)
 */
public class ZigzagConversion {
    public String convert(String s, int numRows) {
        if (numRows == 1) return s;
        StringBuilder[] rows = new StringBuilder[numRows];
        for (int i = 0; i < numRows; i++) rows[i] = new StringBuilder();
        int row = 0, dir = -1;
        for (char c : s.toCharArray()) {
            rows[row].append(c);
            if (row == 0 || row == numRows - 1) dir = -dir;
            row += dir;
        }
        StringBuilder sb = new StringBuilder();
        for (StringBuilder r : rows) sb.append(r);
        return sb.toString();
    }
}
