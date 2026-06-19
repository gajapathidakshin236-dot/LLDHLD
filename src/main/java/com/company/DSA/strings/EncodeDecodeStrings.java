package com.company.DSA.strings;

import java.util.*;

/* ============================================================
 *  LeetCode #271 — Encode and Decode Strings
 * ============================================================
 *  PROBLEM
 *  -------
 *  Round-trip encode/decode a list of strings to/from a single string.
 *  Must handle ANY character content.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: ["hello","world"] → "5#hello5#world" → ["hello","world"]
 *  Ex2: ["", "abc", ""]   → "0#3#abc0#"
 *  Ex3: ["a#b", "c"]      → "3#a#b1#c"
 *
 *  CONSTRAINTS:  1 <= strs.length <= 200; 0 <= strs[i].length <= 200.
 *
 *  HINTS
 *  -----
 *   1. Length-prefix encoding handles any character.
 *   2. Format: "<length>#<word>". Decoder reads length, jumps that many chars.
 * ============================================================ */
public class EncodeDecodeStrings {

    private static final char LENGTH_DELIMITER = '#';

    public String encode(final List<String> strings) {
        final StringBuilder encoded = new StringBuilder();
        for (final String word : strings) {
            encoded.append(word.length())
                   .append(LENGTH_DELIMITER)
                   .append(word);
        }
        return encoded.toString();
    }

    public List<String> decode(final String encoded) {
        final List<String> decodedStrings = new ArrayList<>();
        int cursor = 0;

        while (cursor < encoded.length()) {
            final int delimiterIndex = encoded.indexOf(LENGTH_DELIMITER, cursor);
            final int wordLength     = Integer.parseInt(encoded.substring(cursor, delimiterIndex));
            final int wordStart      = delimiterIndex + 1;
            final int wordEnd        = wordStart + wordLength;

            decodedStrings.add(encoded.substring(wordStart, wordEnd));
            cursor = wordEnd;
        }
        return decodedStrings;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Length-prefix framing: write "len#word" for each string. On decode, read
 *  digits until '#', parse length, then jump exactly that many chars to get
 *  the payload (which may itself contain '#').
 *
 *  Complexity: Time O(N), Space O(N).
 *  Pattern: length-prefix framing — same idea behind HTTP Content-Length.
 * ============================================================ */
