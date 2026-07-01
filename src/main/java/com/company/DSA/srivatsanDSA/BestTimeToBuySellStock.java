package com.company.DSA.srivatsanDSA;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 6-7
 *  Problem: Best Time to Buy and Sell Stock
 *  prices = [7,1,5,3,6,4]   Output: 5  (buy@1, sell@6)
 *
 *  APPROACH (from notes):
 *    int minVal = Integer.MAX_VALUE;
 *    int maxProfit = 0;
 *    for (int i = 0; i < prices.length; i++) {
 *        if (prices[i] < minVal) minVal = prices[i];
 *        int complement = prices[i] - minVal;
 *        if (complement > maxProfit) maxProfit = complement;
 *    }
 *    return maxProfit;
 *
 *  Track running minimum price; check today's price minus that minimum.
 * ============================================================ */
public class BestTimeToBuySellStock {

    public int maxProfit(final int[] prices) {
        int minimumPriceSeen = Integer.MAX_VALUE;
        int bestProfitSoFar  = 0;

        for (final int currentPrice : prices) {
            if (currentPrice < minimumPriceSeen) {
                minimumPriceSeen = currentPrice;
            }
            final int profitIfSellingToday = currentPrice - minimumPriceSeen;
            if (profitIfSellingToday > bestProfitSoFar) {
                bestProfitSoFar = profitIfSellingToday;
            }
        }
        return bestProfitSoFar;
    }
}
