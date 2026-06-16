package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #232 — Implement Queue using Stacks
 * ============================================================
 *  PROBLEM
 *  -------
 *  Implement a FIFO queue (push, pop, peek, empty) using only two stacks.
 *  Amortized O(1) per op.
 *
 *  EXAMPLES
 *  --------
 *  push(1); push(2); peek() → 1; pop() → 1; empty() → false.
 *
 *  CONSTRAINTS:  1 <= val <= 9; at most 100 calls of each.
 *
 *  HINTS
 *  -----
 *   1. Stack `in` for push (LIFO).
 *   2. Stack `out` for pop/peek. When out is empty, drain in → out (reverses order → FIFO).
 *   3. Each element moved between stacks at most once.
 * ============================================================ */
public class QueueUsingStacks {
    Deque<Integer> in = new ArrayDeque<>(), out = new ArrayDeque<>();
    public void push(int x) { in.push(x); }
    public int pop() { peek(); return out.pop(); }
    public int peek() { if (out.isEmpty()) while (!in.isEmpty()) out.push(in.pop()); return out.peek(); }
    public boolean empty() { return in.isEmpty() && out.isEmpty(); }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Two stacks model a queue: pushing onto `in` and popping from `out`.
 *  When `out` is empty, drain ALL of `in` onto `out`. This reverses the order,
 *  giving FIFO behavior with amortized O(1) per operation.
 *
 *  Why amortized O(1):
 *    Each element is pushed and popped a constant number of times across in/out.
 *
 *  Complexity: Push O(1); Pop/Peek amortized O(1); Space O(n).
 *  Edge cases: empty queue → empty() = true; peek on empty undefined.
 *  Pattern: stacks → queue duality. Inverse: queues → stack via two queues.
 * ============================================================ */
