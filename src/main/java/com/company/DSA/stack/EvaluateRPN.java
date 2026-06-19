package com.company.DSA.stack;

import java.util.*;
import java.util.function.IntBinaryOperator;

/* ============================================================
 *  LeetCode #150 — Evaluate Reverse Polish Notation
 * ============================================================
 *  PROBLEM
 *  -------
 *  Evaluate RPN with +, -, *, /. Integer division truncates toward zero.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: ["2","1","+","3","*"] → 9
 *  Ex2: ["4","13","5","/","+"] → 6
 *  Ex3: ["3","-2","-"] → 5
 *
 *  CONSTRAINTS:  1 <= n <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. Push numbers; on op pop two and push the result.
 *   2. Order matters: secondPop = right operand.
 * ============================================================ */
public class EvaluateRPN {

    private static final Map<String, IntBinaryOperator> OPERATORS = new HashMap<>();
    static {
        OPERATORS.put("+", Integer::sum);
        OPERATORS.put("-", (left, right) -> left - right);
        OPERATORS.put("*", (left, right) -> left * right);
        OPERATORS.put("/", (left, right) -> left / right);
    }

    public int evalRPN(final String[] tokens) {
        final Deque<Integer> operandStack = new ArrayDeque<>();

        for (final String token : tokens) {
            final IntBinaryOperator operator = OPERATORS.get(token);

            if (operator != null) {
                final int rightOperand = operandStack.pop();
                final int leftOperand  = operandStack.pop();
                operandStack.push(operator.applyAsInt(leftOperand, rightOperand));
            } else {
                operandStack.push(Integer.parseInt(token));
            }
        }
        return operandStack.pop();
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Stack of operands. Operator pops 2 (right then left) and pushes result.
 *  Operator table lets us avoid a switch.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: stack-based expression evaluation.
 * ============================================================ */
