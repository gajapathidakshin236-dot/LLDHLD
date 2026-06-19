package com.company.DSA.stack;

import java.util.Stack;

public class dailyTemp {

    public int[] dailyTemperatures(int[] temperatures) {

        int n = temperatures.length;
        int[] result = new int[n];
        Stack<Integer> stack = new Stack<>();

        //[73, 74, 75, 71, 69, 72, 76, 73]

        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                int top = stack.pop(); //0
                result[top] = i-top;
            }

            stack.push(i);
        }
     return result;
    }
}
