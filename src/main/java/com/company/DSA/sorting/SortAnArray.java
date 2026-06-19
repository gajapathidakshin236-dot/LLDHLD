package com.company.DSA.sorting;

/* ============================================================
 *  LeetCode #912 — Sort an Array (mergesort)
 * ============================================================
 *  PROBLEM
 *  -------
 *  Sort the array in O(n log n) without using library sort.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [5,2,3,1] → [1,2,3,5]
 *  Ex2: [5,1,1,2,0,0] → [0,0,1,1,2,5]
 *
 *  CONSTRAINTS:  1 <= n <= 5*10^4.
 *
 *  HINTS
 *  -----
 *   1. Mergesort gives deterministic O(n log n).
 * ============================================================ */
public class SortAnArray {

    public int[] sortArray(final int[] numbers) {
        final int[] auxiliaryBuffer = new int[numbers.length];
        mergesort(numbers, auxiliaryBuffer, 0, numbers.length - 1);
        return numbers;
    }

    private void mergesort(final int[] numbers,
                           final int[] auxiliaryBuffer,
                           final int leftBound,
                           final int rightBound) {
        if (leftBound >= rightBound) {
            return;
        }
        final int midIndex = leftBound + (rightBound - leftBound) / 2;
        mergesort(numbers, auxiliaryBuffer, leftBound,        midIndex);
        mergesort(numbers, auxiliaryBuffer, midIndex + 1,     rightBound);
        merge   (numbers, auxiliaryBuffer, leftBound, midIndex, rightBound);
    }

    private void merge(final int[] numbers,
                       final int[] auxiliaryBuffer,
                       final int leftBound,
                       final int midIndex,
                       final int rightBound) {
        for (int index = leftBound; index <= rightBound; index++) {
            auxiliaryBuffer[index] = numbers[index];
        }

        int leftCursor  = leftBound;
        int rightCursor = midIndex + 1;
        int writeCursor = leftBound;

        while (leftCursor <= midIndex && rightCursor <= rightBound) {
            if (auxiliaryBuffer[leftCursor] <= auxiliaryBuffer[rightCursor]) {
                numbers[writeCursor++] = auxiliaryBuffer[leftCursor++];
            } else {
                numbers[writeCursor++] = auxiliaryBuffer[rightCursor++];
            }
        }
        while (leftCursor <= midIndex) {
            numbers[writeCursor++] = auxiliaryBuffer[leftCursor++];
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Top-down mergesort with shared aux buffer. Predictable O(n log n).
 *
 *  Complexity: Time O(n log n), Space O(n).
 *  Pattern: divide & conquer.
 * ============================================================ */
