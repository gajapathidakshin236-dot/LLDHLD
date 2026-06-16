package com.company.DSA;

/* ============================================================
 *  LeetCode #309 — Best Time to Buy and Sell Stock with Cooldown
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given an array of stock prices, find max profit with rules:
 *    - You can complete as many transactions as you want.
 *    - You cannot engage in multiple transactions simultaneously.
 *    - After you SELL, you must "cool down" for 1 day before buying again.
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
 *   1. State machine with two states per day: HOLD (own stock) vs SOLD (don't).
 *   2. hold[i] = max(hold[i-1], sold[i-2] - price[i]).
 *   3. sold[i] = max(sold[i-1], hold[i-1] + price[i]).
 *   4. Only need 3 variables: hold, sold, prevSold.
 * ============================================================ */
public class StockWithCooldown {
    public int maxProfit(int[] prices) {
        int hold = Integer.MIN_VALUE, sold = 0, prevSold = 0;
        for (int p : prices) {
            int prevHold = hold;
            hold = Math.max(hold, prevSold - p);
            prevSold = sold;
            sold = Math.max(sold, prevHold + p);
        }
        return sold;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  DP with state machine:
 *    HOLD[i]: best profit at day i ending in "own stock".
 *    SOLD[i]: best profit at day i ending in "no stock".
 *  Transitions:
 *    HOLD[i] = max(HOLD[i-1], SOLD[i-2] - price[i])   // buy today after cooldown
 *    SOLD[i] = max(SOLD[i-1], HOLD[i-1] + price[i])    // sell today
 *
 *  Why we need SOLD[i-2]:
 *    Cooldown forces a 1-day gap between sell and buy. Use the value from
 *    TWO days ago to enforce that gap.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Edge cases: single day → 0; descending prices → 0.
 *  Pattern: finite-state DP; same shape: #714 (fee), #123/#188 (k transactions).
 * ============================================================ */
