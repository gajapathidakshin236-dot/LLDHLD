package com.company.DSA.strings;

/* ============================================================
 *  LeetCode #12 — Integer to Roman
 * ============================================================
 *  PROBLEM
 *  -------
 *  Convert an integer in [1..3999] to its Roman numeral form.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: 3 → "III"
 *  Ex2: 58 → "LVIII"
 *  Ex3: 1994 → "MCMXCIV"
 *  Ex4: 9 → "IX"
 *
 *  CONSTRAINTS:  1 <= num <= 3999.
 *
 *  HINTS
 *  -----
 *   1. Greedy with values that INCLUDE subtractive forms (900, 400, 90, 40, 9, 4).
 * ============================================================ */
public class IntegerToRoman {

    private static final int[]    ROMAN_VALUES  = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
    private static final String[] ROMAN_SYMBOLS = { "M",  "CM","D", "CD","C", "XC","L","XL","X","IX","V","IV","I" };

    public String intToRoman(int remaining) {
        final StringBuilder result = new StringBuilder();
        for (int index = 0; index < ROMAN_VALUES.length; index++) {
            while (remaining >= ROMAN_VALUES[index]) {
                result.append(ROMAN_SYMBOLS[index]);
                remaining -= ROMAN_VALUES[index];
            }
        }
        return result.toString();
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Greedy denomination matching. Include subtractive forms in the table so we
 *  don't have to special-case "IV" vs "IIII".
 *
 *  Complexity: Time O(1) (bounded by 3999), Space O(1).
 *  Pattern: greedy decomposition.
 * ============================================================ */
