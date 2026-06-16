package com.company.DSA;

/**
 * LeetCode #304 - Range Sum Query 2D - Immutable
 * 2D prefix sums. region = P[r2+1][c2+1] - P[r1][c2+1] - P[r2+1][c1] + P[r1][c1]
 * Time: ctor O(m*n), query O(1)
 */
public class NumMatrix2D {
    private final int[][] P;
    public NumMatrix2D(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        P = new int[m + 1][n + 1];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                P[i + 1][j + 1] = mat[i][j] + P[i][j + 1] + P[i + 1][j] - P[i][j];
    }
    public int sumRegion(int r1, int c1, int r2, int c2) {
        return P[r2 + 1][c2 + 1] - P[r1][c2 + 1] - P[r2 + 1][c1] + P[r1][c1];
    }
}
