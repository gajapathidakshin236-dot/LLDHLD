package com.company.DSA.matrix;

/* ============================================================
 *  LeetCode #48 — Rotate Image
 * ============================================================
 *  PROBLEM
 *  -------
 *  Rotate an n x n matrix 90° clockwise IN PLACE.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [[1,2,3],[4,5,6],[7,8,9]] → [[7,4,1],[8,5,2],[9,6,3]]
 *  Ex2: [[1]] → [[1]]
 *
 *  CONSTRAINTS:  1 <= n <= 20.
 *
 *  HINTS
 *  -----
 *   1. Transpose then reverse each row.
 * ============================================================ */
public class RotateImage {

    public void rotate(final int[][] matrix) {
        transposeInPlace(matrix);
        reverseEachRow(matrix);
    }

    private void transposeInPlace(final int[][] matrix) {
        final int size = matrix.length;
        for (int rowIndex = 0; rowIndex < size; rowIndex++) {
            for (int colIndex = rowIndex + 1; colIndex < size; colIndex++) {
                final int swap = matrix[rowIndex][colIndex];
                matrix[rowIndex][colIndex] = matrix[colIndex][rowIndex];
                matrix[colIndex][rowIndex] = swap;
            }
        }
    }

    private void reverseEachRow(final int[][] matrix) {
        final int size = matrix.length;
        for (int rowIndex = 0; rowIndex < size; rowIndex++) {
            int leftCursor  = 0;
            int rightCursor = size - 1;
            while (leftCursor < rightCursor) {
                final int swap = matrix[rowIndex][leftCursor];
                matrix[rowIndex][leftCursor]  = matrix[rowIndex][rightCursor];
                matrix[rowIndex][rightCursor] = swap;
                leftCursor++;
                rightCursor--;
            }
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Decompose 90° rotation into transpose + reverse-row. Pure O(1) space.
 *
 *  Complexity: Time O(n^2), Space O(1).
 *  Pattern: decompose complex transform into simpler primitives.
 * ============================================================ */
