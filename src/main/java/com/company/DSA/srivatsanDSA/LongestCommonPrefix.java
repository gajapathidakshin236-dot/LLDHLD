package com.company.DSA.srivatsanDSA;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 80
 *  Problem: Longest Common Prefix
 *
 *  APPROACH (from notes):
 *    Vertical scan column by column.
 *    Use strs[0] as reference; for each column i, verify every other string has
 *    same character (and isn't shorter). Stop at first mismatch.
 * ============================================================ */
public class LongestCommonPrefix {

    public String longestCommonPrefix(final String[] words) {
        if (words.length == 0) {
            return "";
        }
        final String referenceWord = words[0];

        for (int columnIndex = 0; columnIndex < referenceWord.length(); columnIndex++) {
            final char referenceChar = referenceWord.charAt(columnIndex);
            for (int wordIndex = 1; wordIndex < words.length; wordIndex++) {
                final boolean reachedEnd    = columnIndex == words[wordIndex].length();
                final boolean charMismatch  = !reachedEnd && words[wordIndex].charAt(columnIndex) != referenceChar;
                if (reachedEnd || charMismatch) {
                    return referenceWord.substring(0, columnIndex);
                }
            }
        }
        return referenceWord;
    }
}
