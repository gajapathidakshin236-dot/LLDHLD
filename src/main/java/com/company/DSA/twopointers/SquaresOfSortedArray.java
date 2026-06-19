package com.company.DSA.twopointers;

/* ============================================================
 *  LeetCode #977 — Squares of a Sorted Array
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return squares of each value, sorted ascending. O(n) required.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [-4,-1,0,3,10] → [0,1,9,16,100]
 *  Ex2: [-7,-3,2,3,11] → [4,9,9,49,121]
 *  Ex3: [1,2,3,4] → [1,4,9,16]
 *
 *  CONSTRAINTS:  1 <= n <= 10^4;  -10^4 <= val <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. Largest square is at one of the ends. Two pointers, fill from the end.
 * ============================================================ */
public class SquaresOfSortedArray {

    public int[] sortedSquares(final int[] sortedNumbers) {
        final int length     = sortedNumbers.length;
        final int[] result   = new int[length];
        int leftCursor       = 0;
        int rightCursor      = length - 1;
        int writeIndex       = length - 1;

        while (leftCursor <= rightCursor) {
            final int leftSquare  = sortedNumbers[leftCursor]  * sortedNumbers[leftCursor];
            final int rightSquare = sortedNumbers[rightCursor] * sortedNumbers[rightCursor];

            if (leftSquare > rightSquare) {
                result[writeIndex--] = leftSquare;
                leftCursor++;
            } else {
                result[writeIndex--] = rightSquare;
                rightCursor--;
            }
        }
        return result;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Two pointers from both ends. The bigger-square element goes to the current
 *  end of the output and that pointer advances inward.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: two-pointer merge of two virtual sorted streams.
 * ============================================================ */
