package com.company.DSA.srivatsanDSA;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 12
 *  Problem: Merge Sorted Array (in place into nums1)
 *  nums1 = [1,2,3,0,0,0], m=3;  nums2 = [2,5,6], n=3
 *  Output: nums1 = [1,2,2,3,5,6]
 *
 *  APPROACH (from notes):
 *    Walk from the BACK (compare from end) to avoid overwriting.
 *    int i = m-1, j = n-1, write = m+n-1;
 *    while (i >= 0 && j >= 0)
 *        if (nums1[i] > nums2[j]) nums1[write--] = nums1[i--];
 *        else                     nums1[write--] = nums2[j--];
 *    while (j >= 0) nums1[write--] = nums2[j--];
 * ============================================================ */
public class MergeSortedArray {

    public void merge(final int[] firstArray, final int firstLength,
                      final int[] secondArray, final int secondLength) {
        int firstCursor  = firstLength  - 1;
        int secondCursor = secondLength - 1;
        int writeCursor  = firstLength + secondLength - 1;

        while (firstCursor >= 0 && secondCursor >= 0) {
            if (firstArray[firstCursor] > secondArray[secondCursor]) {
                firstArray[writeCursor--] = firstArray[firstCursor--];
            } else {
                firstArray[writeCursor--] = secondArray[secondCursor--];
            }
        }
        while (secondCursor >= 0) {
            firstArray[writeCursor--] = secondArray[secondCursor--];
        }
    }
}
