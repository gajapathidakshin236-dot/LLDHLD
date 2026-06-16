package com.company.DSA;

/**
 * LeetCode #25 - Reverse Nodes in k-Group
 * Walk in chunks of k; reverse each chunk in place; stitch the chunks together.
 * Time: O(n)  Space: O(1)
 */
public class ReverseNodesKGroup {
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(0); dummy.next = head;
        ListNode groupPrev = dummy;
        while (true) {
            ListNode kth = groupPrev;
            for (int i = 0; i < k && kth != null; i++) kth = kth.next;
            if (kth == null) break;
            ListNode groupNext = kth.next;
            // reverse [groupPrev.next .. kth]
            ListNode prev = groupNext, cur = groupPrev.next;
            while (cur != groupNext) {
                ListNode nx = cur.next; cur.next = prev; prev = cur; cur = nx;
            }
            ListNode tmp = groupPrev.next;
            groupPrev.next = kth;
            groupPrev = tmp;
        }
        return dummy.next;
    }
}
