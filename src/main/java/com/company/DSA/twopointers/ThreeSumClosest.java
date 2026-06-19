package com.company.DSA.twopointers;

import java.util.*;

/* ============================================================
 *  LeetCode #16 — 3Sum Closest
 * ============================================================
 *  PROBLEM
 *  -------
 *  Find three nums whose sum is closest to target.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[-1,2,1,-4], target=1 → 2
 *  Ex2: nums=[0,0,0],     target=1 → 0
 *  Ex3: nums=[1,1,1,1],   target=0 → 3
 *
 *  CONSTRAINTS:  3 <= n <= 500;  -1000 <= val <= 1000.
 *
 *  HINTS
 *  -----
 *   1. Sort. Fix i; two pointers l, r minimize |sum - target|.
 * ============================================================ */
public class ThreeSumClosest {

    public int threeSumClosest(final int[] numbers, final int target) {
        Arrays.sort(numbers);
        int closestSumSoFar = numbers[0] + numbers[1] + numbers[2];

        for (int firstIndex = 0; firstIndex < numbers.length - 2; firstIndex++) {
            int leftCursor  = firstIndex + 1;
            int rightCursor = numbers.length - 1;

            while (leftCursor < rightCursor) {
                final int currentSum = numbers[firstIndex] + numbers[leftCursor] + numbers[rightCursor];

                if (Math.abs(currentSum - target) < Math.abs(closestSumSoFar - target)) {
                    closestSumSoFar = currentSum;
                }
                if (currentSum < target) {
                    leftCursor++;
                } else {
                    rightCursor--;
                }
            }
        }
        return closestSumSoFar;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Sort + fix-one + two-pointer. Each iteration shrinks the search by sign
 *  of (currentSum - target).
 *
 *  Complexity: Time O(n^2), Space O(1).
 *  Pattern: 2-pointer optimization on a sorted array.
 * ============================================================ */
