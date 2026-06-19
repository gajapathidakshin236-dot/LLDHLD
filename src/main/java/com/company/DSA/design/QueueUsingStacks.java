package com.company.DSA.design;

import java.util.*;

/* ============================================================
 *  LeetCode #232 — Implement Queue using Stacks
 * ============================================================
 *  PROBLEM
 *  -------
 *  FIFO queue with two stacks, amortized O(1) per op.
 *
 *  EXAMPLES
 *  --------
 *  push(1); push(2); peek() → 1; pop() → 1; empty() → false.
 *
 *  CONSTRAINTS:  1 <= val <= 9; at most 100 calls of each.
 *
 *  HINTS
 *  -----
 *   1. `in` for push; `out` for pop/peek. Drain in → out when out empty.
 * ============================================================ */
public class QueueUsingStacks {

    private final Deque<Integer> inboundStack  = new ArrayDeque<>();
    private final Deque<Integer> outboundStack = new ArrayDeque<>();

    public void push(final int value) {
        inboundStack.push(value);
    }

    public int pop() {
        moveInboundToOutboundIfNeeded();
        return outboundStack.pop();
    }

    public int peek() {
        moveInboundToOutboundIfNeeded();
        return outboundStack.peek();
    }

    public boolean empty() {
        return inboundStack.isEmpty() && outboundStack.isEmpty();
    }

    private void moveInboundToOutboundIfNeeded() {
        if (outboundStack.isEmpty()) {
            while (!inboundStack.isEmpty()) {
                outboundStack.push(inboundStack.pop());
            }
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Two stacks model a queue. Drain `in` onto `out` lazily — reverses order →
 *  FIFO. Each element moves between stacks at most once → amortized O(1).
 *
 *  Complexity: Time amortized O(1) per op, Space O(n).
 *  Pattern: stacks → queue duality.
 * ============================================================ */
