package com.company.DSA;

/* ============================================================
 *  LeetCode #108 — Convert Sorted Array to BST
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given an integer array nums sorted ascending, convert it to a
 *  height-balanced binary search tree.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [-10,-3,0,5,9] → [0,-3,9,-10,null,5] (one valid answer)
 *  Ex2: [1,3]          → [3,1] or [1,null,3]
 *  Ex3: [1,2,3,4,5]    → balanced BST with root 3
 *
 *  CONSTRAINTS:  1 <= n <= 10^4; sorted ascending; -10^4 <= val <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. Pick the MIDDLE as root → halves left & right subtrees.
 *   2. Recurse on left half for left subtree; right half for right subtree.
 *   3. Both ranges are sorted → BST + balance property are preserved.
 * ============================================================ */
public class SortedArrayToBST {
    public TreeNode sortedArrayToBST(int[] nums) { return build(nums, 0, nums.length - 1); }
    private TreeNode build(int[] a, int l, int r) {
        if (l > r) return null;
        int m = l + (r - l) / 2;
        TreeNode n = new TreeNode(a[m]);
        n.left  = build(a, l, m - 1);
        n.right = build(a, m + 1, r);
        return n;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Choosing the middle as root forces |left| - |right| ≤ 1, giving height-balance.
 *  Recursion on subranges yields the rest by induction.
 *
 *  Why mid = l + (r-l)/2:
 *    Overflow-safe; consistent leftish bias for even sizes.
 *
 *  Complexity: Time O(n), Space O(log n) recursion.
 *  Edge cases: single element → just a leaf; empty range → null.
 *  Pattern: divide & conquer with sorted input → balanced tree.
 * ============================================================ */
