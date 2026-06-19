package com.company.DSA.common;

/* ============================================================
 *  Shared ListNode helper for singly linked list problems.
 *  Used across linkedlist/ subpackage.
 * ============================================================ */
public class ListNode {
    public int val;
    public ListNode next;
    public ListNode() {}
    public ListNode(int v) { this.val = v; }
    public ListNode(int v, ListNode n) { this.val = v; this.next = n; }
}
