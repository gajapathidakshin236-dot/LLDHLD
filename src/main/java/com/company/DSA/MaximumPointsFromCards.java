package com.company.DSA;
import java.util.*;

public class MaximumPointsFromCards {

    public static int maxScore(int[] cardPoints, int k) {
        int n = cardPoints.length;

        int leftSum = 0;
        int rightSum = 0;
        int maxSum = 0;

        // Take first k cards from left
        for (int i = 0; i < k; i++) {
            leftSum += cardPoints[i];
        }

        maxSum = leftSum;

        int rightIndex = n - 1;

        // Remove from left and add from right
        for (int i = k - 1; i >= 0; i--) {
            leftSum -= cardPoints[i];
            rightSum += cardPoints[rightIndex];
            rightIndex--;

            maxSum = Math.max(maxSum, leftSum + rightSum);
        }

        return maxSum;
    }

    public static void main(String[] args) {
        int[] arr = {6, 2, 3, 4, 7, 2, 1, 7, 1};
        int k = 4;

        System.out.println(maxScore(arr, k));
    }
}