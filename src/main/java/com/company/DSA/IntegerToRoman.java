package com.company.DSA;

/**
 * LeetCode #12 - Integer to Roman
 * Greedy with parallel value/symbol arrays including subtractive forms (900, 400, 90, 40, 9, 4).
 * Time: O(1) (bounded by symbol count)  Space: O(1)
 */
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
