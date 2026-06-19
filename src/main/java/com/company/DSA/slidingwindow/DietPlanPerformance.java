package com.company.DSA.slidingwindow;

/* ============================================================
 *  LeetCode #1176 — Diet Plan Performance
 * ============================================================
 *  PROBLEM
 *  -------
 *  Per window of size k: -1 if sum<lower, +1 if sum>upper, else 0. Return total.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: calories=[1,2,3,4,5], k=1, lower=3, upper=3 → 0
 *  Ex2: calories=[3,2], k=2, lower=0, upper=1 → 1
 *  Ex3: calories=[6,5,0,0], k=2, lower=1, upper=5 → 0
 *
 *  CONSTRAINTS:  1 <= n <= 10^5; 0 <= val <= 20000.
 *
 *  HINTS
 *  -----
 *   1. Fixed-size sliding sum with simple scoring.
 * ============================================================ */
public class DietPlanPerformance {

    public int dietPlanPerformance(final int[] calories,
                                   final int windowSize,
                                   final int lowerBound,
                                   final int upperBound) {
        int currentWindowSum = 0;
        int performancePoints = 0;

        for (int index = 0; index < calories.length; index++) {
            currentWindowSum += calories[index];

            if (index >= windowSize) {
                currentWindowSum -= calories[index - windowSize];
            }
            if (index >= windowSize - 1) {
                if (currentWindowSum < lowerBound) {
                    performancePoints--;
                } else if (currentWindowSum > upperBound) {
                    performancePoints++;
                }
            }
        }
        return performancePoints;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Fixed-size sliding sum. Apply +1/-1 based on threshold checks each window.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: fixed window with threshold scoring.
 * ============================================================ */
