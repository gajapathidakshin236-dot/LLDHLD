package com.company.DSA.dp;

import java.util.ArrayList;
import java.util.List;

public class stockproblem {

    public static int main() {
        List<Integer> items = new ArrayList<>();
        int minPrice = Integer.MAX_VALUE;
        int maxprofit = 0;

        for (int i : items) {
            if (i < minPrice) {
                minPrice = i;
                maxprofit = Math.max(i - minPrice, maxprofit);
            } else {
                maxprofit = Math.max(i - minPrice, maxprofit);
            }
        }
        return maxprofit;
    }
}
