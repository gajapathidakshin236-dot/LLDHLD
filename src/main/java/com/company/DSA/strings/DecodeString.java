package com.company.DSA.strings;

import java.util.*;

/* ============================================================
 *  LeetCode #394 — Decode String
 * ============================================================
 *  PROBLEM
 *  -------
 *  Decode "k[encoded_string]" where the encoded string is repeated k times.
 *  Brackets nest.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "3[a]2[bc]"     → "aaabcbc"
 *  Ex2: "3[a2[c]]"      → "accaccacc"
 *  Ex3: "2[abc]3[cd]ef" → "abcabccdcdcdef"
 *  Ex4: "abc3[cd]xyz"   → "abccdcdcdxyz"
 *
 *  CONSTRAINTS:  1 <= s.length <= 30; k <= 300.
 *
 *  HINTS
 *  -----
 *   1. Two stacks: counts and pending strings.
 *   2. On '[' push current pair, reset. On ']' pop and repeat.
 * ============================================================ */
public class DecodeString {

    public String decodeString(final String encoded) {
        final Deque<Integer>       repeatCountStack = new ArrayDeque<>();
        final Deque<StringBuilder> outerStringStack = new ArrayDeque<>();

        StringBuilder currentInnerString = new StringBuilder();
        int           pendingRepeatCount = 0;

        for (final char character : encoded.toCharArray()) {
            if (Character.isDigit(character)) {
                pendingRepeatCount = pendingRepeatCount * 10 + (character - '0');
                continue;
            }
            if (character == '[') {
                repeatCountStack.push(pendingRepeatCount);
                outerStringStack.push(currentInnerString);
                pendingRepeatCount = 0;
                currentInnerString = new StringBuilder();
                continue;
            }
            if (character == ']') {
                final int           repetitions   = repeatCountStack.pop();
                final StringBuilder outerString   = outerStringStack.pop();
                for (int repeat = 0; repeat < repetitions; repeat++) {
                    outerString.append(currentInnerString);
                }
                currentInnerString = outerString;
                continue;
            }
            currentInnerString.append(character);
        }
        return currentInnerString.toString();
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Iterative two-stack: counts and pending outer strings. On '[', save current
 *  context and start fresh; on ']', expand inner by the saved count and
 *  concatenate to the saved outer.
 *
 *  Complexity: Time O(maxK * n), Space O(n).
 *  Pattern: stack-of-frames for nestable structures.
 * ============================================================ */
