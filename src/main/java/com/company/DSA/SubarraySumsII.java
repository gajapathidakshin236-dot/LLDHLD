package com.company.DSA;

import java.util.*;

/**
 * LeetCode #2435 - Paths in Matrix Whose Sum Is Divisible by K (Subarray Sums II family)
 * DP[i][j][r] = # paths reaching (i,j) with sum mod k == r.
 * Time: O(m*n*k)  Space: O(m*n*k)
 */
public class SubarraySumsII {
    public int numberOfPaths(int[][] grid, int k) {
        int MOD = 1_000_000_007, m = grid.length, n = grid[0].length;
        int[][][] dp = new int[m][n][k];
        dp[0][0][grid[0][0] % k] = 1;
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++) {
                if (i == 0 && j == 0) continue;
                for (int r = 0; r < k; r++) {
                    int prev = ((r - grid[i][j]) % k + k) % k;
                    int s = 0;
                    if (i > 0) s = (s + dp[i - 1][j][prev]) % MOD;
                    if (j > 0) s = (s + dp[i][j - 1][prev]) % MOD;
                    dp[i][j][r] = s;
                }
            }
        return dp[m - 1][n - 1][0];
    }
}
