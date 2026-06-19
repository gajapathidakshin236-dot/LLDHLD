package com.company.DSA.linkedlist;

import com.company.DSA.common.ListNode;

/* ============================================================
 *  LeetCode #141 — Linked List Cycle (detection only)
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return true if the linked list has a cycle, else false. Don't use extra memory.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: 3->2->0->-4, tail connects to node index 1 → true
 *  Ex2: 1->2, tail to index 0 → true
 *  Ex3: 1, no cycle → false
 *  Ex4: empty → false
 *
 *  CONSTRAINTS
 *  -----------
 *   0 <= nodes <= 10^4;  -10^5 <= val <= 10^5;  pos is -1 (no cycle) or valid index.
 *
 *  HINTS
 *  -----
 *   1. HashSet of visited nodes works in O(n) time, O(n) space.
 *   2. Floyd's tortoise-and-hare (slow steps by 1, fast steps by 2) gives O(1) space.
 *   3. If two runners on a closed loop, the faster MUST lap the slower.
 * ============================================================ */
public class LinkedListCycle {

    public boolean hasCycle(final ListNode head) {
        ListNode slowRunner = head;
        ListNode fastRunner = head;

        while (fastRunner != null && fastRunner.next != null) {
            slowRunner = slowRunner.next;
            fastRunner = fastRunner.next.next;

            if (slowRunner == fastRunner) {
                return true;
            }
        }

        return false;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Floyd's cycle detection:
 *    Imagine a circular track. A runner at 2x speed will catch the slow one.
 *    On a straight path with an end, fast hits null first → no cycle.
 *
 *  Why fastRunner.next != null check:
 *    fastRunner.next.next would NPE if fastRunner.next is null.
 *
 *  Termination:
 *    - No cycle: fast reaches null in O(n/2) steps → return false.
 *    - Cycle of length L: they meet in ≤ L iterations after slow enters cycle.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Edge cases: empty (head == null) → false; single node no cycle → false.
 *  Pattern: Floyd's, used in #142 (find start), #287 (find duplicate), #202 (happy number).
 * ============================================================ */
