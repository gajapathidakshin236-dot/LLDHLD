package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #1590 — Make Sum Divisible by P
 * ============================================================
 *  PROBLEM
 *  -------
 *  Remove the SHORTEST subarray (possibly empty) so that the remaining sum is
 *  divisible by p. Return length of removed subarray (cannot remove all).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[3,1,4,2], p=6 → 1   (remove [4])
 *  Ex2: nums=[6,3,5,2], p=9 → 2
 *  Ex3: nums=[1,2,3], p=3 → 0     (already divisible)
 *  Ex4: nums=[1,2,3], p=7 → -1    (cannot remove all)
 *
 *  CONSTRAINTS:  1 <= n <= 10^5; 1 <= val <= 10^9; 1 <= p <= 10^9.
 *
 *  HINTS
 *  -----
 *   1. Let r = totalSum % p. If 0 → 0. We need shortest contiguous subarray with sum % p == r.
 *   2. Prefix sum modulo. Hashmap: (prefix - r) mod p → last index.
 *   3. Track min window length.
 * ============================================================ */
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

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Want to remove the SHORTEST contiguous subarray (l..r) whose sum mod p
 *  equals target = totalSum mod p. After removal, remaining sum is 0 mod p.
 *
 *  Prefix mod technique:
 *    Let pre[i] = (sum of nums[0..i-1]) mod p.
 *    Subarray (l..r) sum % p = (pre[r+1] - pre[l] + p) % p.
 *    We want this to equal target, i.e. pre[l] == (pre[r+1] - target + p) % p.
 *  Maintain a hashmap of latest index for each prefix mod; lookup `want`.
 *
 *  Track LATEST (not first):
 *    To minimize window length, take the LATEST matching previous index.
 *
 *  Initial last.put(0, -1):
 *    Empty prefix at index -1 lets us shrink to subarrays starting at 0.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: prefix mod hashmap. Cousin of #560, #974.
 * ============================================================ */
