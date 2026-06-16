package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #20 — Valid Parentheses
 * ============================================================
 *
 *  PROBLEM
 *  -------
 *  Given a string s containing just the characters '(', ')', '{', '}', '[' and ']',
 *  determine if the input string is valid. A string is valid if:
 *    1. Open brackets must be closed by the same type of bracket.
 *    2. Open brackets must be closed in the correct order.
 *    3. Every close bracket has a corresponding open of the same type.
 *
 *  EXAMPLES
 *  --------
 *  Example 1: s = "()"        → true
 *  Example 2: s = "()[]{}"    → true
 *  Example 3: s = "(]"        → false
 *  Example 4: s = "([)]"      → false  (interleaved, not nested)
 *  Example 5: s = "{[]}"      → true
 *  Example 6: s = ""          → true   (vacuously valid)
 *
 *  CONSTRAINTS
 *  -----------
 *   1 <= s.length <= 10^4
 *   s consists only of brackets.
 *
 *  HINTS
 *  -----
 *   1. Brackets have LIFO behavior — last opened must be first closed → use a stack.
 *   2. On open, push the EXPECTED closing bracket.
 *   3. On close, the top of the stack must equal the current char.
 *   4. End: stack must be empty.
 * ============================================================ */
public class ValidParentheses {
    public boolean isValid(String s) {
        Deque<Character> st = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == '(') st.push(')');
            else if (c == '[') st.push(']');
            else if (c == '{') st.push('}');
            else if (st.isEmpty() || st.pop() != c) return false;
        }
        return st.isEmpty();
    }
}

/* ============================================================
 *  APPROACH EXPLAINED
 * ============================================================
 *
 *  Why a stack:
 *    Matching brackets is LIFO: the most recently opened must close first.
 *    A stack is the perfect data structure.
 *
 *  Push-expected-close trick:
 *    Instead of pushing the open bracket and translating on close, we push the
 *    char we EXPECT next time we see a closer. Then on a closer we just do an
 *    equality check — no mapping needed.
 *
 *  Step-by-step:
 *    For each char c:
 *      if c is '(' / '[' / '{' → push ')'/'['/'{' resp.
 *      else (c is a closer) → if stack empty OR top != c → false.
 *    After loop → stack empty == true.
 *
 *  Why ArrayDeque over Stack class:
 *    java.util.Stack is legacy + synchronized + slow. ArrayDeque is the
 *    modern recommended LIFO/FIFO.
 *
 *  Complexity:
 *    Time:  O(n)
 *    Space: O(n) — worst case all opens.
 *
 *  Edge cases:
 *    - Empty string → no chars processed → returns true.
 *    - Only closers like "]]]" → first char triggers empty-stack check → false.
 *    - Only opens like "[[[" → stack non-empty at end → false.
 *
 *  Pattern:
 *    "Stack matching." Same pattern: #921 Min Add to Make Parens Valid,
 *    #1249 Minimum Remove, #856 Score of Parentheses, expression evaluators.
 * ============================================================ */
