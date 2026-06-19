package com.company.DSA.strings;

/* ============================================================
 *  LeetCode #38 — Count and Say
 * ============================================================
 *  PROBLEM
 *  -------
 *  countAndSay(1)="1"; subsequent values are RLE descriptions of previous.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: n=1 → "1"
 *  Ex2: n=2 → "11"
 *  Ex3: n=3 → "21"
 *  Ex4: n=4 → "1211"
 *  Ex5: n=5 → "111221"
 *  Ex6: n=6 → "312211"
 *
 *  CONSTRAINTS:  1 <= n <= 30.
 *
 *  HINTS
 *  -----
 *   1. Iteratively run-length encode the previous string.
 * ============================================================ */
public class CountAndSay {

    public String countAndSay(final int term) {
        String currentTerm = "1";
        for (int iteration = 2; iteration <= term; iteration++) {
            currentTerm = describe(currentTerm);
        }
        return currentTerm;
    }

    private String describe(final String previousTerm) {
        final StringBuilder description = new StringBuilder();
        int runStart = 0;

        while (runStart < previousTerm.length()) {
            int runEnd = runStart;
            while (runEnd < previousTerm.length()
                    && previousTerm.charAt(runEnd) == previousTerm.charAt(runStart)) {
                runEnd++;
            }
            description.append(runEnd - runStart)
                       .append(previousTerm.charAt(runStart));
            runStart = runEnd;
        }
        return description.toString();
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Repeated run-length encoding. Each pass describes the previous string.
 *
 *  Complexity: Time O(2^n) worst (string roughly doubles), Space O(2^n).
 *  Pattern: classic RLE; same skill as #443 String Compression.
 * ============================================================ */
