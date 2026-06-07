package com.company.DSA;


import java.util.Stack;

class MinStack {
    Stack<int[]> stack; // [value, currentMin]

    public MinStack() {
        stack = new Stack<>();
    }

    public void push(int val) {
        int min = stack.isEmpty() ?  val : Math.min(val,getMin());
        stack.push(new int[]{val,min});
    }

    public void pop() {
        stack.pop();
    }

    public int top() {
        return stack.peek()[0];
    }

    public int getMin() {
        return stack.peek()[1];
    }
}