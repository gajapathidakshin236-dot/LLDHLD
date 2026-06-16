package com.company.DSA;

/**
 * LeetCode #1176 - Diet Plan Performance
 * Sliding window of size k summing calories.
 * Time: O(n)  Space: O(1)
 */
public class DietPlanPerformance {
    public int dietPlanPerformance(int[] cal, int k, int lower, int upper) {
        int sum = 0, points = 0;
        for (int i = 0; i < cal.length; i++) {
            sum += cal[i];
            if (i >= k) sum -= cal[i - k];
            if (i >= k - 1) {
                if (sum < lower) points--;
                else if (sum > upper) points++;
            }
        }
        return points;
    }
}
