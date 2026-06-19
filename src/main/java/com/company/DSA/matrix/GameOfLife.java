package com.company.DSA.matrix;

/* ============================================================
 *  LeetCode #289 — Game of Life
 * ============================================================
 *  PROBLEM
 *  -------
 *  Compute next state of Conway's Game of Life in place.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [[0,1,0],[0,0,1],[1,1,1],[0,0,0]] → [[0,0,0],[1,0,1],[0,1,1],[0,1,0]]
 *  Ex2: [[1,1],[1,0]] → [[1,1],[1,1]]
 *
 *  CONSTRAINTS:  1 <= m,n <= 25; values 0 or 1.
 *
 *  HINTS
 *  -----
 *   1. Pack NEXT state into bit 1 (& 2). Shift right after pass.
 * ============================================================ */
public class GameOfLife {

    private static final int CURRENT_STATE_MASK = 1;
    private static final int NEXT_STATE_MASK    = 2;
    private static final int[][] NEIGHBOR_OFFSETS = {
            {-1,-1},{-1,0},{-1,1},
            { 0,-1},       { 0,1},
            { 1,-1},{ 1,0},{ 1,1}
    };

    public void gameOfLife(final int[][] board) {
        final int rowCount = board.length;
        final int colCount = board[0].length;

        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            for (int colIndex = 0; colIndex < colCount; colIndex++) {
                final int liveNeighborCount = countLiveNeighbors(board, rowIndex, colIndex);
                final boolean cellIsLive    = (board[rowIndex][colIndex] & CURRENT_STATE_MASK) == 1;

                if (cellIsLive && (liveNeighborCount == 2 || liveNeighborCount == 3)) {
                    board[rowIndex][colIndex] |= NEXT_STATE_MASK;
                }
                if (!cellIsLive && liveNeighborCount == 3) {
                    board[rowIndex][colIndex] |= NEXT_STATE_MASK;
                }
            }
        }

        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            for (int colIndex = 0; colIndex < colCount; colIndex++) {
                board[rowIndex][colIndex] >>= 1;
            }
        }
    }

    private int countLiveNeighbors(final int[][] board, final int rowIndex, final int colIndex) {
        int liveNeighborCount = 0;
        for (final int[] offset : NEIGHBOR_OFFSETS) {
            final int neighborRow = rowIndex + offset[0];
            final int neighborCol = colIndex + offset[1];

            final boolean isInBounds = neighborRow >= 0 && neighborRow < board.length
                    && neighborCol >= 0 && neighborCol < board[0].length;

            if (isInBounds) {
                liveNeighborCount += board[neighborRow][neighborCol] & CURRENT_STATE_MASK;
            }
        }
        return liveNeighborCount;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Two-bit trick: bit 0 = current state; bit 1 = next state. Compute all next
 *  states with original bit-0 data; shift right at the end.
 *
 *  Complexity: Time O(m*n), Space O(1).
 *  Pattern: pack new state into spare bits.
 * ============================================================ */
