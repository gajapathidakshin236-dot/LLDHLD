package com.company.DSA;

/* ============================================================
 *  LeetCode #234 — Palindrome Linked List
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given the head of a singly linked list, return true if it's a palindrome.
 *  Bonus: O(n) time + O(1) space.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: 1->2->2->1   → true
 *  Ex2: 1->2         → false
 *  Ex3: 1            → true
 *  Ex4: 1->2->3->2->1 → true
 *
 *  CONSTRAINTS:  1 <= nodes <= 10^5;  0 <= val <= 9.
 *
 *  HINTS
 *  -----
 *   1. Easy version: copy to array, two-pointer. O(n) space.
 *   2. Optimal: find middle, reverse 2nd half in place, compare halves.
 *   3. Restore the list if interview asks; not strictly required.
 * ============================================================ */
public class PalindromeLinkedList {
    public boolean isPalindrome(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next; fast = fast.next.next;
        }
        ListNode prev = null;
        while (slow != null) {
            ListNode nx = slow.next; slow.next = prev; prev = slow; slow = nx;
        }
        ListNode a = head, b = prev;
        while (b != null) { if (a.val != b.val) return false; a = a.next; b = b.next; }
        return true;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Step 1: slow/fast finds middle node.
 *  Step 2: reverse from slow → prev.
 *  Step 3: walk a=head and b=prev; mismatches mean not palindrome.
 *
 *  Why compare against b (reversed half) only:
 *    The first half may be longer by 1 on odd length — that extra middle
 *    is fine to ignore; we stop when b is null.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Edge cases: single node → true; two equal → true; two different → false.
 *  Pattern: middle + reverse + compare = combo of 3 LL primitives.
 * ============================================================ */
