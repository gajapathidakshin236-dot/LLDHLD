package com.company.DSA.linkedlist;

import com.company.DSA.common.ListNode;

/* ============================================================
 *  LeetCode #206 — Reverse Linked List
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given the head of a singly linked list, reverse the list and return the new head.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: 1->2->3->4->5  →  5->4->3->2->1
 *  Ex2: 1->2           →  2->1
 *  Ex3: []             →  []
 *  Ex4: 1              →  1
 *
 *  CONSTRAINTS
 *  -----------
 *   0 <= number of nodes <= 5000;  -5000 <= Node.val <= 5000
 *
 *  HINTS
 *  -----
 *   1. Use 3 pointers: previousNode, currentNode, nextNode.
 *   2. Save nextNode BEFORE rewriting currentNode.next.
 *   3. The new head is the old tail (i.e. previousNode when the loop ends).
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

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Walk the list once. At each node, flip its .next pointer to point
 *  to the previously seen node. Track previousNode to remember what to point at.
 *
 *  Why save `nextNode` first:
 *    We're about to overwrite currentNode.next, so we must remember the
 *    original next first; otherwise we'd lose the rest of the list.
 *
 *  Recursive alt:
 *    reverseList(head) calls reverseList(head.next) then head.next.next = head.
 *    Same result but O(n) stack.
 *
 *  Complexity: Time O(n), Space O(1) iterative or O(n) recursive.
 *  Edge cases: null head → returns null; single node → returns itself.
 *  Pattern: foundation for #92 reverse range, #25 reverse in k-group, #143 reorder.
 * ============================================================ */
