package com.company.DSA.linkedlist;

import com.company.DSA.common.ListNode;

/* ============================================================
 *  LeetCode #19 — Remove Nth Node From End of List
 * ============================================================
 *  PROBLEM
 *  -------
 *  Remove the n-th node from the end and return the head. One pass.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: 1->2->3->4->5, n=2  → 1->2->3->5
 *  Ex2: 1, n=1              → []
 *  Ex3: 1->2, n=1           → 1
 *  Ex4: 1->2, n=2           → 2
 *
 *  CONSTRAINTS:  1 <= nodes <= 30;  1 <= n <= nodes.
 *
 *  HINTS
 *  -----
 *   1. Two pointers with gap = n. Advance fast n steps first.
 *   2. Then advance BOTH until fast hits the last node (fast.next == null).
 *   3. Use a dummy head so removing the actual head is uniform.
 * ============================================================ */
public class RemoveNthFromEnd {

    public ListNode removeNthFromEnd(final ListNode head, final int distanceFromEnd) {
        final ListNode headSentinel = new ListNode(0, head);

        ListNode lagBehind = headSentinel;
        ListNode leadAhead = headSentinel;

        for (int step = 0; step < distanceFromEnd; step++) {
            leadAhead = leadAhead.next;
        }

        while (leadAhead.next != null) {
            lagBehind = lagBehind.next;
            leadAhead = leadAhead.next;
        }

        lagBehind.next = lagBehind.next.next;
        return headSentinel.next;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Two pointers, gap = n, then advance together until `leadAhead.next == null`.
 *  `lagBehind` ends at the node BEFORE the one to remove → easy to unlink.
 *
 *  Why dummy head:
 *    The node to remove might be the head itself. Dummy lets the deletion
 *    expression work uniformly even when removing the head.
 *
 *  Why `leadAhead.next != null` not `leadAhead != null`:
 *    We want leadAhead to STOP on the LAST node, so lagBehind sits at the
 *    predecessor of the to-delete node.
 *
 *  Complexity: Time O(n) single pass, Space O(1).
 *  Pattern: gap-of-k two-pointer trick.
 * ============================================================ */
