package com.company.DSA;

/**
 * LeetCode #19 - Remove Nth Node From End of List
 * Two-pointer gap of n. Move fast n steps ahead, then advance both until fast is at last node.
 * Dummy node to handle "remove head" cleanly.
 * Time: O(n)  Space: O(1)
 */
public class RemoveNthFromEnd {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode slow = dummy, fast = dummy;
        for (int i = 0; i < n; i++) fast = fast.next;
        while (fast.next != null) { slow = slow.next; fast = fast.next; }
        slow.next = slow.next.next;
        return dummy.next;
    }
}
