package com.company.DSA.binarytree;

import com.company.DSA.common.TreeNode;

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
 *  Ex1: [-10,-3,0,5,9] → balanced BST rooted at 0 (one valid answer)
 *  Ex2: [1,3]          → [3,1] or [1,null,3]
 *  Ex3: [1,2,3,4,5]    → balanced BST with root 3
 *
 *  CONSTRAINTS:  1 <= n <= 10^4; sorted ascending; -10^4 <= val <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. Pick the MIDDLE as root → halves left & right subtrees.
 *   2. Recurse on left half / right half.
 * ============================================================ */
public class SortedArrayToBST {

    public TreeNode sortedArrayToBST(final int[] sortedValues) {
        return buildBalanced(sortedValues, 0, sortedValues.length - 1);
    }

    private TreeNode buildBalanced(final int[] values, final int leftBound, final int rightBound) {
        if (leftBound > rightBound) {
            return null;
        }
        final int middleIndex = leftBound + (rightBound - leftBound) / 2;

        final TreeNode root = new TreeNode(values[middleIndex]);
        root.left  = buildBalanced(values, leftBound,        middleIndex - 1);
        root.right = buildBalanced(values, middleIndex + 1,  rightBound);
        return root;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Choosing the middle as root forces |left| - |right| ≤ 1, giving height-balance.
 *  Recursion on subranges yields the rest by induction.
 *
 *  Complexity: Time O(n), Space O(log n) recursion.
 *  Pattern: divide & conquer with sorted input → balanced tree.
 * ============================================================ */
