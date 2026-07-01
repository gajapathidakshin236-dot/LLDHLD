package com.company.DSA.srivatsanDSA;

import java.util.HashSet;
import java.util.Set;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 4-5
 *  Problem: Contains Duplicate
 *  Input:  [1,2,3,1]   Output: true (1 occurs twice)
 *
 *  APPROACH (from notes):
 *    Set<Integer> seen = new HashSet<>();
 *    for (int n : nums) {
 *        if (seen.contains(n)) return true;
 *        seen.add(n);
 *    }
 *    return false;
 *
 *  ALT (notes): Instead of HashSet, can use a boolean[] indexed by value
 *  if value range is small/bounded.
 * ============================================================ */
public class ContainsDuplicate {

    public boolean containsDuplicate(final int[] numbers) {
        final Set<Integer> seenValues = new HashSet<>();

        for (final int value : numbers) {
            if (!seenValues.add(value)) {
                return true;
            }
        }
        return false;
    }
}
