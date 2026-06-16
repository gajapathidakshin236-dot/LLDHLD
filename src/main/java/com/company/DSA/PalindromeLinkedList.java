package com.company.DSA;

/**
 * LeetCode #234 - Palindrome Linked List
 * Find mid, reverse 2nd half, compare.
 * Time: O(n)  Space: O(1)
 */
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
