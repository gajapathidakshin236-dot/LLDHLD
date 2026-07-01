package com.company.DSA.srivatsanDSA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 43
 *  Problem: 3Sum — all unique triplets summing to 0.
 *
 *  APPROACH (from notes):
 *    Sort the array.
 *    For each i: two-pointer (l = i+1, r = n-1) on the remaining range.
 *      Skip duplicates at i, l, and r to avoid repeats.
 *      sum < 0 → l++;  sum > 0 → r--;  sum == 0 → record triplet, advance both.
 * ============================================================ */
public class ThreeSum {

    public List<List<Integer>> threeSum(final int[] numbers) {
        Arrays.sort(numbers);
        final List<List<Integer>> triplets = new ArrayList<>();

        for (int firstIndex = 0; firstIndex < numbers.length - 2; firstIndex++) {
            if (firstIndex > 0 && numbers[firstIndex] == numbers[firstIndex - 1]) {
                continue;
            }
            int leftCursor  = firstIndex + 1;
            int rightCursor = numbers.length - 1;

            while (leftCursor < rightCursor) {
                final int sum = numbers[firstIndex] + numbers[leftCursor] + numbers[rightCursor];
                if (sum < 0) {
                    leftCursor++;
                } else if (sum > 0) {
                    rightCursor--;
                } else {
                    triplets.add(Arrays.asList(numbers[firstIndex], numbers[leftCursor], numbers[rightCursor]));
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
        return triplets;
    }
}
