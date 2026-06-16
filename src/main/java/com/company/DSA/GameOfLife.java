package com.company.DSA;

/**
 * LeetCode #289 - Game of Life
 * Encode new state in the second bit (bit1=next, bit0=cur). Then shift right.
 * Time: O(m*n)  Space: O(1)
 */
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
