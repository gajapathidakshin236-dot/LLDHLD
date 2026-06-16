package com.company.DSA;

/* ============================================================
 *  LeetCode #1652 — Defuse the Bomb
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given circular code[] and k:
 *    k > 0: each code[i] = sum of next k elements (wrap around).
 *    k < 0: each code[i] = sum of previous |k| elements (wrap).
 *    k = 0: replace every element with 0.
 *  Return the resulting array.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: code=[5,7,1,4], k=3   → [12,10,16,13]
 *  Ex2: code=[1,2,3,4], k=0   → [0,0,0,0]
 *  Ex3: code=[2,4,9,3], k=-2  → [12,5,6,13]
 *
 *  CONSTRAINTS:  1 <= n <= 100;  -(n-1) <= k <= n-1;  1 <= code[i] <= 100.
 *
 *  HINTS
 *  -----
 *   1. Modulo with `+n` to handle negatives: ((i + j) % n + n) % n.
 *   2. Sliding window optimization possible but n <= 100 → simple loop OK.
 * ============================================================ */
public class DefuseTheBomb {
    public int[] decrypt(int[] code, int k) {
        int n = code.length;
        int[] res = new int[n];
        if (k == 0) return res;
        for (int i = 0; i < n; i++) {
            int sum = 0;
            if (k > 0) for (int j = 1; j <= k; j++) sum += code[(i + j) % n];
            else for (int j = 1; j <= -k; j++) sum += code[((i - j) % n + n) % n];
            res[i] = sum;
        }
        return res;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Direct simulation with circular indexing. For each i, sum the |k| neighbors
 *  in the chosen direction. Modular arithmetic handles wrap-around.
 *  Sliding window can do O(n) but unnecessary for the constraints.
 *
 *  Complexity: Time O(n * |k|), Space O(n).
 *  Edge cases: k=0 → all zeros; |k| == n-1 → sum of all others.
 *  Pattern: circular array simulation.
 * ============================================================ */
