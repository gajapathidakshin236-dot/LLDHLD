package com.company.DSA.dp;

/* ============================================================
 *  LeetCode #309 — Best Time to Buy and Sell Stock with Cooldown
 * ============================================================
 *  PROBLEM
 *  -------
 *  Max profit with unlimited transactions; one-day cooldown after each sell.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,2,3,0,2]    → 3   (buy@1, sell@2, cool, buy@0, sell@2)
 *  Ex2: [1]            → 0
 *  Ex3: [1,2,4]        → 3
 *  Ex4: [6,1,3,2,4,7]  → 6
 *
 *  CONSTRAINTS:  1 <= n <= 5000;  0 <= prices[i] <= 1000.
 *
 *  HINTS
 *  -----
 *   1. State machine: hold[i] = max(hold[i-1], sold[i-2] - p).
 *      sold[i] = max(sold[i-1], hold[i-1] + p).
 * ============================================================ */
public class StockWithCooldown {

    public int maxProfit(final int[] prices) {
        int profitIfHoldingToday   = Integer.MIN_VALUE;
        int profitIfSoldToday      = 0;
        int profitIfSoldYesterday  = 0;

        for (final int currentPrice : prices) {
            final int previousHolding = profitIfHoldingToday;
            profitIfHoldingToday = Math.max(profitIfHoldingToday, profitIfSoldYesterday - currentPrice);

            profitIfSoldYesterday = profitIfSoldToday;
            profitIfSoldToday     = Math.max(profitIfSoldToday, previousHolding + currentPrice);
        }
        return profitIfSoldToday;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  State machine DP with HOLD and SOLD. Cooldown enforced by using SOLD[i-2]
 *  when transitioning into HOLD[i].
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: finite-state DP.
 * ============================================================ */
