package com.company.DSA;

/* ============================================================
 *  LeetCode #12 — Integer to Roman
 * ============================================================
 *  PROBLEM
 *  -------
 *  Convert an integer (1..3999) to its Roman numeral. Subtractive forms
 *  (IV, IX, XL, XC, CD, CM) must be used where applicable.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: 3    → "III"
 *  Ex2: 58   → "LVIII"
 *  Ex3: 1994 → "MCMXCIV"
 *  Ex4: 9    → "IX"
 *  Ex5: 40   → "XL"
 *
 *  CONSTRAINTS:  1 <= num <= 3999.
 *
 *  HINTS
 *  -----
 *   1. Greedy: include the subtractive forms (900, 400, 90, 40, 9, 4) in the table.
 *   2. Walk values from largest to smallest; subtract while possible, append symbol.
 * ============================================================ */
public class IntegerToRoman {
    public String intToRoman(int num) {
        int[] vals = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] syms = {"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vals.length; i++) {
            while (num >= vals[i]) { sb.append(syms[i]); num -= vals[i]; }
        }
        return sb.toString();
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Pre-sorted (descending) value table INCLUDING subtractive forms.
 *  Greedy: at each value, append the symbol while num >= value.
 *
 *  Why include subtractive forms in the table:
 *    Avoids special-casing "IV" vs "IIII" — single greedy loop handles all.
 *
 *  Complexity: O(1) — bounded by 3999.
 *  Edge cases: smallest (1 → "I"), largest (3999 → "MMMCMXCIX").
 *  Pattern: greedy decomposition using a custom denomination set.
 * ============================================================ */
