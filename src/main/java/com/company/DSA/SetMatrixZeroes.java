package com.company.DSA;

/* ============================================================
 *  LeetCode #73 — Set Matrix Zeroes
 * ============================================================
 *
 *  PROBLEM
 *  -------
 *  Given an m x n matrix, if any cell is 0, set its entire row and column
 *  to 0. Do it IN PLACE.
 *  Follow-up: O(1) extra memory.
 *
 *  EXAMPLES
 *  --------
 *  Example 1:
 *    Input:  [[1,1,1],[1,0,1],[1,1,1]]
 *    Output: [[1,0,1],[0,0,0],[1,0,1]]
 *    Why:    Cell (1,1) is 0 → row 1 and col 1 become 0.
 *
 *  Example 2:
 *    Input:  [[0,1,2,0],[3,4,5,2],[1,3,1,5]]
 *    Output: [[0,0,0,0],[0,4,5,0],[0,3,1,0]]
 *    Why:    Two zeroes in row 0 → row 0 + cols 0,3 all zeroed.
 *
 *  Example 3:
 *    Input:  [[1,2,3]]
 *    Output: [[1,2,3]]   // no zeroes at all
 *
 *  CONSTRAINTS
 *  -----------
 *   m == matrix.length, n == matrix[0].length
 *   1 <= m, n <= 200
 *   -2^31 <= matrix[i][j] <= 2^31 - 1
 *
 *  HINTS
 *  -----
 *   1. Naive: collect rows/cols to zero in two sets — O(m+n) space.
 *   2. Smarter: use the matrix's own first row & first column as the "set".
 *   3. The first row/col can no longer be used as data after marking — track
 *      separately whether they themselves should be zeroed.
 * ============================================================ */
public class SetMatrixZeroes {
    public void setZeroes(int[][] g) {
        int m = g.length, n = g[0].length;
        boolean firstRow = false, firstCol = false;
        for (int j = 0; j < n; j++) if (g[0][j] == 0) { firstRow = true; break; }
        for (int i = 0; i < m; i++) if (g[i][0] == 0) { firstCol = true; break; }
        for (int i = 1; i < m; i++)
            for (int j = 1; j < n; j++)
                if (g[i][j] == 0) { g[i][0] = 0; g[0][j] = 0; }
        for (int i = 1; i < m; i++)
            for (int j = 1; j < n; j++)
                if (g[i][0] == 0 || g[0][j] == 0) g[i][j] = 0;
        if (firstRow) for (int j = 0; j < n; j++) g[0][j] = 0;
        if (firstCol) for (int i = 0; i < m; i++) g[i][0] = 0;
    }
}

/* ============================================================
 *  APPROACH EXPLAINED
 * ============================================================
 *
 *  Idea — reuse first row/col as "markers":
 *    We need to remember which rows and columns should be zeroed.
 *    Instead of allocating boolean[m] and boolean[n], we use row 0 and col 0
 *    as those flags. The trade-off: we lose the data they previously held,
 *    so we must remember (in two booleans) whether row 0 and col 0 themselves
 *    need to be zeroed at the end.
 *
 *  Step-by-step:
 *    1. Scan row 0 → firstRow=true if any 0.
 *    2. Scan col 0 → firstCol=true if any 0.
 *    3. For each interior cell (i>=1, j>=1) that is 0, mark
 *       g[i][0] = 0 and g[0][j] = 0.
 *    4. For each interior cell, if its row marker OR col marker is 0, zero it.
 *    5. Finally, zero row 0 if firstRow; zero col 0 if firstCol.
 *
 *  Why order matters:
 *    If we zeroed row 0 / col 0 first based on flags, we'd corrupt the marker
 *    data we still need for interior cells. So markers are applied LAST.
 *
 *  Complexity:
 *    Time:  O(m*n) — three linear passes over the grid.
 *    Space: O(1)   — two booleans.
 *
 *  Edge cases:
 *    - All zeroes → whole matrix becomes 0.
 *    - No zeroes → matrix unchanged.
 *    - 1x1 → trivial.
 *    - 1xN or Mx1 → firstRow / firstCol cover them.
 *
 *  Pattern:
 *    "Encode metadata in the data itself." Same trick used in
 *    Game of Life (#289) where we pack next state into spare bits.
 * ============================================================ */
