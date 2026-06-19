package com.company.DSA.bst;

import com.company.DSA.common.TreeNode;
import java.util.*;

/* ============================================================
 *  LeetCode #653 — Two Sum IV - Input is a BST
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return true if two DIFFERENT nodes sum to k.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: root=[5,3,6,2,4,null,7], k=9 → true
 *  Ex2: root=[5,3,6,2,4,null,7], k=28 → false
 *  Ex3: root=[1], k=2 → false
 *
 *  CONSTRAINTS:  1 <= n <= 10^4;  -10^4 <= val <= 10^4;  -10^5 <= k <= 10^5.
 *
 *  HINTS
 *  -----
 *   1. DFS + HashSet of seen values; look for complement (k - val).
 * ============================================================ */
public class TwoSumIVBST {

    public boolean findTarget(final TreeNode root, final int sumTarget) {
        return search(root, sumTarget, new HashSet<>());
    }

    private boolean search(final TreeNode node, final int sumTarget, final Set<Integer> seenValues) {
        if (node == null) {
            return false;
        }
        if (seenValues.contains(sumTarget - node.val)) {
            return true;
        }
        seenValues.add(node.val);
        return search(node.left,  sumTarget, seenValues)
            || search(node.right, sumTarget, seenValues);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Treat the tree as a collection; visit each node and check whether the
 *  complement was previously seen.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: classic two-sum applied to a non-array structure.
 * ============================================================ */
