package com.company.DSA;

/* ============================================================
 *  LeetCode #129 — Sum Root to Leaf Numbers
 * ============================================================
 *  PROBLEM
 *  -------
 *  Each root-to-leaf path forms a number (concatenation of digits). Return
 *  the sum of all such numbers.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,2,3] → 25     (12 + 13)
 *  Ex2: [4,9,0,5,1] → 1026  (495+491+40)
 *  Ex3: [1] → 1
 *
 *  CONSTRAINTS:  1 <= n <= 1000;  0 <= val <= 9; depth <= 10.
 *
 *  HINTS
 *  -----
 *   1. Carry the running number down the DFS. New value: cur*10 + node.val.
 *   2. At a leaf, add that running number to total.
 * ============================================================ */
public class SumRootToLeafNumbers {
    public int sumNumbers(TreeNode root) { return dfs(root, 0); }
    private int dfs(TreeNode n, int cur) {
        if (n == null) return 0;
        cur = cur * 10 + n.val;
        if (n.left == null && n.right == null) return cur;
        return dfs(n.left, cur) + dfs(n.right, cur);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  DFS that carries the partial number. Multiplying by 10 shifts the digits;
 *  adding val appends the new digit. At a leaf, return the assembled number.
 *
 *  Why returning sum from both branches works:
 *    DFS sums up subtree contributions; nulls return 0. Final value at root is
 *    sum across all leaves.
 *
 *  Complexity: Time O(n), Space O(h).
 *  Edge cases: single node returns its value.
 *  Pattern: "carry accumulated state down a tree." Same: #112 path-sum.
 * ============================================================ */
