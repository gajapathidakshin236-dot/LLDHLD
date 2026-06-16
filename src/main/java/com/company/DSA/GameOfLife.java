package com.company.DSA;

/* ============================================================
 *  LeetCode #289 — Game of Life
 * ============================================================
 *  PROBLEM
 *  -------
 *  Conway's Game of Life: each cell with 8 neighbors evolves per rules:
 *    Live cell with <2 live neighbors → dies (underpopulation).
 *    Live cell with 2 or 3 → lives.
 *    Live cell with >3 → dies (overpopulation).
 *    Dead cell with exactly 3 → lives (reproduction).
 *  Compute the NEXT state in place.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [[0,1,0],[0,0,1],[1,1,1],[0,0,0]]
 *      → [[0,0,0],[1,0,1],[0,1,1],[0,1,0]]
 *  Ex2: [[1,1],[1,0]] → [[1,1],[1,1]]
 *
 *  CONSTRAINTS:  1 <= m,n <= 25; board[i][j] is 0 or 1.
 *
 *  HINTS
 *  -----
 *   1. Pass 1 corrupts the data needed for Pass 2 — must encode next state separately.
 *   2. Pack NEXT into bit 1 (& 2), keep CURRENT in bit 0 (& 1).
 *   3. After computing all cells, shift right by 1.
 * ============================================================ */
public class GameOfLife {
    public void gameOfLife(int[][] board) {
        int m = board.length, n = board[0].length;
        int[] dx = {-1,-1,-1,0,0,1,1,1}, dy = {-1,0,1,-1,1,-1,0,1};
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int live = 0;
                for (int d = 0; d < 8; d++) {
                    int x = i + dx[d], y = j + dy[d];
                    if (x >= 0 && x < m && y >= 0 && y < n) live += board[x][y] & 1;
                }
                if ((board[i][j] & 1) == 1 && (live == 2 || live == 3)) board[i][j] |= 2;
                if ((board[i][j] & 1) == 0 && live == 3)                 board[i][j] |= 2;
            }
        }
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                board[i][j] >>= 1;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Two-bit trick:
 *    bit 0 = current state, bit 1 = NEXT state.
 *    When counting neighbors, read & 1 (current). When setting next, set bit 1 with |= 2.
 *    Final pass shifts right by 1 to make next become current.
 *
 *  Why we need the trick:
 *    Naive "set in place" would change a neighbor's value mid-pass and break
 *    subsequent neighbor counts for that pass.
 *
 *  Complexity: Time O(m*n*8) = O(m*n), Space O(1).
 *  Edge cases: 1x1; all dead; all alive (everyone dies of overpop except corner cases).
 *  Pattern: "pack new state into spare bits" trick — same as #73 Set Matrix Zeroes.
 * ============================================================ */
