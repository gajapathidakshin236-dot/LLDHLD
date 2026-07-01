package com.company.DSA.srivatsanDSA;

import java.util.HashMap;
import java.util.Map;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 78
 *  Problem: Word Pattern  (LC #290)
 *  Given pattern and a string of space-separated words, return true if there
 *  is a bijection between pattern chars and words.
 *
 *  APPROACH (from notes):
 *    Maintain two HashMaps:
 *      Map<Character, String> charToWord
 *      Map<String, Character> wordToChar
 *    Walk pattern + words in parallel; ensure mappings are consistent.
 *    If either side disagrees with an earlier mapping → false.
 * ============================================================ */
public class StringPatternMatching {

    public boolean wordPattern(final String pattern, final String sentence) {
        final String[] words = sentence.split(" ");
        if (pattern.length() != words.length) {
            return false;
        }

        final Map<Character, String> wordByPatternChar  = new HashMap<>();
        final Map<String, Character> charByWord         = new HashMap<>();

        for (int index = 0; index < pattern.length(); index++) {
            final char   patternChar = pattern.charAt(index);
            final String currentWord = words[index];

            if (wordByPatternChar.containsKey(patternChar)
                    && !wordByPatternChar.get(patternChar).equals(currentWord)) {
                return false;
            }
            if (charByWord.containsKey(currentWord)
                    && charByWord.get(currentWord) != patternChar) {
                return false;
            }
            wordByPatternChar.put(patternChar, currentWord);
            charByWord.put(currentWord,        patternChar);
        }
        return true;
    }
}
