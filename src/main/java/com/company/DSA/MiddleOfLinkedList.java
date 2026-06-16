package com.company.DSA;

/* ============================================================
 *  LeetCode #876 — Middle of the Linked List
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return the middle node. If there are two middle nodes (even length),
 *  return the SECOND one.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: 1->2->3->4->5    → 3
 *  Ex2: 1->2->3->4->5->6 → 4   (second middle)
 *  Ex3: 1                 → 1
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
    public ListNode middleNode(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next; fast = fast.next.next;
        }
        return slow;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Slow/fast pointers: fast moves twice as fast, so when fast reaches the
 *  end, slow has covered exactly half the distance — the middle.
 *
 *  Why "second middle" on even length:
 *    With this loop condition, after 1->2->3->4: fast=null, slow=3 (1-based 3rd).
 *    For odd 1->2->3->4->5: fast lands on 5, slow=3.
 *    Yields the second middle on even, exact middle on odd.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Edge cases: single node returns itself. Empty: returns null.
 *  Pattern: slow/fast — used in cycle, palindrome check, reverse half.
 * ============================================================ */
