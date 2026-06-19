package com.company.DSA.linkedlist;

import com.company.DSA.common.ListNode;

/* ============================================================
 *  LeetCode #876 — Middle of the Linked List
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return the middle node. For even length, return the SECOND middle.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: 1->2->3->4->5    → 3
 *  Ex2: 1->2->3->4->5->6 → 4
 *  Ex3: 1                → 1
 *  Ex4: 1->2             → 2
 *
 *  CONSTRAINTS:  1 <= nodes <= 100;  1 <= val <= 100.
 *
 *  HINTS
 *  -----
 *   1. Slow advances 1, fast advances 2. When fast hits end → slow is middle.
 *   2. Loop condition `fast != null && fast.next != null` returns the second middle on even length.
 * ============================================================ */
public class MiddleOfLinkedList {

    public ListNode middleNode(final ListNode head) {
        ListNode slowRunner = head;
        ListNode fastRunner = head;

        while (fastRunner != null && fastRunner.next != null) {
            slowRunner = slowRunner.next;
            fastRunner = fastRunner.next.next;
        }
        return slowRunner;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Slow/fast pointers: fast moves twice as fast, so when fast reaches the
 *  end, slow has covered exactly half the distance — the middle.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: slow/fast — used in cycle, palindrome check, reverse half.
 * ============================================================ */
