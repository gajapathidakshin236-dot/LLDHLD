package com.company.DSA;

/* ============================================================
 *  LeetCode #98 — Validate Binary Search Tree
 * ============================================================
 *  PROBLEM
 *  -------
 *  Determine if a binary tree is a valid BST: every node's left subtree
 *  contains strictly smaller values, right subtree strictly larger.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [2,1,3]                  → true
 *  Ex2: [5,1,4,null,null,3,6]    → false  (3 in 5's right subtree but < 5)
 *  Ex3: [10,5,15,null,null,6,20] → false  (6 in 10's right but < 10)
 *  Ex4: [2,2,2]                  → false  (equals not allowed, strictly)
 *
 *  CONSTRAINTS:  1 <= nodes <= 10^4;  -2^31 <= val <= 2^31 - 1.
 *
 *  HINTS
 *  -----
 *   1. A common bug: only checking parent vs child — doesn't catch deep violations.
 *   2. Carry (lo, hi) BOUNDS down the recursion.
 *   3. Use Long bounds so node value Integer.MIN/MAX don't cause false negatives.
 * ============================================================ */
public class ValidateBST {
    public boolean isValidBST(TreeNode root) {
        return dfs(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }
    private boolean dfs(TreeNode n, long lo, long hi) {
        if (n == null) return true;
        if (n.val <= lo || n.val >= hi) return false;
        return dfs(n.left, lo, n.val) && dfs(n.right, n.val, hi);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Carry an open interval (lo, hi) for the values allowed at each node.
 *  Going left, the upper bound tightens to current node's value.
 *  Going right, the lower bound tightens to current node's value.
 *
 *  Why Long, not int:
 *    Tests include nodes with val = Integer.MIN_VALUE or MAX_VALUE.
 *    Long avoids clashes with sentinel values for the initial bounds.
 *
 *  Alternative: in-order traversal must produce strictly ascending sequence.
 *    Both are O(n).
 *
 *  Complexity: Time O(n), Space O(h) recursion.
 *  Edge cases: equal values (e.g., 2,2,2) → false; single node → true.
 *  Pattern: "pass invariants down the recursion" — common in tree problems.
 * ============================================================ */
