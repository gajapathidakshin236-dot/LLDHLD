package com.company.DSA.matrix;

/* ============================================================
 *  LeetCode #304 — Range Sum Query 2D - Immutable
 * ============================================================
 *  PROBLEM
 *  -------
 *  sumRegion(r1,c1,r2,c2) for a fixed matrix in O(1) per query.
 *
 *  EXAMPLES
 *  --------
 *  matrix = [[3,0,1,4,2],[5,6,3,2,1],[1,2,0,1,5],[4,1,0,1,7],[1,0,3,0,5]]
 *  sumRegion(2,1,4,3) → 8;  sumRegion(1,1,2,2) → 11;  sumRegion(1,2,2,4) → 12
 *
 *  CONSTRAINTS:  1 <= m,n <= 200; up to 10^4 queries.
 *
 *  HINTS
 *  -----
 *   1. 2D prefix sums + inclusion-exclusion.
 * ============================================================ */
public class NumMatrix2D {

    private final int[][] paddedPrefixSum;

    public NumMatrix2D(final int[][] matrix) {
        final int rowCount = matrix.length;
        final int colCount = matrix[0].length;
        this.paddedPrefixSum = new int[rowCount + 1][colCount + 1];

        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            for (int colIndex = 0; colIndex < colCount; colIndex++) {
                paddedPrefixSum[rowIndex + 1][colIndex + 1]
                        = matrix[rowIndex][colIndex]
                        + paddedPrefixSum[rowIndex][colIndex + 1]
                        + paddedPrefixSum[rowIndex + 1][colIndex]
                        - paddedPrefixSum[rowIndex][colIndex];
            }
        }
    }

    public int sumRegion(final int topRow,    final int leftCol,
                         final int bottomRow, final int rightCol) {
        return paddedPrefixSum[bottomRow + 1][rightCol + 1]
             - paddedPrefixSum[topRow][rightCol + 1]
             - paddedPrefixSum[bottomRow + 1][leftCol]
             + paddedPrefixSum[topRow][leftCol];
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  2D prefix sums with one row/col of leading zeros simplifies edge cases.
 *  Inclusion-exclusion gives O(1) region sum.
 *
 *  Complexity: Build O(m*n), query O(1), space O(m*n).
 *  Pattern: 2D prefix sums.
 * ============================================================ */
