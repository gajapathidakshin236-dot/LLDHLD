package com.company.DSA;

import java.util.*;

/**
 * LeetCode #232 - Implement Queue using Stacks
 * Two stacks: in for push, out for pop/peek. When out empty, drain in -> out.
 * Time: amortized O(1)  Space: O(n)
 */
public class QueueUsingStacks {
    Deque<Integer> in = new ArrayDeque<>(), out = new ArrayDeque<>();
    public void push(int x) { in.push(x); }
    public int pop() { peek(); return out.pop(); }
    public int peek() { if (out.isEmpty()) while (!in.isEmpty()) out.push(in.pop()); return out.peek(); }
    public boolean empty() { return in.isEmpty() && out.isEmpty(); }
}
