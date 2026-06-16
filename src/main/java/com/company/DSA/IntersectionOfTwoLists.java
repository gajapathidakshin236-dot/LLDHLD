package com.company.DSA;

/**
 * LeetCode #160 - Intersection of Two Linked Lists
 * Two pointers; on null switch to the other head. After 2 passes they meet at intersection or null.
 * Time: O(n+m)  Space: O(1)
 */
public class IntersectionOfTwoLists {
    public ListNode getIntersectionNode(ListNode a, ListNode b) {
        ListNode p = a, q = b;
        while (p != q) {
            p = (p == null) ? b : p.next;
            q = (q == null) ? a : q.next;
        }
        return p;
    }
}
