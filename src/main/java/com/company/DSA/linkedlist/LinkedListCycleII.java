package com.company.DSA.linkedlist;

import com.company.DSA.common.ListNode;

/* ============================================================
 *  LeetCode #142 — Linked List Cycle II
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return the node where the cycle BEGINS (the entry node), or null if no cycle.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: 3->2->0->-4, tail → index 1 → returns node with val 2
 *  Ex2: 1->2, tail → index 0 → returns node with val 1
 *  Ex3: 1, no cycle → null
 *  Ex4: empty → null
 *
 *  CONSTRAINTS
 *  -----------
 *   0 <= nodes <= 10^4;  -10^5 <= val <= 10^5; pos = -1 or valid index.
 *
 *  HINTS
 *  -----
 *   1. Phase 1: find meeting point with Floyd's slow/fast (cycle exists).
 *   2. Phase 2: reset one pointer to head; move both 1 step at a time → they meet at the cycle start.
 * ============================================================ */
public class LinkedListCycleII {

    public ListNode detectCycle(final ListNode head) {
        final ListNode meetingPoint = findMeetingPoint(head);
        if (meetingPoint == null) {
            return null;
        }
        return findCycleStart(head, meetingPoint);
    }

    private ListNode findMeetingPoint(final ListNode head) {
        ListNode slowRunner = head;
        ListNode fastRunner = head;

        while (fastRunner != null && fastRunner.next != null) {
            slowRunner = slowRunner.next;
            fastRunner = fastRunner.next.next;

            if (slowRunner == fastRunner) {
                return slowRunner;
            }
        }
        return null;
    }

    private ListNode findCycleStart(final ListNode head, final ListNode meetingPoint) {
        ListNode fromHead    = head;
        ListNode fromMeeting = meetingPoint;

        while (fromHead != fromMeeting) {
            fromHead    = fromHead.next;
            fromMeeting = fromMeeting.next;
        }
        return fromHead;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Floyd's Phase 1 detects a cycle and returns the meeting point.
 *  Phase 2 walks two pointers (one from head, one from the meeting point) one
 *  step at a time — they meet exactly at the cycle entry.
 *
 *  Why this math works:
 *    Let a = distance head → cycle start.
 *    Let b = distance cycle start → meeting point along the cycle.
 *    Let L = cycle length.
 *    At meeting: slow traveled a+b; fast traveled 2(a+b). Difference must be a
 *    multiple of L:  (a+b) = k*L  →  a = (k-1)*L + (L - b)
 *    So `a` steps from head ends at the cycle start, and the same number of
 *    steps from meeting also ends there.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Edge cases: no cycle → returns null; cycle of length 1 → start at slow.
 *  Pattern: Floyd's, applied to find duplicate (#287).
 * ============================================================ */
