package com.company.DSA.arrays;

/* ============================================================
 *  LeetCode #287 — Find the Duplicate Number
 * ============================================================
 *  PROBLEM
 *  -------
 *  Array of n+1 in [1..n] with one duplicate. Find it. Don't modify; O(1) space.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,3,4,2,2] → 2
 *  Ex2: [3,1,3,4,2] → 3
 *  Ex3: [1,1]       → 1
 *
 *  CONSTRAINTS:  1 <= n <= 10^5; one duplicate.
 *
 *  HINTS
 *  -----
 *   1. Treat array as a function f(i)=nums[i]. Duplicate makes a CYCLE.
 *   2. Floyd's cycle detection.
 * ============================================================ */
public class FindDuplicateNumber {

    public int findDuplicate(final int[] numbers) {
        int slowRunner = numbers[0];
        int fastRunner = numbers[0];

        do {
            slowRunner = numbers[slowRunner];
            fastRunner = numbers[numbers[fastRunner]];
        } while (slowRunner != fastRunner);

        int probe = numbers[0];
        while (probe != slowRunner) {
            probe      = numbers[probe];
            slowRunner = numbers[slowRunner];
        }
        return probe;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Floyd's tortoise and hare on the index-function. Cycle entry = duplicate value.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: Floyd's cycle detection generalized.
 * ============================================================ */
