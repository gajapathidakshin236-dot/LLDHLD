package com.company.DSA;

/**
 * LeetCode #227 - Basic Calculator II  (+ - * / with non-negative ints)
 * One pass; keep prev operator. For + or - push/subtract; for * or / fold into last.
 * Time: O(n)  Space: O(n)
 */
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
