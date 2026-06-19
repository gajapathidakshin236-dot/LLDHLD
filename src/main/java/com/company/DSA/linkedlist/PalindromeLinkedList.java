package com.company.DSA.linkedlist;

import com.company.DSA.common.ListNode;

/* ============================================================
 *  LeetCode #234 — Palindrome Linked List
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return true if singly linked list reads same forwards and backwards.
 *  Bonus: O(n) time + O(1) space.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: 1->2->2->1     → true
 *  Ex2: 1->2           → false
 *  Ex3: 1              → true
 *  Ex4: 1->2->3->2->1  → true
 *
 *  CONSTRAINTS:  1 <= nodes <= 10^5;  0 <= val <= 9.
 *
 *  HINTS
 *  -----
 *   1. Easy version: copy to array, two-pointer. O(n) space.
 *   2. Optimal: find middle, reverse 2nd half, compare halves.
 * ============================================================ */
public class PalindromeLinkedList {

    public boolean isPalindrome(final ListNode head) {
        final ListNode middleNode       = findMiddle(head);
        final ListNode reversedSecondHalf = reverse(middleNode);
        return compareHalves(head, reversedSecondHalf);
    }

    private ListNode findMiddle(final ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    private ListNode reverse(ListNode head) {
        ListNode previous = null;
        while (head != null) {
            final ListNode next = head.next;
            head.next = previous;
            previous  = head;
            head      = next;
        }
        return previous;
    }

    private boolean compareHalves(ListNode firstHalf, ListNode secondHalfReversed) {
        while (secondHalfReversed != null) {
            if (firstHalf.val != secondHalfReversed.val) {
                return false;
            }
            firstHalf          = firstHalf.next;
            secondHalfReversed = secondHalfReversed.next;
        }
        return true;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Step 1: slow/fast finds middle node.
 *  Step 2: reverse second half starting from middle.
 *  Step 3: walk first-half and reversed-second-half pointers; mismatches mean not palindrome.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: middle + reverse + compare = combo of 3 LL primitives.
 * ============================================================ */
