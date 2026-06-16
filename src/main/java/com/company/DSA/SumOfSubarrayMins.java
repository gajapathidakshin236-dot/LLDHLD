package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #907 — Sum of Subarray Minimums
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given array arr, return the sum of min(b) for every contiguous subarray b,
 *  modulo 10^9 + 7.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [3,1,2,4] → 17
 *      subs: [3](3), [1](1), [2](2), [4](4), [3,1](1), [1,2](1), [2,4](2),
 *            [3,1,2](1), [1,2,4](1), [3,1,2,4](1). Sum = 17.
 *  Ex2: [11,81,94,43,3] → 444
 *
 *  CONSTRAINTS:  1 <= n <= 3*10^4;  1 <= val <= 3*10^4.
 *
 *  HINTS
 *  -----
 *   1. Instead of enumerating subarrays (O(n^2)), count CONTRIBUTION of each element.
 *   2. arr[i] is min of subarrays spanning (prevLessIdx, nextLessIdx).
 *   3. Use monotonic stacks for previous/next "less" indices.
 *   4. Contribution = arr[i] * (i - PL) * (NL - i).
 * ============================================================ */
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

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Contribution counting:
 *    Element arr[i] is the minimum of every subarray that:
 *      - starts in (PL, i]   (PL = previous strictly smaller index, or -1)
 *      - ends   in [i, NL)   (NL = next smaller-or-equal index, or n)
 *    Number of such subarrays = (i - PL) * (NL - i).
 *    Contribution = arr[i] * count.
 *
 *  Strict vs non-strict comparisons:
 *    To avoid double-counting equal values, one side uses STRICT (<) and the
 *    other uses NON-STRICT (<=). Here: PL strict (>=), NL strict (>).
 *
 *  Why monotonic stacks:
 *    They compute previous/next "less" indices in O(n).
 *
 *  Complexity: Time O(n), Space O(n).
 *  Edge cases: all equal — break ties by strict-on-one-side rule above.
 *  Pattern: "contribution by element" + "monotonic stack" — used in many sum-of-min/max problems.
 * ============================================================ */
