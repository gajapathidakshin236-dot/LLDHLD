package com.company.DSA.twopointers;

import java.util.*;

/* ============================================================
 *  LeetCode #611 — Valid Triangle Number
 * ============================================================
 *  PROBLEM
 *  -------
 *  Count triples (i<j<k) that form a valid triangle.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [2,2,3,4] → 3
 *  Ex2: [4,2,3,4] → 4
 *  Ex3: [0,0,0]   → 0
 *
 *  CONSTRAINTS:  1 <= n <= 1000;  0 <= val <= 1000.
 *
 *  HINTS
 *  -----
 *   1. Sort. Fix largest side k. Two pointers on smaller pair.
 * ============================================================ */
public class ValidTriangleNumber {

    public int triangleNumber(final int[] sideLengths) {
        Arrays.sort(sideLengths);
        int totalValidTriangles = 0;

        for (int largestSideIndex = sideLengths.length - 1; largestSideIndex >= 2; largestSideIndex--) {
            int leftCursor  = 0;
            int rightCursor = largestSideIndex - 1;

            while (leftCursor < rightCursor) {
                if (sideLengths[leftCursor] + sideLengths[rightCursor] > sideLengths[largestSideIndex]) {
                    totalValidTriangles += rightCursor - leftCursor;
                    rightCursor--;
                } else {
                    leftCursor++;
                }
            }
        }
        return totalValidTriangles;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Sort; fix the LARGEST side and use two pointers on the smaller pair. For
 *  sorted sides, only check a + b > c. When sum > c, all pairs (l..r-1, r)
 *  also work → ans += r - l.
 *
 *  Complexity: Time O(n^2), Space O(1) extra.
 *  Pattern: sort + two pointers on smaller pair.
 * ============================================================ */
