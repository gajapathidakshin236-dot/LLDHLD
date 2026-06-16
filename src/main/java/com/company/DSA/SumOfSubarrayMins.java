package com.company.DSA;

import java.util.*;

/**
 * LeetCode #907 - Sum of Subarray Minimums
 * For each element, count how many subarrays it is the minimum of, using prev/next less stacks.
 * Time: O(n)  Space: O(n)
 */
public class SumOfSubarrayMins {
    public int sumSubarrayMins(int[] arr) {
        int MOD = 1_000_000_007, n = arr.length;
        int[] pl = new int[n], nl = new int[n];
        Deque<Integer> st = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!st.isEmpty() && arr[st.peek()] >= arr[i]) st.pop();
            pl[i] = st.isEmpty() ? -1 : st.peek();
            st.push(i);
        }
        st.clear();
        for (int i = n - 1; i >= 0; i--) {
            while (!st.isEmpty() && arr[st.peek()] > arr[i]) st.pop();
            nl[i] = st.isEmpty() ? n : st.peek();
            st.push(i);
        }
        long ans = 0;
        for (int i = 0; i < n; i++) {
            ans = (ans + (long) arr[i] * (i - pl[i]) % MOD * (nl[i] - i)) % MOD;
        }
        return (int) ans;
    }
}
