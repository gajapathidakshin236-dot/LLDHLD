package com.company.DSA.linkedlist;

import com.company.DSA.common.ListNode;

/* ============================================================
 *  LeetCode #25 — Reverse Nodes in k-Group
 * ============================================================
 *  PROBLEM
 *  -------
 *  Reverse every CONSECUTIVE GROUP of k nodes. If the last group has fewer
 *  than k nodes, leave it.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: 1->2->3->4->5, k=2 → 2->1->4->3->5
 *  Ex2: 1->2->3->4->5, k=3 → 3->2->1->4->5
 *  Ex3: 1->2->3->4->5, k=1 → 1->2->3->4->5
 *  Ex4: 1->2,         k=3 → 1->2
 *
 *  CONSTRAINTS:  1 <= nodes <= 5000;  1 <= k <= nodes;  0 <= val <= 1000.
 *
 *  HINTS
 *  -----
 *   1. Walk in chunks of k. Anchor each chunk on a "groupPredecessor" node.
 *   2. For each chunk, find the k-th node. If it doesn't exist → stop.
 *   3. Reverse the chunk in place; then stitch group boundaries.
 * ============================================================ */
public class ReverseNodesKGroup {

    public ListNode reverseKGroup(final ListNode head, final int groupSize) {
        final ListNode headSentinel = new ListNode(0, head);
        ListNode groupPredecessor   = headSentinel;

        while (true) {
            final ListNode kthNode = nthAfter(groupPredecessor, groupSize);
            if (kthNode == null) {
                break;
            }
            final ListNode nextGroupStart = kthNode.next;
            final ListNode oldGroupStart  = groupPredecessor.next;

            reverseRange(oldGroupStart, nextGroupStart);

            groupPredecessor.next = kthNode;
            oldGroupStart.next    = nextGroupStart;
            groupPredecessor      = oldGroupStart;
        }
        return headSentinel.next;
    }

    private ListNode nthAfter(ListNode anchor, final int distance) {
        for (int step = 0; step < distance && anchor != null; step++) {
            anchor = anchor.next;
        }
        return anchor;
    }

    private void reverseRange(final ListNode rangeStart, final ListNode rangeEndExclusive) {
        ListNode previous = rangeEndExclusive;
        ListNode current  = rangeStart;
        while (current != rangeEndExclusive) {
            final ListNode next = current.next;
            current.next = previous;
            previous     = current;
            current      = next;
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Repeat: find the chunk boundary (k-th node), reverse the chunk in place,
 *  then stitch. Dummy head simplifies the FIRST chunk.
 *
 *  Stitching after reverse:
 *    Before: groupPredecessor -> A -> B -> ... -> K -> nextGroupStart
 *    After:  groupPredecessor -> K -> ... -> B -> A -> nextGroupStart
 *    So `groupPredecessor.next` becomes K (kth), and the old A becomes the
 *    new `groupPredecessor` for the next chunk.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: combine "find k-th" + "reverse range" + "stitch".
 * ============================================================ */
