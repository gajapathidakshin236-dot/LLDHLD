package com.company.DSA.prefixsum;

import java.util.*;

/* ============================================================
 *  LeetCode #1590 — Make Sum Divisible by P
 * ============================================================
 *  PROBLEM
 *  -------
 *  Remove the shortest subarray so remaining sum is divisible by p.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[3,1,4,2], p=6 → 1
 *  Ex2: nums=[6,3,5,2], p=9 → 2
 *  Ex3: nums=[1,2,3], p=3 → 0
 *  Ex4: nums=[1,2,3], p=7 → -1
 *
 *  CONSTRAINTS:  1 <= n <= 10^5;  1 <= val <= 10^9;  1 <= p <= 10^9.
 *
 *  HINTS
 *  -----
 *   1. Removed subarray's sum mod p must equal totalSum mod p.
 *   2. Hashmap of latest index per remainder.
 * ============================================================ */
public class MakeSumDivisibleByP {

    public int minSubarray(final int[] numbers, final int divisor) {
        final int totalRemainder = computeTotalRemainder(numbers, divisor);
        if (totalRemainder == 0) {
            return 0;
        }

        final Map<Integer, Integer> latestIndexByRemainder = new HashMap<>();
        latestIndexByRemainder.put(0, -1);

        int runningPrefixSum = 0;
        int shortestRemovalLength = numbers.length;

        for (int index = 0; index < numbers.length; index++) {
            runningPrefixSum = (runningPrefixSum + numbers[index]) % divisor;
            final int requiredPreviousRemainder = (runningPrefixSum - totalRemainder + divisor) % divisor;

            if (latestIndexByRemainder.containsKey(requiredPreviousRemainder)) {
                shortestRemovalLength = Math.min(
                        shortestRemovalLength,
                        index - latestIndexByRemainder.get(requiredPreviousRemainder));
            }
            latestIndexByRemainder.put(runningPrefixSum, index);
        }
        return shortestRemovalLength == numbers.length ? -1 : shortestRemovalLength;
    }

    private int computeTotalRemainder(final int[] numbers, final int divisor) {
        int remainder = 0;
        for (final int value : numbers) {
            remainder = (remainder + value) % divisor;
        }
        return remainder;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Find shortest subarray whose sum mod p equals target. Prefix mod + hashmap
 *  of LATEST index per remainder minimizes window length.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: prefix mod hashmap.
 * ============================================================ */
