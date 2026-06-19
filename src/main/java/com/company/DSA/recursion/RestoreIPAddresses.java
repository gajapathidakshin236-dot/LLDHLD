package com.company.DSA.recursion;

import java.util.*;

/* ============================================================
 *  LeetCode #93 — Restore IP Addresses
 * ============================================================
 *  PROBLEM
 *  -------
 *  All valid IPv4 addresses by inserting 3 dots into the digit string.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "25525511135" → ["255.255.11.135","255.255.111.35"]
 *  Ex2: "0000"        → ["0.0.0.0"]
 *  Ex3: "1111"        → ["1.1.1.1"]
 *
 *  CONSTRAINTS:  1 <= s.length <= 20.
 *
 *  HINTS
 *  -----
 *   1. Backtrack with segments of length 1-3. Reject invalid segments.
 * ============================================================ */
public class RestoreIPAddresses {

    private static final int MAX_SEGMENT_VALUE = 255;
    private static final int IP_SEGMENT_COUNT  = 4;

    public List<String> restoreIpAddresses(final String digitString) {
        final List<String> results = new ArrayList<>();
        backtrack(digitString, 0, 0, new StringBuilder(), results);
        return results;
    }

    private void backtrack(final String digitString,
                           final int charIndex,
                           final int segmentsPlaced,
                           final StringBuilder currentIp,
                           final List<String> results) {
        if (segmentsPlaced == IP_SEGMENT_COUNT) {
            if (charIndex == digitString.length()) {
                results.add(currentIp.substring(0, currentIp.length() - 1));
            }
            return;
        }

        for (int segmentLength = 1; segmentLength <= 3
                && charIndex + segmentLength <= digitString.length(); segmentLength++) {
            final String segment = digitString.substring(charIndex, charIndex + segmentLength);
            if (!isValidSegment(segment)) {
                continue;
            }
            final int previousLength = currentIp.length();
            currentIp.append(segment).append('.');
            backtrack(digitString, charIndex + segmentLength,
                      segmentsPlaced + 1, currentIp, results);
            currentIp.setLength(previousLength);
        }
    }

    private boolean isValidSegment(final String segment) {
        final boolean hasLeadingZero = segment.length() > 1 && segment.charAt(0) == '0';
        if (hasLeadingZero) {
            return false;
        }
        return Integer.parseInt(segment) <= MAX_SEGMENT_VALUE;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Bounded backtracking with depth 4 and branching 1-3. Reject invalid segments
 *  early. Trailing dot stripped on success.
 *
 *  Complexity: Time O(1) bounded; <= 3^4 = 81 branches.
 *  Pattern: small fixed-depth backtracking with parsing.
 * ============================================================ */
