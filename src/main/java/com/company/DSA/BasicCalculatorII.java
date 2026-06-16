package com.company.DSA;

/* ============================================================
 *  LeetCode #227 — Basic Calculator II
 * ============================================================
 *  PROBLEM
 *  -------
 *  Implement a calculator for a string expression with +, -, *, /, non-negative
 *  integers and spaces. Honor precedence: * and / before + and -. Integer
 *  division truncates toward zero. No parentheses.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "3+2*2"     → 7
 *  Ex2: " 3/2 "     → 1
 *  Ex3: " 3+5 / 2 " → 5
 *  Ex4: "14-3/2"    → 13   (14 - 1)
 *
 *  CONSTRAINTS:  1 <= s.length <= 3*10^5; digits and operators; expression valid.
 *
 *  HINTS
 *  -----
 *   1. Walk left to right with `op` = previous operator and `last` = previous term.
 *   2. * / fold into `last`; + / - finalize `last` into `total` and start fresh.
 *   3. Don't forget to finalize at the end of string.
 * ============================================================ */
public class BasicCalculatorII {
    public int calculate(String s) {
        int n = s.length(), num = 0, last = 0, total = 0;
        char op = '+';
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) num = num * 10 + (c - '0');
            if ((!Character.isDigit(c) && c != ' ') || i == n - 1) {
                switch (op) {
                    case '+': total += last; last = num; break;
                    case '-': total += last; last = -num; break;
                    case '*': last *= num; break;
                    case '/': last /= num; break;
                }
                op = c; num = 0;
            }
        }
        return total + last;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Single pass, O(1) space. The "last term" idea:
 *    We DELAY committing the current term to `total` until we see a + or -.
 *    Until then, * or / fold into `last` (since they have higher precedence).
 *
 *  Operator timing:
 *    `op` holds the operator we read BEFORE the current number. When we hit
 *    the next operator (or end-of-string), we apply `op` to combine `last`
 *    and `num`.
 *
 *  Why we treat end-of-string as a trigger:
 *    The final number doesn't have a trailing operator. The `i == n-1` clause
 *    forces the final flush.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Edge cases: leading/trailing whitespace; division floor toward zero (Java default).
 *  Pattern: precedence with one operand buffer. Used in Basic Calculator family.
 * ============================================================ */
