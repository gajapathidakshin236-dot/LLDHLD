package com.company.DSA;

/**
 * LeetCode #1652 - Defuse the Bomb
 * For k>0 sum next k circularly; k<0 sum previous |k|; k=0 zero out.
 * Time: O(n*|k|) but works with sliding window for O(n).
 */
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
