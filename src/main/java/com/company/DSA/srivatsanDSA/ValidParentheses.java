package com.company.DSA.srivatsanDSA;

import java.util.ArrayDeque;
import java.util.Deque;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 44-45
 *  Problem: Valid Parentheses — only () [] {} brackets.
 *
 *  APPROACH (from notes):
 *    Stack of EXPECTED closing brackets.
 *    On open '(' '[' '{' → push matching ')' ']' '}'.
 *    On close → if stack empty OR stack.pop() != char → false.
 *    At end → return stack.isEmpty().
 * ============================================================ */
public class ValidParentheses {

    public boolean isValid(final String brackets) {
        final Deque<Character> expectedClosers = new ArrayDeque<>();

        for (final char character : brackets.toCharArray()) {
            switch (character) {
                case '(': expectedClosers.push(')'); break;
                case '[': expectedClosers.push(']'); break;
                case '{': expectedClosers.push('}'); break;
                default:
                    if (expectedClosers.isEmpty() || expectedClosers.pop() != character) {
                        return false;
                    }
            }
        }
        return expectedClosers.isEmpty();
    }
}
