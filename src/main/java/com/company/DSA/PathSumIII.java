package com.company.DSA;

import java.util.*;

/**
 * LeetCode #437 - Path Sum III (count downward paths summing to target)
 * Prefix-sum hashmap of partial sums on the current root-to-node path.
 * Time: O(n)  Space: O(n)
 */
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
