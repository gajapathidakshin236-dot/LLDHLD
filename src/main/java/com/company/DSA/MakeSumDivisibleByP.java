package com.company.DSA;

import java.util.*;

/**
 * LeetCode #1590 - Make Sum Divisible by P
 * Remove shortest subarray whose sum mod p == total mod p.
 * Use hashmap of last-seen prefix remainder; track minimum length window.
 * Time: O(n)  Space: O(n)
 */
public class MakeSumDivisibleByP {
    public int minSubarray(int[] nums, int p) {
        int target = 0;
        for (int v : nums) target = (target + v) % p;
        if (target == 0) return 0;
        Map<Integer, Integer> last = new HashMap<>();
        last.put(0, -1);
        int sum = 0, best = nums.length;
        for (int i = 0; i < nums.length; i++) {
            sum = (sum + nums[i]) % p;
            int want = (sum - target + p) % p;
            if (last.containsKey(want)) best = Math.min(best, i - last.get(want));
            last.put(sum, i);
        }
        return best == nums.length ? -1 : best;
    }
}
