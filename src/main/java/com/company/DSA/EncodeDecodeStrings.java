package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #271 — Encode and Decode Strings
 * ============================================================
 *
 *  PROBLEM
 *  -------
 *  Design an algorithm to encode a list of strings to a single string, and
 *  decode that single string back into the same list. The encoding must work
 *  for ANY string content (including special characters like '#' or '/').
 *
 *  EXAMPLES
 *  --------
 *  Example 1:
 *    Input:  ["hello","world"]
 *    Encode: "5#hello5#world"
 *    Decode: ["hello","world"]
 *
 *  Example 2:
 *    Input:  ["", "abc", ""]
 *    Encode: "0#3#abc0#"
 *    Decode: ["", "abc", ""]
 *
 *  Example 3:
 *    Input:  ["a#b", "c"]
 *    Encode: "3#a#b1#c"
 *    Decode: ["a#b", "c"]   // '#' inside the string is NOT a separator
 *
 *  CONSTRAINTS
 *  -----------
 *   1 <= strs.length <= 200
 *   0 <= strs[i].length <= 200
 *   Any valid Unicode characters.
 *
 *  HINTS
 *  -----
 *   1. You CANNOT use a single delimiter — input may contain that delimiter.
 *   2. Length-prefix encoding solves this generically.
 *   3. Format: "<length>#<word>". Decoder reads length, jumps that many chars.
 * ============================================================ */
public class EncodeDecodeStrings {
    public String encode(List<String> strs) {
        StringBuilder sb = new StringBuilder();
        for (String s : strs) sb.append(s.length()).append('#').append(s);
        return sb.toString();
    }
    public List<String> decode(String s) {
        List<String> res = new ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            int j = s.indexOf('#', i);
            int len = Integer.parseInt(s.substring(i, j));
            res.add(s.substring(j + 1, j + 1 + len));
            i = j + 1 + len;
        }
        return res;
    }
}

/* ============================================================
 *  APPROACH EXPLAINED
 * ============================================================
 *
 *  Why length-prefix works for ANY input:
 *    A naive delimiter (say, ",") fails when input contains ",". An escape-char
 *    scheme works but is fiddly. Length-prefix is delimiter-free in spirit:
 *    the '#' is only a separator between the length digits and the payload.
 *    Once we read N chars after '#', we don't care what's inside.
 *
 *  Step-by-step:
 *    encode:
 *      For each string s, append `s.length() + "#" + s`. Concatenate all.
 *    decode:
 *      Pointer i = 0.
 *      While i < total:
 *        Find next '#' starting at i → j.
 *        len = parseInt(substring(i, j))
 *        word = substring(j+1, j+1+len)
 *        i = j + 1 + len
 *
 *  Why we read indexOf('#', i) safely:
 *    The only '#' AT THIS POSITION is the one we wrote between length and payload.
 *    Any '#' inside the payload is skipped over because we jump by exact length.
 *
 *  Complexity:
 *    Time:  O(N) where N = total characters across all strings.
 *    Space: O(N) for the output buffer.
 *
 *  Edge cases:
 *    - Empty strings → encoded as "0#" and decoded back to "".
 *    - Strings with '#' inside → preserved by length-jump.
 *    - Empty list → encoded as "" and decoded back to [].
 *
 *  Pattern:
 *    "Length-prefixed framing." Used everywhere — HTTP Content-Length,
 *    TCP framing, Protocol Buffers varint length prefixes, Redis RESP, etc.
 * ============================================================ */
