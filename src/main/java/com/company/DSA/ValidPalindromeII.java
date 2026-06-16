package com.company.DSA;

/* ============================================================
 *  LeetCode #680 — Valid Palindrome II
 * ============================================================
 *
 *  PROBLEM
 *  -------
 *  Given a string s, return true if it can be a palindrome after deleting
 *  AT MOST ONE character from it.
 *
 *  EXAMPLES
 *  --------
 *  Example 1:
 *    Input:  s = "aba"
 *    Output: true   (already palindrome, no deletion needed)
 *  Example 2:
 *    Input:  s = "abca"
 *    Output: true   (delete 'b' → "aca" or delete 'c' → "aba")
 *  Example 3:
 *    Input:  s = "abc"
 *    Output: false  (no single deletion makes it palindrome)
 *  Example 4:
 *    Input:  s = "deeee"
 *    Output: true   (delete 'd' → "eeee")
 *
 *  CONSTRAINTS
 *  -----------
 *   1 <= s.length <= 10^5
 *   s consists of lowercase English letters.
 *
 *  HINTS
 *  -----
 *   1. Two pointers from both ends. On mismatch you have exactly two choices.
 *   2. Try skipping the LEFT char OR skipping the RIGHT char and re-check.
 *   3. If either of those is a palindrome → true.
 * ============================================================ */
public class ValidPalindromeII {
    public boolean validPalindrome(String s) {
        int l = 0, r = s.length() - 1;
        while (l < r) {
            if (s.charAt(l) != s.charAt(r))
                return isPal(s, l + 1, r) || isPal(s, l, r - 1);
            l++; r--;
        }
        return true;
    }
    private boolean isPal(String s, int l, int r) {
        while (l < r) { if (s.charAt(l++) != s.charAt(r--)) return false; }
        return true;
    }
}

/* ============================================================
 *  APPROACH EXPLAINED
 * ============================================================
 *
 *  Why two-pointer with one chance:
 *    Without deletion, valid palindrome is plain two-pointer scan. With ONE
 *    allowed deletion, on the first mismatch we must decide: drop s[l] or s[r].
 *    Both choices are easy to verify in O(n) by running an inner palindrome check.
 *
 *  Step-by-step:
 *    1. l=0, r=len-1. Move while chars match.
 *    2. On mismatch:
 *         - Try skipping l: check substring s[l+1..r] is palindrome.
 *         - Try skipping r: check substring s[l..r-1] is palindrome.
 *         - Return OR of the two.
 *    3. If the outer loop finishes without mismatch → already palindrome.
 *
 *  Why not "delete each char and check"?
 *    That is O(n^2) — works but wasteful. The two-choice trick is O(n).
 *
 *  Complexity:
 *    Time:  O(n)  — outer loop O(n), inner check at most one extra O(n).
 *    Space: O(1)
 *
 *  Edge cases:
 *    - Already palindrome → never enters mismatch branch.
 *    - Single char → vacuously palindrome.
 *    - Two-char mismatch like "ab" → skipping either yields one char → true.
 *
 *  Pattern:
 *    "Two pointers + bounded retry." Extends to #1216 (k deletions) where
 *    you recursively or DP with remaining-deletions budget.
 * ============================================================ */
