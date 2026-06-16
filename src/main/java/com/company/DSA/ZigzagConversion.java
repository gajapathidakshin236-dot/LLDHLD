package com.company.DSA;

/* ============================================================
 *  LeetCode #6 — Zigzag Conversion
 * ============================================================
 *  PROBLEM
 *  -------
 *  Write string s in a zigzag pattern on n rows, then read row by row.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: s="PAYPALISHIRING", numRows=3 → "PAHNAPLSIIGYIR"
 *       P   A   H   N
 *       A P L S I I G
 *       Y   I   R
 *  Ex2: s="PAYPALISHIRING", numRows=4 → "PINALSIGYAHRPI"
 *  Ex3: numRows=1 → string unchanged
 *
 *  CONSTRAINTS:  1 <= s.length <= 1000;  1 <= numRows <= 1000.
 *
 *  HINTS
 *  -----
 *   1. Maintain one StringBuilder per row.
 *   2. Track current row + direction. Flip direction at row 0 and row numRows-1.
 *   3. Concatenate the row buffers at the end.
 * ============================================================ */
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

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Simulate the zigzag walk. Direction starts at -1 because the first thing
 *  we'll do at row=0 is flip to +1 (going down). When we hit either boundary
 *  (top or bottom row), flip direction. Append each char to its row buffer.
 *  Finally concatenate the rows.
 *
 *  Why not math-only:
 *    A direct formula exists (compute index per row via period 2*(numRows-1))
 *    but it's error-prone. Simulation is the safe interview answer.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Edge cases: numRows=1 → string unchanged; numRows >= s.length → straight rows.
 *  Pattern: simulation with state machine.
 * ============================================================ */
