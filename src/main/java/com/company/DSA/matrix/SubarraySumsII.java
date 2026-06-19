package com.company.DSA.matrix;

/* ============================================================
 *  LeetCode #2435 — Paths in Matrix Whose Sum Is Divisible by K
 * ============================================================
 *  PROBLEM
 *  -------
 *  Count paths from (0,0) to (m-1,n-1) (only right/down) whose sum is
 *  divisible by k. Modulo 1e9+7.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: grid=[[5,2,4],[3,0,5],[0,7,2]], k=3 → 2
 *  Ex2: grid=[[0,0]], k=5 → 1
 *  Ex3: grid=[[7,3,4,9],[2,3,6,2],[2,3,7,0]], k=1 → 10
 *
 *  CONSTRAINTS:  m*n <= 5*10^4; 0 <= val <= 100; 1 <= k <= 50.
 *
 *  HINTS
 *  -----
 *   1. DP[i][j][r] = # paths to (i,j) with sum mod k == r.
 * ============================================================ */
public class SubarraySumsII {

    private static final int MODULUS = 1_000_000_007;

    public int numberOfPaths(final int[][] grid, final int divisor) {
        final int rowCount = grid.length;
        final int colCount = grid[0].length;

        final int[][][] pathCountByRemainder = new int[rowCount][colCount][divisor];
        pathCountByRemainder[0][0][grid[0][0] % divisor] = 1;

        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            for (int colIndex = 0; colIndex < colCount; colIndex++) {
                if (rowIndex == 0 && colIndex == 0) {
                    continue;
                }
                fillCell(grid, divisor, pathCountByRemainder, rowIndex, colIndex);
            }
        }
        return pathCountByRemainder[rowCount - 1][colCount - 1][0];
    }

    private void fillCell(final int[][] grid,
                          final int divisor,
                          final int[][][] pathCountByRemainder,
                          final int rowIndex,
                          final int colIndex) {
        for (int currentRemainder = 0; currentRemainder < divisor; currentRemainder++) {
            final int previousRemainder = ((currentRemainder - grid[rowIndex][colIndex]) % divisor + divisor) % divisor;

            int aggregatedCount = 0;
            if (rowIndex > 0) {
                aggregatedCount = (aggregatedCount + pathCountByRemainder[rowIndex - 1][colIndex][previousRemainder]) % MODULUS;
            }
            if (colIndex > 0) {
                aggregatedCount = (aggregatedCount + pathCountByRemainder[rowIndex][colIndex - 1][previousRemainder]) % MODULUS;
            }
            pathCountByRemainder[rowIndex][colIndex][currentRemainder] = aggregatedCount;
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  3D DP with (row, col, remainder) state. Previous remainder = (current - cellValue) mod k.
 *
 *  Complexity: Time O(m*n*k), Space O(m*n*k).
 *  Pattern: grid DP with extra modular state.
 * ============================================================ */
