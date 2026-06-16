package com.company.DSA;

/* ============================================================
 *  LeetCode #142 — Linked List Cycle II
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return the node where the cycle BEGINS (the entry node), or null if no cycle.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: 3->2->0->-4, tail → index 1 → returns node with val 2
 *  Ex2: 1->2, tail → index 0 → returns node with val 1
 *  Ex3: 1, no cycle → null
 *  Ex4: empty → null
 *
 *  CONSTRAINTS
 *  -----------
 *   0 <= nodes <= 10^4;  -10^5 <= val <= 10^5; pos = -1 or valid index.
 *
 *  HINTS
 *  -----
 *   1. Phase 1: find meeting point with Floyd's slow/fast (cycle exists).
 *   2. Phase 2: reset one pointer to head; move both at 1 step → they meet at start.
 *   3. The math: distances satisfy 2*(a+b) = a+b+L*k → a = (k-1)*L + (L-b).
 * ============================================================ */
public class LinkedListCycleII {
    public ListNode detectCycle(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next; fast = fast.next.next;
            if (slow == fast) {
                ListNode a = head;
                while (a != slow) { a = a.next; slow = slow.next; }
                return a;
            }
        }
        return null;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Floyd's Phase 1 detects a cycle. To find the START, run a second pointer
 *  from head; advance both 1-step; they meet at the cycle entry.
 *
 *  Why this math works:
 *    Let a = distance head → cycle start.
 *    Let b = distance cycle start → meeting point along the cycle.
 *    Let L = cycle length.
 *    At meeting: slow traveled a+b; fast traveled 2(a+b). Difference must be a
 *    multiple of L:  (a+b) = k*L  →  a = k*L - b = (k-1)*L + (L - b)
 *    So from head, walking `a` steps lands you at meeting + (k-1) full laps,
 *    which lands at the cycle start. The other pointer is already mid-cycle;
 *    walking (L - b) brings it to the same cycle start.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Edge cases: no cycle → returns null; cycle of length 1 → start at slow.
 *  Pattern: Floyd's, applied to find duplicate (#287).
 * ============================================================ */
