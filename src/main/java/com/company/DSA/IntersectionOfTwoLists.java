package com.company.DSA;

/* ============================================================
 *  LeetCode #160 — Intersection of Two Linked Lists
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given the heads of two singly linked lists, return the node where they
 *  intersect (by reference, not by value). If no intersection, return null.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: A: 4->1->8->4->5
 *       B: 5->6->1->8->4->5    intersect at "8" → return that node.
 *  Ex2: A: 1->9->1->2->4
 *       B:     3->2->4         intersect at "2"
 *  Ex3: A: 2->6->4
 *       B: 1->5               no intersection → null
 *
 *  CONSTRAINTS:  0 <= len(A), len(B) <= 3*10^4;  -10^5 <= val <= 10^5.
 *
 *  HINTS
 *  -----
 *   1. HashSet of A's nodes; walk B → O(n+m) time, O(n) space.
 *   2. O(1) space: two pointers switching heads → distances equalize.
 *   3. They meet at intersection or BOTH at null after at most 2*(n+m) steps.
 * ============================================================ */
public class IntersectionOfTwoLists {
    public ListNode getIntersectionNode(ListNode a, ListNode b) {
        ListNode p = a, q = b;
        while (p != q) {
            p = (p == null) ? b : p.next;
            q = (q == null) ? a : q.next;
        }
        return p;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Two pointers walk their own list first, then switch heads.
 *  By the end of pass 2, both have walked exactly (lenA + lenB) steps
 *  and are synchronized — they meet at the first shared node OR at null.
 *
 *  Why this works:
 *    Let lenA, lenB be lengths and c be shared tail length.
 *    Distinct prefixes a = lenA - c and b = lenB - c.
 *    p path: a + c + b = a + b + c
 *    q path: b + c + a = a + b + c
 *    Same total → they're aligned at the intersection on the 2nd pass.
 *
 *  Complexity: Time O(n+m), Space O(1).
 *  Edge cases: empty lists → both null first iteration → return null;
 *              fully shared → meet at head A or B.
 *  Pattern: clever pointer arithmetic over linked structures.
 * ============================================================ */
