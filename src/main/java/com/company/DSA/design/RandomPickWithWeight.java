package com.company.DSA.design;

import java.util.*;

/* ============================================================
 *  LeetCode #528 — Random Pick with Weight
 * ============================================================
 *  PROBLEM
 *  -------
 *  Pick index i with probability w[i] / sum(w).
 *
 *  EXAMPLES
 *  --------
 *  w=[1,3]: index 0 ~25%, index 1 ~75%.
 *  w=[1]: always 0.
 *  w=[1,1,1,1]: uniform.
 *
 *  CONSTRAINTS:  1 <= w.length <= 10^4; 1 <= w[i] <= 10^5; <=10^4 calls.
 *
 *  HINTS
 *  -----
 *   1. Prefix sums; pick random in [1..total]; binary search first prefix >= r.
 * ============================================================ */
public class RandomPickWithWeight {

    private final int[]  cumulativeWeights;
    private final Random randomGenerator = new Random();

    public RandomPickWithWeight(final int[] weights) {
        this.cumulativeWeights = new int[weights.length];
        cumulativeWeights[0]   = weights[0];
        for (int index = 1; index < weights.length; index++) {
            cumulativeWeights[index] = cumulativeWeights[index - 1] + weights[index];
        }
    }

    public int pickIndex() {
        final int totalWeight = cumulativeWeights[cumulativeWeights.length - 1];
        final int randomTarget = randomGenerator.nextInt(totalWeight) + 1;
        return findFirstIndexWithCumulativeAtLeast(randomTarget);
    }

    private int findFirstIndexWithCumulativeAtLeast(final int randomTarget) {
        int leftCursor  = 0;
        int rightCursor = cumulativeWeights.length - 1;

        while (leftCursor < rightCursor) {
            final int midIndex = leftCursor + (rightCursor - leftCursor) / 2;
            if (cumulativeWeights[midIndex] < randomTarget) {
                leftCursor = midIndex + 1;
            } else {
                rightCursor = midIndex;
            }
        }
        return leftCursor;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Discrete inverse-CDF: weight ranges = prefix sums; random pick into [1..total]
 *  identified via binary search.
 *
 *  Complexity: Build O(n), pick O(log n), Space O(n).
 *  Pattern: discrete inverse-CDF sampling.
 * ============================================================ */
