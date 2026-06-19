package com.company.DSA.recursion;

/* ============================================================
 *  LeetCode #79 — Word Search
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return true if word can be formed by adjacent (UDLR) cells, each used once.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: board=[["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word="ABCCED" → true
 *  Ex2: word="SEE" → true
 *  Ex3: word="ABCB" → false
 *
 *  CONSTRAINTS:  1 <= m,n <= 6; 1 <= word.length <= 15.
 *
 *  HINTS
 *  -----
 *   1. DFS from each cell; mark visited by mutating value.
 * ============================================================ */
public class WordSearch {

    private static final char VISITED_MARKER = '#';
    private static final int[][] DIRECTIONS = {
            { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }
    };

    public boolean exist(final char[][] board, final String word) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (depthFirstSearch(board, row, col, word, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean depthFirstSearch(final char[][] board,
                                     final int currentRow,
                                     final int currentCol,
                                     final String word,
                                     final int matchedCount) {
        if (matchedCount == word.length()) {
            return true;
        }
        if (isOutOfBounds(board, currentRow, currentCol)
                || board[currentRow][currentCol] != word.charAt(matchedCount)) {
            return false;
        }

        final char originalCharacter = board[currentRow][currentCol];
        board[currentRow][currentCol] = VISITED_MARKER;

        boolean foundInAnyDirection = false;
        for (final int[] step : DIRECTIONS) {
            if (depthFirstSearch(board,
                                 currentRow + step[0],
                                 currentCol + step[1],
                                 word,
                                 matchedCount + 1)) {
                foundInAnyDirection = true;
                break;
            }
        }
        board[currentRow][currentCol] = originalCharacter;
        return foundInAnyDirection;
    }

    private boolean isOutOfBounds(final char[][] board, final int row, final int col) {
        return row < 0 || col < 0 || row >= board.length || col >= board[0].length;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  DFS from each cell; backtrack via temporary char mutation. Iterate directions.
 *
 *  Complexity: Time O(m*n*4^L), Space O(L) recursion.
 *  Pattern: 2D backtracking with grid + visited mark.
 * ============================================================ */
