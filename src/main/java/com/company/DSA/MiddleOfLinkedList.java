package com.company.DSA;

/**
 * LeetCode #876 - Middle of Linked List
 * Slow/fast. When fast reaches end, slow is at middle (second middle for even).
 * Time: O(n)  Space: O(1)
 */
public class MiddleOfLinkedList {
    public ListNode middleNode(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next; fast = fast.next.next;
        }
        return slow;
    }
}
