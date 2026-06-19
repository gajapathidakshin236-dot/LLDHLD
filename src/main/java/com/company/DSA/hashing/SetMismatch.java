package com.company.DSA.hashing;

/* ============================================================
 *  LeetCode #645 — Set Mismatch
 * ============================================================
 *  PROBLEM
 *  -------
 *  One value duplicated, another missing in 1..n. Return [duplicate, missing].
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,2,2,4] → [2,3]
 *  Ex2: [1,1]     → [1,2]
 *  Ex3: [3,2,2]   → [2,1]
 *
 *  CONSTRAINTS:  2 <= n <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. Negate-index marking trick.
 * ============================================================ */
public class SetMismatch {

    public int[] findErrorNums(final int[] numbers) {
        int duplicateValue = -1;
        int missingValue   = -1;

        for (final int rawValue : numbers) {
            final int indexToFlip = Math.abs(rawValue) - 1;
            if (numbers[indexToFlip] < 0) {
                duplicateValue = indexToFlip + 1;
            } else {
                numbers[indexToFlip] = -numbers[indexToFlip];
            }
        }

        for (int index = 0; index < numbers.length; index++) {
            if (numbers[index] > 0) {
                missingValue = index + 1;
            }
        }
        return new int[] { duplicateValue, missingValue };
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Index-marking. Negate nums[v-1] when value v observed; if already negative
 *  → duplicate. Index still positive at end → missing.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: in-place value-as-index marking.
 * ============================================================ */
