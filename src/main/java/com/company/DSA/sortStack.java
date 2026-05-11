package com.company.DSA;

import java.util.Stack;

public class sortStack {

   public static void sort(Stack<Integer> stack) {
       Integer el=null;
       if (!stack.isEmpty()) {
           el=stack.pop();
           sort(stack); // 2  is  there no
           insert(stack,el); // 2 <3
       }

   }
   public static void insert(Stack<Integer> stack,int el) {
       if(stack.isEmpty()) {
           stack.push(el);
           return;
       } else if (stack.peek()<=el) {  // 2<3  1
              stack.push(el);
           return;
       }

       if (stack.peek()>el) {
           int k = stack.pop();
           insert(stack,el);
           stack.push(k);
       }

   }
    public static void main(String[] args) {

        Stack<Integer> stack = new Stack<>();
        stack.push(4);
        stack.push(1);
        stack.push(3);
        stack.push(2);

        sort(stack);

        // Print the sorted stack
        System.out.print("Sorted stack (descending order): ");
        while (!stack.isEmpty()) {
            System.out.print(stack.pop() + " ");
        }
    }
}
