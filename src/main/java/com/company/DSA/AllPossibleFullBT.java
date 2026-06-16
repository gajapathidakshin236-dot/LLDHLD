package com.company.DSA;

import java.util.*;

/**
 * LeetCode #894 - All Possible Full Binary Trees
 * Recurse: full BT must have odd N. For each odd L (1..n-2), pair with R = n-1-L.
 * Memoize by n.
 * Time/Space: exponential.
 */
public class AllPossibleFullBT {
    Map<Integer, List<TreeNode>> memo = new HashMap<>();
    public List<TreeNode> allPossibleFBT(int n) {
        if (memo.containsKey(n)) return memo.get(n);
        List<TreeNode> res = new ArrayList<>();
        if (n == 1) { res.add(new TreeNode(0)); }
        else if (n % 2 == 1) {
            for (int L = 1; L < n; L += 2) {
                int R = n - 1 - L;
                for (TreeNode l : allPossibleFBT(L))
                    for (TreeNode r : allPossibleFBT(R)) {
                        res.add(new TreeNode(0, l, r));
                    }
            }
        }
        memo.put(n, res);
        return res;
    }
}
