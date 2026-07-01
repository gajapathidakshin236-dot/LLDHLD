package com.company.DSA.srivatsanDSA;

import java.util.Arrays;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 13-15
 *  Problem: Valid Anagram
 *  s="anagram", t="nagaram"  Output: true
 *  s="rat",     t="car"      Output: false
 *
 *  APPROACH (from notes):
 *   Method 1: sort both strings; arrays.equals(s.toCharArray(), t.toCharArray()).
 *   Method 2 (counter): int[26] counts;  s++ / t--; all zero → anagram.
 *      for c in s: count[c-'a']++;
 *      for c in t: count[c-'a']--;
 *      for x in count: if (x != 0) return false;
 *      return true;
 * ============================================================ */
public class ValidAnagram {

    public boolean isAnagram(final String first, final String second) {
        if (first.length() != second.length()) {
            return false;
        }
        final int[] frequencyByLetter = new int[26];
        for (int index = 0; index < first.length(); index++) {
            frequencyByLetter[first.charAt(index)  - 'a']++;
            frequencyByLetter[second.charAt(index) - 'a']--;
        }
        for (final int count : frequencyByLetter) {
            if (count != 0) {
                return false;
            }
        }
        return true;
    }

    /** Sorted-strings alternative shown in notes. */
    public boolean isAnagramSorted(final String first, final String second) {
        final char[] firstChars  = first.toCharArray();
        final char[] secondChars = second.toCharArray();
        Arrays.sort(firstChars);
        Arrays.sort(secondChars);
        return Arrays.equals(firstChars, secondChars);
    }
}
