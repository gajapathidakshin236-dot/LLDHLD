package com.company.DSA.strings;

/* ============================================================
 *  LeetCode #925 — Long Pressed Name
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return true if typed could be the long-pressed version of name.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: name="alex",   typed="aaleex"   → true
 *  Ex2: name="saeed",  typed="ssaaedd"  → false
 *  Ex3: name="leelee", typed="lleeelee" → true
 *  Ex4: name="laiden", typed="laiden"   → true
 *
 *  CONSTRAINTS:  1 <= len <= 1000.
 *
 *  HINTS
 *  -----
 *   1. Two pointers. If match → advance both. If typed-repeat → advance typed only.
 * ============================================================ */
public class LongPressedName {

    public boolean isLongPressedName(final String name, final String typed) {
        int nameCursor  = 0;
        int typedCursor = 0;

        while (typedCursor < typed.length()) {
            final boolean charactersMatch = nameCursor < name.length()
                    && name.charAt(nameCursor) == typed.charAt(typedCursor);
            final boolean isRepeatedKey = typedCursor > 0
                    && typed.charAt(typedCursor) == typed.charAt(typedCursor - 1);

            if (charactersMatch) {
                nameCursor++;
                typedCursor++;
            } else if (isRepeatedKey) {
                typedCursor++;
            } else {
                return false;
            }
        }
        return nameCursor == name.length();
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Two-pointer match. Either characters match (advance both), typed has an
 *  extra repeat (advance typed only), else fail.
 *
 *  Complexity: Time O(|name| + |typed|), Space O(1).
 *  Pattern: align-with-repeats two-pointer.
 * ============================================================ */
