package com.company.DSA.stack;

import java.util.*;

/* ============================================================
 *  LeetCode #946 — Validate Stack Sequences
 * ============================================================
 *  PROBLEM
 *  -------
 *  Could pushed/popped be a valid push/pop sequence on an empty stack?
 *
 *  EXAMPLES
 *  --------
 *  Ex1: pushed=[1,2,3,4,5], popped=[4,5,3,2,1] → true
 *  Ex2: pushed=[1,2,3,4,5], popped=[4,3,5,1,2] → false
 *  Ex3: pushed=[1,2], popped=[2,1] → true
 *
 *  CONSTRAINTS:  1 <= n <= 1000.
 *
 *  HINTS
 *  -----
 *   1. Simulate: push each; pop while top equals popped[j].
 *   2. Final stack must be empty.
 * ============================================================ */
public class ValidateStackSequences {

    public boolean validateStackSequences(final int[] pushedSequence, final int[] poppedSequence) {
        final Deque<Integer> simulatedStack = new ArrayDeque<>();
        int popCursor = 0;

        for (final int value : pushedSequence) {
            simulatedStack.push(value);

            while (!simulatedStack.isEmpty() && simulatedStack.peek() == poppedSequence[popCursor]) {
                simulatedStack.pop();
                popCursor++;
            }
        }
        return simulatedStack.isEmpty();
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Simulate with a stack. After each push, greedily pop while top matches
 *  popped[j]. If stack empty at end → valid.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: greedy stack simulation.
 * ============================================================ */
