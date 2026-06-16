package com.company.DSA;

/* ============================================================
 *  LeetCode #2 — Add Two Numbers
 * ============================================================
 *  PROBLEM
 *  -------
 *  Two non-negative integers are stored in linked lists, digit-per-node, in
 *  REVERSE order (least significant first). Add and return as a linked list.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: l1 = 2->4->3 (=342), l2 = 5->6->4 (=465) → 7->0->8 (=807)
 *  Ex2: l1 = 0,         l2 = 0                    → 0
 *  Ex3: l1 = 9->9->9->9, l2 = 9->9->9            → 8->9->9->0->1 (=10998)
 *  Ex4: l1 = 5,         l2 = 5                    → 0->1 (carry creates a node)
 *
 *  CONSTRAINTS
 *  -----------
 *   1 <= len <= 100; 0 <= digit <= 9; no leading zeros except number 0.
 *
 *  HINTS
 *  -----
 *   1. Add digit by digit, propagating carry.
 *   2. Loop while EITHER list has digits OR carry > 0.
 *   3. Reverse order makes carry propagation natural (low → high).
 * ============================================================ */
public class AddTwoNumbers {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0), tail = dummy;
        int carry = 0;
        while (l1 != null || l2 != null || carry != 0) {
            int x = l1 != null ? l1.val : 0;
            int y = l2 != null ? l2.val : 0;
            int sum = x + y + carry;
            carry = sum / 10;
            tail.next = new ListNode(sum % 10);
            tail = tail.next;
            if (l1 != null) l1 = l1.next;
            if (l2 != null) l2 = l2.next;
        }
        return dummy.next;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Long addition, exactly as taught in school, but the lists are already in
 *  the convenient reverse order so we never need to reverse anything.
 *
 *  Why carry check in the WHILE condition:
 *    Two single-digit nodes can produce a carry-out (5+5=10), creating a new
 *    most significant digit. Must keep looping until carry==0.
 *
 *  Why dummy head:
 *    Lets us append the FIRST node identically to subsequent ones.
 *
 *  Complexity: Time O(max(n,m)), Space O(max(n,m)) for the result.
 *  Edge cases: 0+0 returns 0; equal length; very different lengths.
 *  Pattern: Long arithmetic + dummy head. Same pattern for adding strings (#415),
 *           multiplying strings (#43), big-int math.
 * ============================================================ */
