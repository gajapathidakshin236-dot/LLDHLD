package com.company.DSA;

/**
 * LeetCode #309 - Best Time to Buy and Sell Stock with Cooldown
 * State machine: hold[i] = max(hold[i-1], sold[i-2] - price)
 *                sold[i] = max(sold[i-1], hold[i-1] + price)
 * Time: O(n)  Space: O(1)
 */
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
