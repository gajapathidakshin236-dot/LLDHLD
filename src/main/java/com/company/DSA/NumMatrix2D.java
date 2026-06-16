package com.company.DSA;

/* ============================================================
 *  LeetCode #304 — Range Sum Query 2D - Immutable
 * ============================================================
 *  PROBLEM
 *  -------
 *  Build a class supporting sumRegion(r1, c1, r2, c2) = sum of submatrix in O(1).
 *
 *  EXAMPLES
 *  --------
 *  matrix = [[3,0,1,4,2],
 *            [5,6,3,2,1],
 *            [1,2,0,1,5],
 *            [4,1,0,1,7],
 *            [1,0,3,0,5]]
 *  sumRegion(2,1,4,3) → 8
 *  sumRegion(1,1,2,2) → 11
 *  sumRegion(1,2,2,4) → 12
 *
 *  CONSTRAINTS:  1 <= m,n <= 200; ints; up to 10^4 queries.
 *
 *  HINTS
 *  -----
 *   1. 2D prefix sums: P[i+1][j+1] = sum of submatrix [0..i][0..j].
 *   2. Inclusion-exclusion: P[r2+1][c2+1] - P[r1][c2+1] - P[r2+1][c1] + P[r1][c1].
 *   3. Padded with zeros so r1=0/c1=0 needs no special case.
 * ============================================================ */
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

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Inclusion-exclusion on 2D prefix sums:
 *    region = TL_to_BR - TL_to_above_TL_region - TL_to_left_region + corner_overlap
 *
 *  Padding by one row/col of zeros simplifies index math at the edges.
 *
 *  Complexity: Build O(m*n), Query O(1), Space O(m*n).
 *  Pattern: 2D prefix sums. Used in image processing, matrix games, etc.
 * ============================================================ */
