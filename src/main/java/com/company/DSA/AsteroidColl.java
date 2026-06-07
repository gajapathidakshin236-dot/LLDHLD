package com.company.DSA;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class AsteroidColl {
    public static void main(String[] args) {

    }


    public static List<Integer> AstroidCol(List<Integer> values) {
        if (values == null || values.isEmpty()) {
            return new ArrayList<>();
        }

        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i <= values.size() - 1; i++) {


            if(values.get(i) >= 0 ) {
                stack.push(values.get(i));
            }

            if(values.get(i) < 0) {
                while (!stack.isEmpty() && stack.peek() > 0
                        && stack.peek() < Math.abs(values.get(i))) {
                    stack.pop();
                }

                if (!stack.isEmpty()  && ( Math.abs(stack.peek()) - Math.abs(values.get(i)) )==0  ) {
                    stack.pop();
                }
                else if (stack.isEmpty() || stack.peek() < 0) {
                   stack.push(values.get(i));
                }
            }

        }
         return new ArrayList<>(stack);
    }
}
