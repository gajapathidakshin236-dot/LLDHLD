package com.company.DSA.linkedlist;

import com.company.DSA.common.ListNode;

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
 *  Ex1: list1=1->2->4, list2=1->3->4 → 1->1->2->3->4->4
 *  Ex2: list1=[], list2=[]           → []
 *  Ex3: list1=[], list2=0            → 0
 *  Ex4: list1=5, list2=1->2->3       → 1->2->3->5
 *
 *  CONSTRAINTS
 *  -----------
 *   0 <= len of each list <= 50;  -100 <= val <= 100; both sorted ascending.
 *
 *  HINTS
 *  -----
 *   1. Use a DUMMY head so you don't have to special-case the first attachment.
 *   2. Keep a `tail` pointer at the last node of the merged result.
 *   3. After loop, just append whichever list is non-empty — it's already sorted.
 * ============================================================ */
public class MergeTwoSortedLists {

    public ListNode mergeTwoLists(ListNode firstList, ListNode secondList) {
        final ListNode mergedHeadSentinel = new ListNode(0);
        ListNode mergedTail = mergedHeadSentinel;

        while (firstList != null && secondList != null) {
            if (firstList.val <= secondList.val) {
                mergedTail.next = firstList;
                firstList = firstList.next;
            } else {
                mergedTail.next = secondList;
                secondList = secondList.next;
            }
            mergedTail = mergedTail.next;
        }

        mergedTail.next = (firstList != null) ? firstList : secondList;
        return mergedHeadSentinel.next;
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
 *  Pattern: dummy-head, used in mergeKLists (#23), addTwoNumbers (#2).
 * ============================================================ */
