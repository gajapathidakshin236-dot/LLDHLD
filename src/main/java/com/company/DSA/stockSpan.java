package com.company.DSA;

import java.util.Stack;

public class stockSpan {


        private Stack<int[]> stack = new Stack<>();   // [price, span]

        public int next(int price) {
            int span = 1;
            while (!stack.isEmpty() && stack.peek()[0] <= price) {
                span += stack.pop()[1];
            }
            stack.push(new int[]{price, span});
            return span;
        }
    }

