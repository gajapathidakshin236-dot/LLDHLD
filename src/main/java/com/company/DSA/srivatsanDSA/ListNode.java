package com.company.DSA.srivatsanDSA;

/** Local ListNode used by linked-list problems in srivatsanDSA. */
public class ListNode {
    public int val;
    public ListNode next;
    public ListNode() {}
    public ListNode(final int v) { this.val = v; }
    public ListNode(final int v, final ListNode n) { this.val = v; this.next = n; }
}
