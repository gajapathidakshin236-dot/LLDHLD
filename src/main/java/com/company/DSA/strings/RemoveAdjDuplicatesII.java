package com.company.DSA.strings;

import java.util.*;

/* ============================================================
 *  LeetCode #1209 — Remove All Adjacent Duplicates II
 * ============================================================
 *  PROBLEM
 *  -------
 *  Repeatedly remove k consecutive equal chars; return the result.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: s="abcd",  k=2 → "abcd"
 *  Ex2: s="deeedbbcccbdaa", k=3 → "aa"
 *  Ex3: s="pbbcggttciiippooaais", k=2 → "ps"
 *
 *  CONSTRAINTS:  1 <= s.length <= 10^5; 2 <= k <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. Stack of (char, count). On adjacent match increment; if reaches k → pop.
 * ============================================================ */
public class RemoveAdjDuplicatesII {

    private static class CharRun {
        final char character;
        int        runLength;
        CharRun(final char character, final int runLength) {
            this.character = character;
            this.runLength = runLength;
        }
    }

    public String removeDuplicates(final String input, final int explosionLength) {
        final Deque<CharRun> stack = new ArrayDeque<>();

        for (final char character : input.toCharArray()) {
            if (!stack.isEmpty() && stack.peek().character == character) {
                stack.peek().runLength++;
                if (stack.peek().runLength == explosionLength) {
                    stack.pop();
                }
            } else {
                stack.push(new CharRun(character, 1));
            }
        }

        return rebuildString(stack);
    }

    private String rebuildString(final Deque<CharRun> stack) {
        final StringBuilder reversedOutput = new StringBuilder();
        for (final CharRun run : stack) {
            for (int copy = 0; copy < run.runLength; copy++) {
                reversedOutput.append(run.character);
            }
        }
        return reversedOutput.reverse().toString();
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Run-length-encoded stack. Each frame holds (char, count). When count == k
 *  the frame is annihilated — pop. Handles chain reactions naturally.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: RLE stack.
 * ============================================================ */
