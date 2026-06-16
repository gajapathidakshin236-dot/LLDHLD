package com.company.DSA;

import java.util.*;

/**
 * LeetCode #525 - Contiguous Array
 * Map 0 -> -1. Track running sum; first time we see a sum re-occur, the in-between segment sums 0.
 * Time: O(n)  Space: O(n)
 */
public class ContiguousArray {
    public int findMaxLength(int[] nums) {
        Map<Integer,Integer> first = new HashMap<>();
        first.put(0, -1);
        int sum = 0, best = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += (nums[i] == 0 ? -1 : 1);
            if (first.containsKey(sum)) best = Math.max(best, i - first.get(sum));
            else first.put(sum, i);
        }
        return best;
    }
}
