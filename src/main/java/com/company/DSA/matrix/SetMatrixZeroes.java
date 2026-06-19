package com.company.DSA.matrix;

/* ============================================================
 *  LeetCode #73 — Set Matrix Zeroes
 * ============================================================
 *  PROBLEM
 *  -------
 *  If any cell is 0, set its row and column to 0. In place.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [[1,1,1],[1,0,1],[1,1,1]] → [[1,0,1],[0,0,0],[1,0,1]]
 *  Ex2: [[0,1,2,0],[3,4,5,2],[1,3,1,5]] → [[0,0,0,0],[0,4,5,0],[0,3,1,0]]
 *
 *  CONSTRAINTS:  1 <= m,n <= 200.
 *
 *  HINTS
 *  -----
 *   1. Use first row/col as markers; flags for first row/col themselves.
 * ============================================================ */
public class SetMatrixZeroes {

    public void setZeroes(final int[][] grid) {
        final int rowCount = grid.length;
        final int colCount = grid[0].length;

        final boolean firstRowHasZero = anyZeroInRow(grid, 0);
        final boolean firstColHasZero = anyZeroInColumn(grid, 0);

        markZerosOnEdges(grid);
        applyMarkersToInterior(grid);

        if (firstRowHasZero) {
            zeroOutRow(grid, 0);
        }
        if (firstColHasZero) {
            zeroOutColumn(grid, 0);
        }
    }

    private boolean anyZeroInRow(final int[][] grid, final int rowIndex) {
        for (int colIndex = 0; colIndex < grid[0].length; colIndex++) {
            if (grid[rowIndex][colIndex] == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean anyZeroInColumn(final int[][] grid, final int colIndex) {
        for (int rowIndex = 0; rowIndex < grid.length; rowIndex++) {
            if (grid[rowIndex][colIndex] == 0) {
                return true;
            }
        }
        return false;
    }

    private void markZerosOnEdges(final int[][] grid) {
        for (int rowIndex = 1; rowIndex < grid.length; rowIndex++) {
            for (int colIndex = 1; colIndex < grid[0].length; colIndex++) {
                if (grid[rowIndex][colIndex] == 0) {
                    grid[rowIndex][0] = 0;
                    grid[0][colIndex] = 0;
                }
            }
        }
    }

    private void applyMarkersToInterior(final int[][] grid) {
        for (int rowIndex = 1; rowIndex < grid.length; rowIndex++) {
            for (int colIndex = 1; colIndex < grid[0].length; colIndex++) {
                if (grid[rowIndex][0] == 0 || grid[0][colIndex] == 0) {
                    grid[rowIndex][colIndex] = 0;
                }
            }
        }
    }

    private void zeroOutRow(final int[][] grid, final int rowIndex) {
        for (int colIndex = 0; colIndex < grid[0].length; colIndex++) {
            grid[rowIndex][colIndex] = 0;
        }
    }

    private void zeroOutColumn(final int[][] grid, final int colIndex) {
        for (int rowIndex = 0; rowIndex < grid.length; rowIndex++) {
            grid[rowIndex][colIndex] = 0;
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Use first row/col as markers; remember if they themselves contained 0
 *  via two flags. Apply markers last to avoid corrupting them.
 *
 *  Complexity: Time O(m*n), Space O(1).
 *  Pattern: encode metadata in the data itself.
 * ============================================================ */
