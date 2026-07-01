package com.company.DSA.srivatsanDSA;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 59
 *  Problem: Merge Two Sorted Linked Lists
 *
 *  APPROACH (from notes):
 *    Dummy head + tail; pick smaller of l1 / l2, advance.
 *    After loop, append leftover (one of them is sorted and non-null).
 *    Return dummy.next.
 * ============================================================ */
public class MergeTwoSortedLists {

    public ListNode mergeTwoLists(ListNode firstList, ListNode secondList) {
        final ListNode dummyHead = new ListNode(0);
        ListNode tail            = dummyHead;

        while (firstList != null && secondList != null) {
            if (firstList.val <= secondList.val) {
                tail.next = firstList;
                firstList = firstList.next;
            } else {
                tail.next = secondList;
                secondList = secondList.next;
            }
            tail = tail.next;
        }
        tail.next = (firstList != null) ? firstList : secondList;
        return dummyHead.next;
    }
}
