package com.company.DSA.arrays;

/* ============================================================
 *  LeetCode #31 — Next Permutation
 * ============================================================
 *  PROBLEM
 *  -------
 *  Rearrange nums into the next lex-greater permutation, in place.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,2,3] → [1,3,2]
 *  Ex2: [3,2,1] → [1,2,3]   (wrap)
 *  Ex3: [1,1,5] → [1,5,1]
 *  Ex4: [1,3,2] → [2,1,3]
 *
 *  CONSTRAINTS:  1 <= n <= 100.
 *
 *  HINTS
 *  -----
 *   1. Find pivot (last i with nums[i] < nums[i+1]).
 *   2. Swap with smallest greater value in suffix.
 *   3. Reverse the suffix.
 * ============================================================ */
public class NextPermutation {

    public void nextPermutation(final int[] numbers) {
        final int pivotIndex = findPivotIndex(numbers);

        if (pivotIndex >= 0) {
            final int swapIndex = findSmallestGreaterInSuffix(numbers, pivotIndex);
            swap(numbers, pivotIndex, swapIndex);
        }
        reverseRange(numbers, pivotIndex + 1, numbers.length - 1);
    }

    private int findPivotIndex(final int[] numbers) {
        for (int index = numbers.length - 2; index >= 0; index--) {
            if (numbers[index] < numbers[index + 1]) {
                return index;
            }
        }
        return -1;
    }

    private int findSmallestGreaterInSuffix(final int[] numbers, final int pivotIndex) {
        for (int candidate = numbers.length - 1; candidate > pivotIndex; candidate--) {
            if (numbers[candidate] > numbers[pivotIndex]) {
                return candidate;
            }
        }
        throw new IllegalStateException();
    }

    private void swap(final int[] numbers, final int firstIndex, final int secondIndex) {
        final int swap = numbers[firstIndex];
        numbers[firstIndex]  = numbers[secondIndex];
        numbers[secondIndex] = swap;
    }

    private void reverseRange(final int[] numbers, int leftCursor, int rightCursor) {
        while (leftCursor < rightCursor) {
            swap(numbers, leftCursor++, rightCursor--);
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Standard "next-permutation":
 *    Pivot = last index where suffix isn't fully descending.
 *    Swap pivot with rightmost element greater than it; reverse suffix.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: combinatorial generation.
 * ============================================================ */
