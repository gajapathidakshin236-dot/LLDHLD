package com.company.DSA;

/* ============================================================
 *  LeetCode #21 — Merge Two Sorted Lists
 * ============================================================
 *  PROBLEM
 *  -------
 *  Merge two sorted singly linked lists and return the head of the merged
 *  sorted list. Splice nodes in place — do not create new ones.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: l1=1->2->4, l2=1->3->4 → 1->1->2->3->4->4
 *  Ex2: l1=[], l2=[]           → []
 *  Ex3: l1=[], l2=0            → 0
 *  Ex4: l1=5, l2=1->2->3       → 1->2->3->5
 *
 *  CONSTRAINTS
 *  -----------
 *   0 <= len of each list <= 50;  -100 <= val <= 100; both sorted ascending.
 *
 *  HINTS
 *  -----
 *   1. Use a DUMMY node so you don't have to special-case the first attachment.
 *   2. Keep a `tail` pointer at the last node of the result.
 *   3. After loop, just append whichever list is non-empty — it's already sorted.
 * ============================================================ */
public class MergeTwoSortedLists {
    public ListNode mergeTwoLists(ListNode a, ListNode b) {
        ListNode dummy = new ListNode(0), tail = dummy;
        while (a != null && b != null) {
            if (a.val <= b.val) { tail.next = a; a = a.next; }
            else                { tail.next = b; b = b.next; }
            tail = tail.next;
        }
        tail.next = (a != null) ? a : b;
        return dummy.next;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Classic two-pointer merge from merge-sort.
 *  Dummy head pattern avoids the "is this the first node?" branch.
 *
 *  Why splice instead of copy:
 *    O(1) extra space and preserves identity (important if other code holds refs).
 *
 *  Why <= not <:
 *    Keeps the merge STABLE — ties preserve their original list order.
 *
 *  Complexity: Time O(n+m), Space O(1).
 *  Edge cases:
 *    - One empty list → directly return the other.
 *    - Both empty → dummy.next is still null → returns null.
 *  Pattern: dummy-head, used in mergeKLists (#23), addTwoNumbers (#2),
 *           and any "build a list as you go" problem.
 * ============================================================ */
