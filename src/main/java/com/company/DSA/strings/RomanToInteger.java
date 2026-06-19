package com.company.DSA.strings;

/* ============================================================
 *  LeetCode #13 — Roman to Integer
 * ============================================================
 *  PROBLEM
 *  -------
 *  Convert a valid Roman numeral to integer.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "III"     → 3
 *  Ex2: "IV"      → 4
 *  Ex3: "IX"      → 9
 *  Ex4: "LVIII"   → 58
 *  Ex5: "MCMXCIV" → 1994
 *
 *  CONSTRAINTS:  1 <= s.length <= 15.
 *
 *  HINTS
 *  -----
 *   1. If current < next → SUBTRACT current; else add it.
 * ============================================================ */
public class RomanToInteger {

    private static final int[] VALUE_BY_SYMBOL = new int[128];

    static {
        VALUE_BY_SYMBOL['I'] = 1;
        VALUE_BY_SYMBOL['V'] = 5;
        VALUE_BY_SYMBOL['X'] = 10;
        VALUE_BY_SYMBOL['L'] = 50;
        VALUE_BY_SYMBOL['C'] = 100;
        VALUE_BY_SYMBOL['D'] = 500;
        VALUE_BY_SYMBOL['M'] = 1000;
    }

    public int romanToInt(final String romanLiteral) {
        int total           = 0;
        final int lastIndex = romanLiteral.length() - 1;

        for (int index = 0; index <= lastIndex; index++) {
            final int currentValue = VALUE_BY_SYMBOL[romanLiteral.charAt(index)];
            final int nextValue    = (index < lastIndex)
                    ? VALUE_BY_SYMBOL[romanLiteral.charAt(index + 1)]
                    : 0;

            total += (currentValue < nextValue) ? -currentValue : currentValue;
        }
        return total;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Single left-to-right pass with one-character lookahead: subtract on
 *  ascending pair, add otherwise.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: one-step lookahead transformation.
 * ============================================================ */
