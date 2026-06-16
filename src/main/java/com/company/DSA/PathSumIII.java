package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #437 — Path Sum III
 * ============================================================
 *  PROBLEM
 *  -------
 *  Count the number of paths in a binary tree that sum to targetSum.
 *  Paths must go downward (parent → child only) and need NOT start at root
 *  or end at a leaf.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: root=[10,5,-3,3,2,null,11,3,-2,null,1], target=8 → 3
 *  Ex2: root=[5,4,8,11,null,13,4,7,2,null,null,5,1], target=22 → 3
 *  Ex3: root=[], target=0 → 0
 *
 *  CONSTRAINTS:  0 <= nodes <= 1000;  -10^9 <= val <= 10^9;  target in int range.
 *
 *  HINTS
 *  -----
 *   1. Naive: from every node, DFS down counting — O(n^2).
 *   2. Better: prefix sums along the CURRENT path.
 *   3. Use a HashMap of {prefixSum → count} representing what's reachable on path.
 *   4. On entering a node, add prefix; on leaving, REMOVE (backtrack).
 * ============================================================ */
public class PathSumIII {
    int count = 0; int target;
    public int pathSum(TreeNode root, int targetSum) {
        target = targetSum;
        Map<Long,Integer> pref = new HashMap<>();
        pref.put(0L, 1);
        dfs(root, 0L, pref);
        return count;
    }
    private void dfs(TreeNode n, long cur, Map<Long,Integer> pref) {
        if (n == null) return;
        cur += n.val;
        count += pref.getOrDefault(cur - target, 0);
        pref.merge(cur, 1, Integer::sum);
        dfs(n.left, cur, pref);
        dfs(n.right, cur, pref);
        pref.merge(cur, -1, Integer::sum);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Prefix sums on the live root-to-current path. A downward path summing to
 *  target exists iff (curPrefix - target) appears somewhere earlier on the path.
 *  Maintain a HashMap of prefix sums along the path. On enter, increment; on
 *  leave (backtrack), decrement — like a stack of partial sums.
 *
 *  Why Long:
 *    Node values can be -10^9..10^9; long avoids overflow in accumulation.
 *
 *  Why pref.put(0L, 1):
 *    Represents "empty path" — counts paths from root downward.
 *
 *  Complexity: Time O(n), Space O(h) map + O(h) recursion.
 *  Edge cases: empty; tree with negative values; target zero.
 *  Pattern: prefix-sum hashmap on a TREE PATH — same idea as #560 on arrays.
 * ============================================================ */
