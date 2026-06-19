package com.company.DSA.linkedlist;

import com.company.DSA.common.ListNode;

/* ============================================================
 *  LeetCode #160 — Intersection of Two Linked Lists
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return the node where the two lists intersect (by reference), or null if none.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: A: 4->1->8->4->5, B: 5->6->1->8->4->5  → node with val 8
 *  Ex2: A: 1->9->1->2->4, B: 3->2->4           → node with val 2
 *  Ex3: A: 2->6->4,       B: 1->5              → null
 *
 *  CONSTRAINTS:  0 <= len(A), len(B) <= 3*10^4;  -10^5 <= val <= 10^5.
 *
 *  HINTS
 *  -----
 *   1. HashSet of A's nodes; walk B → O(n+m) time, O(n) space.
 *   2. O(1) space: two pointers switching heads → distances equalize.
 * ============================================================ */
public class IntersectionOfTwoLists {

    public ListNode getIntersectionNode(final ListNode firstHead, final ListNode secondHead) {
        ListNode walkerOnFirst  = firstHead;
        ListNode walkerOnSecond = secondHead;

        while (walkerOnFirst != walkerOnSecond) {
            walkerOnFirst  = (walkerOnFirst  == null) ? secondHead : walkerOnFirst.next;
            walkerOnSecond = (walkerOnSecond == null) ? firstHead  : walkerOnSecond.next;
        }
        return walkerOnFirst;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Two pointers walk their own list first, then switch heads. After the second
 *  pass, both have walked exactly (lenA + lenB) steps and are synchronized —
 *  they meet at the first shared node OR at null.
 *
 *  Why this works:
 *    Distance from first head to intersection = a + c (c = shared tail length).
 *    Distance from second head to intersection = b + c.
 *    First pointer covers (a + c + b); second covers (b + c + a). Same total
 *    distance → aligned at the intersection.
 *
 *  Complexity: Time O(n+m), Space O(1).
 *  Pattern: clever pointer arithmetic over linked structures.
 * ============================================================ */
