package com.company.DSA.linkedlist;

import com.company.DSA.common.ListNode;

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
 *   3. Reverse-order input makes carry propagation natural (low → high).
 * ============================================================ */
public class AddTwoNumbers {

    public ListNode addTwoNumbers(ListNode firstNumber, ListNode secondNumber) {
        final ListNode resultHeadSentinel = new ListNode(0);
        ListNode resultTail = resultHeadSentinel;
        int carryOver = 0;

        while (firstNumber != null || secondNumber != null || carryOver != 0) {
            final int firstDigit  = (firstNumber  != null) ? firstNumber.val  : 0;
            final int secondDigit = (secondNumber != null) ? secondNumber.val : 0;

            final int digitSum     = firstDigit + secondDigit + carryOver;
            final int currentDigit = digitSum % 10;
            carryOver              = digitSum / 10;

            resultTail.next = new ListNode(currentDigit);
            resultTail      = resultTail.next;

            if (firstNumber  != null) { firstNumber  = firstNumber.next;  }
            if (secondNumber != null) { secondNumber = secondNumber.next; }
        }

        return resultHeadSentinel.next;
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
 *  Complexity: Time O(max(n,m)), Space O(max(n,m)) for the result.
 *  Pattern: Long arithmetic + dummy head — same as #415, #43, big-int math.
 * ============================================================ */
