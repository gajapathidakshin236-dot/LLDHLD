package com.company.DSA;

/* ============================================================
 *  LeetCode #48 — Rotate Image
 * ============================================================
 *
 *  PROBLEM
 *  -------
 *  You are given an n x n 2D matrix representing an image. Rotate the image
 *  by 90 degrees clockwise. You must rotate in place — DO NOT allocate
 *  another 2D matrix.
 *
 *  EXAMPLES
 *  --------
 *  Example 1:
 *    Input:  [[1,2,3],[4,5,6],[7,8,9]]
 *    Output: [[7,4,1],[8,5,2],[9,6,3]]
 *    Why:    Row 1 becomes the last column of output, etc.
 *
 *  Example 2:
 *    Input:  [[5,1,9,11],[2,4,8,10],[13,3,6,7],[15,14,12,16]]
 *    Output: [[15,13,2,5],[14,3,4,1],[12,6,8,9],[16,7,10,11]]
 *
 *  Example 3:
 *    Input:  [[1]]
 *    Output: [[1]]
 *
 *  CONSTRAINTS
 *  -----------
 *   n == matrix.length == matrix[i].length
 *   1 <= n <= 20
 *   -1000 <= matrix[i][j] <= 1000
 *
 *  HINTS
 *  -----
 *   1. Rotating 90° clockwise = transpose + reverse each row.
 *   2. Try it on paper with a 3x3 — you'll see the trick immediately.
 *   3. Transpose by swapping (i,j) with (j,i) for j > i ONLY (else you'd undo it).
 * ============================================================ */
public class RotateImage {
    public void rotate(int[][] m) {
        int n = m.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int t = m[i][j]; m[i][j] = m[j][i]; m[j][i] = t;
            }
        }
        for (int i = 0; i < n; i++) {
            for (int l = 0, r = n - 1; l < r; l++, r--) {
                int t = m[i][l]; m[i][l] = m[i][r]; m[i][r] = t;
            }
        }
    }
}

/* ============================================================
 *  APPROACH EXPLAINED
 * ============================================================
 *
 *  Why this works (the geometric intuition):
 *    A 90° clockwise rotation maps cell (i,j) to (j, n-1-i).
 *    We decompose that single mapping into TWO simple ones:
 *      Step A — Transpose:     (i,j) → (j,i)
 *      Step B — Reverse rows:  (j,i) → (j, n-1-i)
 *    Composing them gives the rotation we want.
 *
 *  Step-by-step:
 *    1. Transpose: swap matrix[i][j] with matrix[j][i] for j > i.
 *       (j > i guarantees each pair is swapped exactly once.)
 *    2. Reverse each row left-to-right.
 *
 *  Why not rotate "4 cells at a time" (the layer approach)?
 *    That also works in O(n^2)/O(1) but the index math is fiddly and
 *    error-prone in interviews. Transpose+reverse is much cleaner.
 *
 *  Complexity:
 *    Time:  O(n^2)  — every cell touched a constant number of times.
 *    Space: O(1)    — only a temporary int.
 *
 *  Edge cases:
 *    - n == 1 → no swaps, returns unchanged.
 *    - Even n vs odd n → both work, transpose loop covers each pair once.
 *
 *  Pattern:
 *    Decompose a complex transform into simpler primitives (transpose, reverse).
 *    Counter-clockwise = transpose + reverse columns (same trick reversed).
 * ============================================================ */
