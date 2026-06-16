package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #150 — Evaluate Reverse Polish Notation
 * ============================================================
 *
 *  PROBLEM
 *  -------
 *  You are given an array of strings tokens representing an arithmetic expression
 *  in Reverse Polish Notation. Evaluate the expression and return the integer
 *  value. Operators are +, -, *, /. Integer division truncates toward zero.
 *
 *  EXAMPLES
 *  --------
 *  Example 1:
 *    Input:  ["2","1","+","3","*"]
 *    Output: 9      // (2+1)*3
 *  Example 2:
 *    Input:  ["4","13","5","/","+"]
 *    Output: 6      // 4 + (13/5) = 4 + 2
 *  Example 3:
 *    Input:  ["10","6","9","3","+","-11","*","/","*","17","+","5","+"]
 *    Output: 22
 *  Example 4:
 *    Input:  ["3","-2","-"]
 *    Output: 5      // 3 - (-2)
 *
 *  CONSTRAINTS
 *  -----------
 *   1 <= tokens.length <= 10^4
 *   tokens[i] is "+","-","*","/", or an integer in [-200, 200].
 *
 *  HINTS
 *  -----
 *   1. RPN is built to be evaluated with a stack.
 *   2. Number → push. Operator → pop two, apply, push result.
 *   3. Order matters for - and /: second-popped is the LEFT operand.
 * ============================================================ */
public class EvaluateRPN {
    public int evalRPN(String[] tokens) {
        Deque<Integer> st = new ArrayDeque<>();
        for (String t : tokens) {
            switch (t) {
                case "+": case "-": case "*": case "/":
                    int b = st.pop(), a = st.pop();
                    switch (t) {
                        case "+": st.push(a + b); break;
                        case "-": st.push(a - b); break;
                        case "*": st.push(a * b); break;
                        case "/": st.push(a / b); break;
                    }
                    break;
                default:
                    st.push(Integer.parseInt(t));
            }
        }
        return st.pop();
    }
}

/* ============================================================
 *  APPROACH EXPLAINED
 * ============================================================
 *
 *  Why RPN + stack are made for each other:
 *    In RPN, operands always come before the operator. So a stack naturally
 *    holds pending operands, and an operator pops exactly the two it needs.
 *
 *  Operand order trap:
 *    "a b -" means a - b. Since we push a first then b, b is the TOP.
 *    Pop order: b = pop(); a = pop(); result = a OP b.
 *    Forgetting this flips the sign of subtraction/division.
 *
 *  Step-by-step:
 *    For each token:
 *      - If it's an operator: pop b, pop a, push (a OP b).
 *      - Else: parse integer and push.
 *    Final stack has exactly one element — the answer.
 *
 *  Java division truncates toward zero already:
 *    -7 / 2 == -3 in Java (matches the problem spec).
 *
 *  Complexity:
 *    Time:  O(n)
 *    Space: O(n) for the stack.
 *
 *  Edge cases:
 *    - Single number token ["5"] → push and return 5.
 *    - Negative numbers like "-3" → Integer.parseInt handles the minus.
 *    - Division by zero — problem guarantees this won't happen.
 *
 *  Pattern:
 *    "Stack-based expression evaluation." Foundation for calculators,
 *    infix-to-postfix conversion, and Basic Calculator (#224, #227).
 * ============================================================ */
