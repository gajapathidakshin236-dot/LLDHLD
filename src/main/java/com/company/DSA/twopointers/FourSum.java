package com.company.DSA.twopointers;

import java.util.*;

/* ============================================================
 *  LeetCode #18 — 4Sum
 * ============================================================
 *  PROBLEM
 *  -------
 *  All unique quadruples summing to target.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[1,0,-1,0,-2,2], target=0 → [[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]
 *  Ex2: nums=[2,2,2,2,2], target=8 → [[2,2,2,2]]
 *
 *  CONSTRAINTS:  1 <= n <= 200;  -10^9 <= val/target <= 10^9.
 *
 *  HINTS
 *  -----
 *   1. Sort. Two outer loops; two-pointer inner. Skip dups everywhere.
 * ============================================================ */
public class FourSum {

    public List<List<Integer>> fourSum(final int[] numbers, final int target) {
        Arrays.sort(numbers);
        final List<List<Integer>> quadruples = new ArrayList<>();

        for (int firstIndex = 0; firstIndex < numbers.length - 3; firstIndex++) {
            if (firstIndex > 0 && numbers[firstIndex] == numbers[firstIndex - 1]) {
                continue;
            }
            for (int secondIndex = firstIndex + 1; secondIndex < numbers.length - 2; secondIndex++) {
                if (secondIndex > firstIndex + 1 && numbers[secondIndex] == numbers[secondIndex - 1]) {
                    continue;
                }
                findPairsForRemainder(numbers, firstIndex, secondIndex, target, quadruples);
            }
        }
        return quadruples;
    }

    private void findPairsForRemainder(final int[] numbers,
                                       final int firstIndex,
                                       final int secondIndex,
                                       final int target,
                                       final List<List<Integer>> output) {
        final long requiredPairSum = (long) target - numbers[firstIndex] - numbers[secondIndex];
        int leftCursor             = secondIndex + 1;
        int rightCursor            = numbers.length - 1;

        while (leftCursor < rightCursor) {
            final int currentPairSum = numbers[leftCursor] + numbers[rightCursor];

            if (currentPairSum < requiredPairSum) {
                leftCursor++;
            } else if (currentPairSum > requiredPairSum) {
                rightCursor--;
            } else {
                output.add(Arrays.asList(
                        numbers[firstIndex], numbers[secondIndex],
                        numbers[leftCursor], numbers[rightCursor]));
                while (leftCursor < rightCursor && numbers[leftCursor] == numbers[leftCursor + 1]) {
                    leftCursor++;
                }
                while (leftCursor < rightCursor && numbers[rightCursor] == numbers[rightCursor - 1]) {
                    rightCursor--;
                }
                leftCursor++;
                rightCursor--;
            }
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Sort, then two nested loops fix a, b; inner two-pointer for c, d. Skip
 *  duplicates at every level. Use long for sum to avoid int overflow.
 *
 *  Complexity: Time O(n^3), Space O(1) extra.
 *  Pattern: k-sum recursion.
 * ============================================================ */
