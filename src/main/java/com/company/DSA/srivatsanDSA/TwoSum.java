package com.company.DSA.srivatsanDSA;

import java.util.HashMap;
import java.util.Map;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 2-3
 *  Problem: Two Sum
 *  Sum of any 2 nos in array should be target.
 *  Input:  [2,7,11,15], target = 9
 *  Output: [0,1]
 *
 *  APPROACH (from notes):
 *    Solve using HashMap and the difference value.
 *    for each i: complement = target - nums[i]
 *                if map.containsKey(complement) → return [map.get(complement), i]
 *                else → map.put(nums[i], i)
 *
 *  SIDE NOTES (from page 3):
 *   - If the array is SORTED, no need for hash map → use 2 pointers (left & right).
 *   - If ALL solutions asked (not just one), sort + 2 pointers. When sum == target →
 *     add pair → move BOTH pointers.
 * ============================================================ */
public class TwoSum {

    public int[] twoSum(final int[] numbers, final int target) {
        final Map<Integer, Integer> indexByValue = new HashMap<>();

        for (int index = 0; index < numbers.length; index++) {
            final int complement = target - numbers[index];

            if (indexByValue.containsKey(complement)) {
                return new int[] { indexByValue.get(complement), index };
            }
            indexByValue.put(numbers[index], index);
        }
        return new int[] { -1, -1 };
    }
}
