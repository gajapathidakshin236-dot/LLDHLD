package com.company.DSA.hashing;

import java.util.*;

/* ============================================================
 *  LeetCode #1496 — Path Crossing
 * ============================================================
 *  PROBLEM
 *  -------
 *  Walking N/S/E/W one unit per char, does the path revisit any point?
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "NES"   → false
 *  Ex2: "NESWW" → true
 *  Ex3: "NSWE"  → true (back to origin)
 *
 *  CONSTRAINTS:  1 <= path.length <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. Visited (x,y) HashSet. Pack into long for speed.
 * ============================================================ */
public class PathCrossing {

    public boolean isPathCrossing(final String path) {
        final Set<Long> visitedPoints = new HashSet<>();
        int currentX = 0;
        int currentY = 0;
        visitedPoints.add(packCoordinates(currentX, currentY));

        for (final char step : path.toCharArray()) {
            switch (step) {
                case 'N': currentY++; break;
                case 'S': currentY--; break;
                case 'E': currentX++; break;
                case 'W': currentX--; break;
                default: throw new IllegalArgumentException();
            }
            if (!visitedPoints.add(packCoordinates(currentX, currentY))) {
                return true;
            }
        }
        return false;
    }

    private long packCoordinates(final int x, final int y) {
        return ((long) x << 32) | (y & 0xffffffffL);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Track visited points. Pack (x, y) into long. add() returns false → revisit.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: state-set walk simulation.
 * ============================================================ */
