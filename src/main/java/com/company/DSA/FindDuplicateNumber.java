package com.company.DSA;

/* ============================================================
 *  LeetCode #287 — Find the Duplicate Number
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given nums of length n+1 containing values in [1..n], exactly one value is
 *  repeated (possibly more than twice). Find the duplicate. Don't modify the
 *  array. Use O(1) extra space and O(n) time.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,3,4,2,2] → 2
 *  Ex2: [3,1,3,4,2] → 3
 *  Ex3: [1,1] → 1
 *  Ex4: [1,1,2] → 1
 *
 *  CONSTRAINTS:  1 <= n <= 10^5;  1 <= val <= n; exactly one duplicate.
 *
 *  HINTS
 *  -----
 *   1. Treat nums as a linked list: i → nums[i]. Duplicate makes a CYCLE.
 *   2. Use Floyd's cycle detection — phase 1 find meeting; phase 2 find entry.
 *   3. The cycle entrance is the duplicate value.
 * ============================================================ */
public class FindDuplicateNumber {
    public int findDuplicate(int[] nums) {
        int s = nums[0], f = nums[0];
        do { s = nums[s]; f = nums[nums[f]]; } while (s != f);
        int p = nums[0];
        while (p != s) { p = nums[p]; s = nums[s]; }
        return p;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Model nums as a function f(i) = nums[i]. With values in [1..n] and length
 *  n+1, multiple indices map to the same value (the duplicate). Following f
 *  from index 0 yields a sequence that must enter a CYCLE; the entrance is
 *  the duplicate value.
 *
 *  Phase 1: slow/fast meet inside the cycle.
 *  Phase 2: reset slow to head; step both at 1 → they meet at cycle entry.
 *
 *  Why we can't modify the array:
 *    Tags like "index marking" are off-limits per problem constraints.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: Floyd's cycle detection generalized to non-list structures.
 * ============================================================ */
