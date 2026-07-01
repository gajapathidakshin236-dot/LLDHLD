package com.company.DSA.srivatsanDSA;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 56
 *  Problem: Reverse Linked List
 *
 *  APPROACH (from notes):
 *    ListNode prev = null;
 *    ListNode cur  = head;
 *    while (cur != null) {
 *        ListNode next = cur.next;
 *        cur.next = prev;
 *        prev = cur;
 *        cur  = next;
 *    }
 *    return prev;
 *  Notes also mention recursive version: build chain, but iterative is preferred.
 * ============================================================ */
public class ReverseLinkedList {

    public ListNode reverseList(final ListNode head) {
        ListNode previousNode = null;
        ListNode currentNode  = head;

        while (currentNode != null) {
            final ListNode nextNode = currentNode.next;
            currentNode.next = previousNode;
            previousNode     = currentNode;
            currentNode      = nextNode;
        }
        return previousNode;
    }
}
