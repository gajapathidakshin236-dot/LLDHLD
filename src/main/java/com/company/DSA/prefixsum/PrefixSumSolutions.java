package com.company.DSA.prefixsum;

import java.util.HashMap;
import java.util.Map;


public class PrefixSumSolutions {

    /* ============================================================
     *  HELPER — build prefix array WITHOUT mutating the input.
     *  (Original code mutated nums in place, which corrupted the
     *   total-sum calculation in pivotIndex. This version is safe.)
     * ============================================================ */
    public static int[] buildPrefix(int[] nums) {
        int[] prefix = new int[nums.length];
        prefix[0] = nums[0];
        //p[1] = p[0]+num[1]
        for (int i = 1; i < nums.length; i++) {
            prefix[i] = prefix[i - 1] + nums[i];
        }
        return prefix;
    }

    /* ============================================================
     *  #303  RANGE SUM QUERY (Immutable)
     *  Sum of elements between [left, right] in O(1).
     *
     *  FORMULA:  sum(left,right) = prefix[right] - prefix[left-1]
     *  EDGE:     left==0 → no "before" part → return prefix[right]
     * ============================================================ */
    public static int sumRange(int left, int right, int[] prefix) {
        if (left == 0) return prefix[right];
        return prefix[right] - prefix[left - 1];
    }

    /* ============================================================
     *  #724  FIND PIVOT INDEX
     *  Index where sum of left == sum of right (pivot excluded).
     *
     *  KEY:  leftSum  = prefix[i-1]      (everything BEFORE i)
     *        rightSum = total - prefix[i] (everything AFTER i)
     *        both exclude the pivot element nums[i].
     *
     *  FIX vs original: total now comes from prefix[n-1], NOT a
     *  separate loop over the (already-mutated) array.
     * ============================================================ */
    public static int pivotIndex(int[] nums) {
        int[] prefix = buildPrefix(nums);
        int total = prefix[nums.length - 1];   // last prefix IS the total

        for (int i = 0; i < nums.length; i++) {
            int leftSum  = (i == 0) ? 0 : prefix[i - 1];
            int rightSum = total - prefix[i];
            if (leftSum == rightSum) return i;
        }
        return -1;   // no pivot found
    }

    /* ============================================================
     *  #560  SUBARRAY SUM EQUALS K   ← the king pattern
     *  Count contiguous subarrays summing to exactly k.
     *
     *  map = { prefixSum : howManyTimesSeen }
     *  map.put(0,1)  → base case: empty prefix has sum 0, seen once.
     *                  This lets subarrays starting at index 0 count.
     *
     *  ORDER MATTERS:
     *   1. check (sum - k) FIRST  (look at earlier prefixes)
     *   2. THEN store current sum (for future positions)
     * ============================================================ */
    public static int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);

        int sum = 0, count = 0;
        for (int num : nums) {
            sum += num;
            int need = sum - k;

            if (map.containsKey(need)) {
                count += map.get(need);   // each earlier match = a valid subarray
            }

            map.put(sum, map.getOrDefault(sum, 0) + 1);   // ALWAYS store
        }
        return count;
    }

    /* ============================================================
     *  #974  SUBARRAY SUMS DIVISIBLE BY K
     *  Count subarrays whose sum is divisible by k.
     *
     *  INSIGHT: if two prefix sums share the SAME remainder mod k,
     *           the subarray between them is divisible by k.
     *           (prefix[j]%k == prefix[i]%k ⇒ (diff)%k == 0)
     *
     *  So we store REMAINDERS instead of raw sums.
     *
     *  NEGATIVE FIX: Java's % can return negatives (-1%5 = -1).
     *               ((sum % k) + k) % k forces a positive remainder.
     *               Works for positives too, so apply it always.
     * ============================================================ */
    public static int subarraysDivByK(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);

        int sum = 0, count = 0;
        for (int num : nums) {
            sum += num;
            int rem = ((sum % k) + k) % k;   // always-positive remainder

            if (map.containsKey(rem)) {
                count += map.get(rem);
            }

            map.put(rem, map.getOrDefault(rem, 0) + 1);
        }
        return count;
    }

    /* ============================================================
     *  #930  BINARY SUBARRAYS WITH SUM
     *  Array of 0s and 1s. Count subarrays summing to goal.
     *
     *  This IS #560 — identical code, k = goal.
     * ============================================================ */
    public static int numSubarraysWithSum(int[] nums, int goal) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);

        int sum = 0, count = 0;
        for (int num : nums) {
            sum += num;
            int need = sum - goal;

            if (map.containsKey(need)) {
                count += map.get(need);
            }

            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }
        return count;
    }

    /* ============================================================
     *  #523  CONTINUOUS SUBARRAY SUM
     *  Is there a subarray of length >= 2 that is a multiple of k?
     *  Returns boolean (just need ONE), not a count.
     *
     *  TWIST: enforce length >= 2.
     *   → store the INDEX of each remainder's FIRST appearance.
     *   → when a remainder repeats, check (i - firstIndex >= 2).
     *
     *  map.put(0,-1) base case → a prefix divisible by k at index 1
     *   gives length 1-(-1)=2, which is valid.
     *
     *  Only store FIRST occurrence (the else branch) so the gap
     *  to a future repeat is as large as possible.
     * ============================================================ */
    public static boolean checkSubarraySum(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);

        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            int rem = ((sum % k) + k) % k;   // safe remainder (handles negatives)

            if (map.containsKey(rem)) {
                if (i - map.get(rem) >= 2) {  // length must be >= 2
                    return true;
                }
            } else {
                map.put(rem, i);             // store FIRST index only
            }
        }
        return false;
    }

    /* ============================================================
     *  DEMO
     * ============================================================ */
    public static void main(String[] args) {
        // #303
        int[] prefix = buildPrefix(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        System.out.println("#303 sumRange(1,4) = " + sumRange(1, 4, prefix)); // 14
        System.out.println("#303 sumRange(0,2) = " + sumRange(0, 2, prefix)); // 6

        // #724
        System.out.println("#724 pivotIndex   = " + pivotIndex(new int[]{1, 7, 3, 6, 5, 6})); // 3

        // #560
        System.out.println("#560 subarraySum  = " + subarraySum(new int[]{1, 1, 1}, 2)); // 2

        // #974
        System.out.println("#974 divByK       = " + subarraysDivByK(new int[]{4, 5, 0, -2, -3, 1}, 5)); // 7

        // #930
        System.out.println("#930 binarySum    = " + numSubarraysWithSum(new int[]{1, 0, 1, 0, 1}, 2)); // 4

        // #523
        System.out.println("#523 contSubarray = " + checkSubarraySum(new int[]{23, 2, 4, 6, 7}, 6)); // true
    }
}
