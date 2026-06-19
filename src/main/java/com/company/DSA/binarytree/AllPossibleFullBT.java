package com.company.DSA.binarytree;

import com.company.DSA.common.TreeNode;
import java.util.*;

/* ============================================================
 *  LeetCode #894 — All Possible Full Binary Trees
 * ============================================================
 *  PROBLEM
 *  -------
 *  A FULL binary tree has every node with exactly 0 or 2 children. Given n,
 *  return all possible full binary trees with n nodes. All values are 0.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: n=7 → 5 structurally distinct full BTs.
 *  Ex2: n=3 → 1 tree.
 *  Ex3: n=2 → []
 *  Ex4: n=1 → [single node]
 *
 *  CONSTRAINTS:  1 <= n <= 20.
 *
 *  HINTS
 *  -----
 *   1. Full BT must have ODD node count.
 *   2. Split into left (odd L) and right (odd R = n-1-L). Try all L = 1, 3, ...
 *   3. Memoize by n.
 * ============================================================ */
public class AllPossibleFullBT {

    private final Map<Integer, List<TreeNode>> memoByNodeCount = new HashMap<>();

    public List<TreeNode> allPossibleFBT(final int nodeCount) {
        if (memoByNodeCount.containsKey(nodeCount)) {
            return memoByNodeCount.get(nodeCount);
        }

        final List<TreeNode> result = new ArrayList<>();
        if (nodeCount == 1) {
            result.add(new TreeNode(0));
        } else if (nodeCount % 2 == 1) {
            for (int leftSize = 1; leftSize < nodeCount; leftSize += 2) {
                final int rightSize = nodeCount - 1 - leftSize;
                for (final TreeNode leftSubtree  : allPossibleFBT(leftSize))  {
                    for (final TreeNode rightSubtree : allPossibleFBT(rightSize)) {
                        result.add(new TreeNode(0, leftSubtree, rightSubtree));
                    }
                }
            }
        }
        memoByNodeCount.put(nodeCount, result);
        return result;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Recursive enumeration with memoization. A full BT of n nodes = root + left
 *  subtree (full BT of L odd) + right subtree (full BT of n-1-L odd). Try
 *  every odd split.
 *
 *  Memoization shares subtree references — safe because nothing mutates them.
 *
 *  Complexity: Time/Space exponential — Catalan-like.
 *  Pattern: structural enumeration with memo.
 * ============================================================ */
