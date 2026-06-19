package com.company.DSA.linkedlist;

import com.company.DSA.common.ListNode;

/* ============================================================
 *  LeetCode #143 — Reorder List
 * ============================================================
 *  PROBLEM
 *  -------
 *  Reorder list L0->L1->...->Ln-1 to L0->Ln-1->L1->Ln-2->L2->Ln-3->...
 *  Do it in place (no node copies).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: 1->2->3->4    → 1->4->2->3
 *  Ex2: 1->2->3->4->5 → 1->5->2->4->3
 *  Ex3: 1             → 1
 *  Ex4: 1->2          → 1->2
 *
 *  CONSTRAINTS
 *  -----------
 *   1 <= nodes <= 5*10^4;  1 <= val <= 1000.
 *
 *  HINTS
 *  -----
 *   1. Three classic linked-list ops chained: find middle, reverse second half, merge.
 *   2. Use slow/fast to find mid; CUT the list there (slow.next = null).
 *   3. Reverse the second half; then weave node-by-node from both ends.
 * ============================================================ */
public class ReorderList {

    public void reorderList(final ListNode head) {
        if (head == null || head.next == null) {
            return;
        }
        final ListNode middleNode      = findMiddle(head);
        final ListNode secondHalfHead  = reverse(middleNode.next);
        middleNode.next                = null;
        interleave(head, secondHalfHead);
    }

    private ListNode findMiddle(final ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast.next != null && fast.next.next != null) {
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

    private void interleave(ListNode firstHalf, ListNode secondHalf) {
        while (secondHalf != null) {
            final ListNode firstNext  = firstHalf.next;
            final ListNode secondNext = secondHalf.next;

            firstHalf.next  = secondHalf;
            secondHalf.next = firstNext;

            firstHalf  = firstNext;
            secondHalf = secondNext;
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Three sub-problems, each a classic:
 *    1) Find middle — slow/fast pointers (#876).
 *    2) Reverse second half — in-place reverse (#206).
 *    3) Merge alternately — weave A and B with a temp save.
 *
 *  Why cut the list (middleNode.next = null):
 *    Prevents accidentally merging into a cycle and gives both halves
 *    well-defined null terminations.
 *
 *  Loop condition `while (secondHalf != null)`:
 *    Second half is shorter or equal — when secondHalf is null, weaving done.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: combine three primitives. Common in linked-list interview rounds.
 * ============================================================ */
