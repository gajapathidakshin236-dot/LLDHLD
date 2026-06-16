package com.company.DSA;

/**
 * LeetCode #79 - Word Search
 * DFS each cell; backtrack with temp char swap to mark visited.
 * Time: O(m*n * 4^L)  Space: O(L)
 */
public class WordSearch {
    public boolean exist(char[][] board, String word) {
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[0].length; j++)
                if (dfs(board, i, j, word, 0)) return true;
        return false;
    }
    private boolean dfs(char[][] b, int i, int j, String w, int k) {
        if (k == w.length()) return true;
        if (i < 0 || j < 0 || i >= b.length || j >= b[0].length || b[i][j] != w.charAt(k)) return false;
        char t = b[i][j]; b[i][j] = '#';
        boolean ok = dfs(b, i+1, j, w, k+1) || dfs(b, i-1, j, w, k+1)
                  || dfs(b, i, j+1, w, k+1) || dfs(b, i, j-1, w, k+1);
        b[i][j] = t;
        return ok;
    }
}
