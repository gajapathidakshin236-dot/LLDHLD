package com.company.DSA.srivatsanDSA;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 60
 *  Problem: Remove Nth Node From End of Linked List
 *
 *  APPROACH (from notes):
 *    Dummy head pointing to head.
 *    Two pointers slow / fast both at dummy.
 *    Move fast n steps first.
 *    Then advance both until fast.next == null.
 *    Skip slow.next: slow.next = slow.next.next.
 *    Return dummy.next.
 * ============================================================ */
public class RemoveNthFromEnd {

    public ListNode removeNthFromEnd(final ListNode head, final int distanceFromEnd) {
        final ListNode dummyHead = new ListNode(0, head);

        ListNode lagBehind = dummyHead;
        ListNode leadAhead = dummyHead;

        for (int step = 0; step < distanceFromEnd; step++) {
            leadAhead = leadAhead.next;
        }
        while (leadAhead.next != null) {
            lagBehind = lagBehind.next;
            leadAhead = leadAhead.next;
        }
        lagBehind.next = lagBehind.next.next;
        return dummyHead.next;
    }
}
