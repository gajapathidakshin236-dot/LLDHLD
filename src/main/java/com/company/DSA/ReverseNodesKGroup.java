package com.company.DSA;

/* ============================================================
 *  LeetCode #25 — Reverse Nodes in k-Group
 * ============================================================
 *  PROBLEM
 *  -------
 *  Reverse every CONSECUTIVE GROUP of k nodes in the linked list. If the
 *  last group has fewer than k nodes, leave it as-is.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: 1->2->3->4->5, k=2 → 2->1->4->3->5
 *  Ex2: 1->2->3->4->5, k=3 → 3->2->1->4->5
 *  Ex3: 1->2->3->4->5, k=1 → 1->2->3->4->5  (no-op)
 *  Ex4: 1->2,         k=3 → 1->2           (fewer than k → keep)
 *
 *  CONSTRAINTS:  1 <= nodes <= 5000;  1 <= k <= nodes;  0 <= val <= 1000.
 *
 *  HINTS
 *  -----
 *   1. Walk in chunks of k. Anchor each chunk on a "groupPrev" node.
 *   2. For each chunk, find the k-th node. If it doesn't exist → stop.
 *   3. Reverse the chunk in place; then stitch group boundaries.
 * ============================================================ */
public class ReverseNodesKGroup {
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(0); dummy.next = head;
        ListNode groupPrev = dummy;
        while (true) {
            ListNode kth = groupPrev;
            for (int i = 0; i < k && kth != null; i++) kth = kth.next;
            if (kth == null) break;
            ListNode groupNext = kth.next;
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

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Repeat: find the chunk boundary (k-th node), reverse the chunk, then
 *  stitch. Dummy head simplifies stitching the FIRST chunk.
 *
 *  Stitching after reverse:
 *    Before reverse: groupPrev -> A -> B -> ... -> K -> groupNext
 *    After:           groupPrev -> K -> ... -> B -> A -> groupNext
 *    So groupPrev.next becomes K (kth), and the old A (groupPrev.next at start)
 *    becomes the new groupPrev for the NEXT chunk.
 *
 *  Why initial prev = groupNext during reverse:
 *    So that A's .next ends up pointing at groupNext after the local reverse.
 *
 *  Complexity: Time O(n) — each node visited a couple of times. Space O(1).
 *  Edge cases: list shorter than k → no reversal; k=1 → no-op; k = length.
 *  Pattern: combine "find k-th" + "reverse range" + "stitch". Building block
 *           for swap pairs (#24), reverse between m..n (#92).
 * ============================================================ */
