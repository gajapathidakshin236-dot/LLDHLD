package com.company.DSA.strings;

/* ============================================================
 *  LeetCode #6 — Zigzag Conversion
 * ============================================================
 *  PROBLEM
 *  -------
 *  Lay out s in a zigzag on numRows rows, then read row by row.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "PAYPALISHIRING", 3 → "PAHNAPLSIIGYIR"
 *  Ex2: "PAYPALISHIRING", 4 → "PINALSIGYAHRPI"
 *  Ex3: numRows=1 → string unchanged
 *
 *  CONSTRAINTS:  1 <= s.length <= 1000;  1 <= numRows <= 1000.
 *
 *  HINTS
 *  -----
 *   1. One StringBuilder per row. Bounce direction at top/bottom rows.
 * ============================================================ */
public class ZigzagConversion {

    public String convert(final String input, final int numRows) {
        if (numRows == 1) {
            return input;
        }

        final StringBuilder[] rows = new StringBuilder[numRows];
        for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
            rows[rowIndex] = new StringBuilder();
        }

        int currentRow    = 0;
        int rowDirection  = -1;

        for (final char character : input.toCharArray()) {
            rows[currentRow].append(character);
            if (currentRow == 0 || currentRow == numRows - 1) {
                rowDirection = -rowDirection;
            }
            currentRow += rowDirection;
        }

        final StringBuilder concatenated = new StringBuilder(input.length());
        for (final StringBuilder row : rows) {
            concatenated.append(row);
        }
        return concatenated.toString();
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Simulate the zigzag walk; bounce direction at row 0 and row numRows-1.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: simulation with state machine.
 * ============================================================ */
