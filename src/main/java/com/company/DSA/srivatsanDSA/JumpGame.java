package com.company.DSA.srivatsanDSA;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 70
 *  Problem: Jump Game — can you reach last index given jump[i] max jump length?
 *
 *  APPROACH (from notes):
 *    Greedy. Track maxReach = 0.
 *    for i in [0, n):
 *      if i > maxReach → return false (can't even get here)
 *      maxReach = max(maxReach, i + nums[i])
 *      if maxReach >= n - 1 → return true
 *    return true
 * ============================================================ */
public class JumpGame {

    public boolean canJump(final int[] maxJumpFromIndex) {
        int furthestReachableIndex = 0;
        final int lastIndex = maxJumpFromIndex.length - 1;

        for (int currentIndex = 0; currentIndex < maxJumpFromIndex.length; currentIndex++) {
            if (currentIndex > furthestReachableIndex) {
                return false;
            }
            furthestReachableIndex = Math.max(furthestReachableIndex,
                                              currentIndex + maxJumpFromIndex[currentIndex]);
            if (furthestReachableIndex >= lastIndex) {
                return true;
            }
        }
        return true;
    }
}
