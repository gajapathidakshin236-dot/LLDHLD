package com.company.DSA;

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
 *  Ex4: 1->2          → 1->2 (already in order)
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
    public void reorderList(ListNode head) {
        if (head == null || head.next == null) return;
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next; fast = fast.next.next;
        }
        ListNode prev = null, cur = slow.next;
        slow.next = null;
        while (cur != null) {
            ListNode nx = cur.next; cur.next = prev; prev = cur; cur = nx;
        }
        ListNode a = head, b = prev;
        while (b != null) {
            ListNode an = a.next, bn = b.next;
            a.next = b; b.next = an;
            a = an; b = bn;
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
 *  Why cut the list (slow.next = null):
 *    Prevents accidentally merging into a cycle and gives both halves
 *    well-defined null terminations.
 *
 *  Why we keep `an` and `bn` saved:
 *    We are about to overwrite a.next and b.next, so we must remember the
 *    next nodes from each half before they're lost.
 *
 *  Loop condition `while (b != null)`:
 *    Second half is shorter or equal — when b is null, all weaving done.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Edge cases: 0 or 1 node → no-op; even/odd length both work.
 *  Pattern: combine three primitives. Common in linked-list interview rounds.
 * ============================================================ */
