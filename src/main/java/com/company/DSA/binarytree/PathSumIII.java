package com.company.DSA.binarytree;

import com.company.DSA.common.TreeNode;
import java.util.*;

/* ============================================================
 *  LeetCode #437 — Path Sum III
 * ============================================================
 *  PROBLEM
 *  -------
 *  Count the number of downward paths in a binary tree that sum to targetSum.
 *  Paths need NOT start at root or end at a leaf.
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
 *   2. Better: prefix sums on the CURRENT path.
 *   3. HashMap of {prefixSum → count} along the path.
 *   4. On entering a node, increment; on leaving, decrement (backtrack).
 * ============================================================ */
public class PathSumIII {

    private int matchingPathCount = 0;
    private int targetSum;

    public int pathSum(final TreeNode root, final int targetSum) {
        this.targetSum = targetSum;

        final Map<Long, Integer> prefixSumCounts = new HashMap<>();
        prefixSumCounts.put(0L, 1);

        depthFirst(root, 0L, prefixSumCounts);
        return matchingPathCount;
    }

    private void depthFirst(final TreeNode node,
                            long runningSum,
                            final Map<Long, Integer> prefixSumCounts) {
        if (node == null) {
            return;
        }

        runningSum += node.val;
        final long complementSum = runningSum - targetSum;
        matchingPathCount += prefixSumCounts.getOrDefault(complementSum, 0);

        prefixSumCounts.merge(runningSum, 1, Integer::sum);
        depthFirst(node.left,  runningSum, prefixSumCounts);
        depthFirst(node.right, runningSum, prefixSumCounts);
        prefixSumCounts.merge(runningSum, -1, Integer::sum);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Prefix sums on the live root-to-current path. A downward path summing to
 *  target exists iff (runningSum - target) appears somewhere earlier on the path.
 *  Maintain a HashMap of prefix sums along the path; increment on enter,
 *  decrement on leave (backtrack) — like a stack of partial sums.
 *
 *  Complexity: Time O(n), Space O(h).
 *  Pattern: prefix-sum hashmap on a TREE PATH (same idea as #560 on arrays).
 * ============================================================ */
