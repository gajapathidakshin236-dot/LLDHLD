package com.company.DSA;

/* ============================================================
 *  LeetCode #19 — Remove Nth Node From End of List
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given the head of a linked list, remove the n-th node from the end and
 *  return its head. Do it in one pass.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: 1->2->3->4->5, n=2  → 1->2->3->5
 *  Ex2: 1, n=1              → []
 *  Ex3: 1->2, n=1           → 1
 *  Ex4: 1->2, n=2           → 2  (remove head)
 *
 *  CONSTRAINTS:  1 <= nodes <= 30;  1 <= n <= nodes.
 *
 *  HINTS
 *  -----
 *   1. Two pointers with gap = n. Advance fast n steps first.
 *   2. Then advance BOTH until fast hits the last node (fast.next == null).
 *   3. Use a dummy head so removing the actual head is uniform.
 * ============================================================ */
public class RemoveNthFromEnd {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode slow = dummy, fast = dummy;
        for (int i = 0; i < n; i++) fast = fast.next;
        while (fast.next != null) { slow = slow.next; fast = fast.next; }
        slow.next = slow.next.next;
        return dummy.next;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Two pointers, gap=n, then advance together until fast.next==null.
 *  Slow ends at the node BEFORE the one to remove → easy to unlink.
 *
 *  Why dummy head:
 *    The node to remove might be the head itself. Dummy lets slow.next.next
 *    work even when slow is the dummy → returning dummy.next handles that case.
 *
 *  Why `fast.next != null` not `fast != null`:
 *    We want fast to stop on the LAST node, not past it, so slow sits at the
 *    predecessor of the to-delete node.
 *
 *  Complexity: Time O(n) single pass, Space O(1).
 *  Edge cases: remove head; remove tail; single node remove.
 *  Pattern: gap-of-k two-pointer trick, also in "find k-th from end" interview qs.
 * ============================================================ */
