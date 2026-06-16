package com.company.DSA;

import java.util.*;

/**
 * LeetCode #560 - Subarray Sum Equals K
 * Prefix sums + hashmap of counts. Number of i where prefix[r] - prefix[l] == k.
 * Time: O(n)  Space: O(n)
 */
public class SubarraySumEqualsK {
    public int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        freq.put(0, 1);
        int sum = 0, count = 0;
        for (int x : nums) {
            sum += x;
            count += freq.getOrDefault(sum - k, 0);
            freq.merge(sum, 1, Integer::sum);
        }
        return count;
    }
}
