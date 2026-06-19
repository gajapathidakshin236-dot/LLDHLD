package com.company.DSA.stack;

/* ============================================================
 *  LeetCode #227 — Basic Calculator II
 * ============================================================
 *  PROBLEM
 *  -------
 *  Evaluate +, -, *, / expression with non-negative ints and spaces.
 *  Honor * / over + -.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "3+2*2"     → 7
 *  Ex2: " 3/2 "     → 1
 *  Ex3: " 3+5 / 2 " → 5
 *  Ex4: "14-3/2"    → 13
 *
 *  CONSTRAINTS:  1 <= s.length <= 3*10^5.
 *
 *  HINTS
 *  -----
 *   1. Track previousOperator + lastTerm; commit on + / -.
 * ============================================================ */
public class BasicCalculatorII {

    public int calculate(final String expression) {
        int currentNumber  = 0;
        int lastTerm       = 0;
        int totalSoFar     = 0;
        char previousOp    = '+';
        final int length   = expression.length();

        for (int index = 0; index < length; index++) {
            final char character = expression.charAt(index);

            if (Character.isDigit(character)) {
                currentNumber = currentNumber * 10 + (character - '0');
            }

            final boolean isOperatorOrEnd = (!Character.isDigit(character) && character != ' ')
                    || index == length - 1;

            if (isOperatorOrEnd) {
                switch (previousOp) {
                    case '+': totalSoFar += lastTerm; lastTerm  =  currentNumber; break;
                    case '-': totalSoFar += lastTerm; lastTerm  = -currentNumber; break;
                    case '*': lastTerm   *=  currentNumber;                       break;
                    case '/': lastTerm   /=  currentNumber;                       break;
                    default: throw new IllegalArgumentException();
                }
                previousOp     = character;
                currentNumber  = 0;
            }
        }
        return totalSoFar + lastTerm;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Single pass; delay committing lastTerm to total until +/-, since * / take
 *  precedence and fold into lastTerm.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: precedence with one operand buffer.
 * ============================================================ */
