package com.company.DSA.srivatsanDSA;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 11
 *  Problem: Move Zeroes
 *  Move all 0's to the end while maintaining relative order of non-zeros.
 *  Input: [0,1,0,3,12]  Output: [1,3,12,0,0]
 *
 *  APPROACH (from notes):
 *    int j = 0;
 *    for (int i = 0; i < nums.length; i++)
 *        if (nums[i] != 0) { nums[j] = nums[i]; j++; }
 *    while (j < nums.length) nums[j++] = 0;
 *
 *  Two pointers: i scans, j writes non-zero values. Then fill rest with 0s.
 * ============================================================ */
public class MoveZeroes {

    public void moveZeroes(final int[] numbers) {
        int writeIndex = 0;

        for (int readIndex = 0; readIndex < numbers.length; readIndex++) {
            if (numbers[readIndex] != 0) {
                numbers[writeIndex++] = numbers[readIndex];
            }
        }
        while (writeIndex < numbers.length) {
            numbers[writeIndex++] = 0;
        }
    }
}
