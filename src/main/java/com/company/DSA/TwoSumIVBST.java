package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #653 — Two Sum IV - Input is a BST
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given the root of a BST and integer k, return true if there exist two
 *  DIFFERENT nodes in the tree such that their values sum to k.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: root=[5,3,6,2,4,null,7], k=9 → true (2+7 or 3+6 or 4+5)
 *  Ex2: root=[5,3,6,2,4,null,7], k=28 → false
 *  Ex3: root=[1], k=2 → false
 *
 *  CONSTRAINTS:  1 <= n <= 10^4;  -10^4 <= val <= 10^4;  -10^5 <= k <= 10^5.
 *
 *  HINTS
 *  -----
 *   1. Easy: DFS + HashSet of complement.
 *   2. O(n) time, O(h) space alt: two iterators (inorder & reverse inorder)
 *      converging like two-pointer on a sorted array.
 * ============================================================ */
public class TwoSumIVBST {
    public boolean findTarget(TreeNode root, int k) {
        return dfs(root, k, new HashSet<>());
    }
    private boolean dfs(TreeNode n, int k, Set<Integer> seen) {
        if (n == null) return false;
        if (seen.contains(k - n.val)) return true;
        seen.add(n.val);
        return dfs(n.left, k, seen) || dfs(n.right, k, seen);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Treat the tree as an unordered collection; visit each node and check if
 *  its complement was previously seen — classic two-sum HashSet pattern.
 *
 *  Alt for O(h) space:
 *    Two stacks: one walking inorder, one walking reverse-inorder. Move
 *    smaller forward / larger backward until they meet or cross.
 *
 *  Complexity: Time O(n), Space O(n) set.
 *  Edge cases: single node → false (need TWO distinct nodes).
 *  Pattern: classic two-sum applied to a non-array structure.
 * ============================================================ */
