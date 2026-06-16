package com.company.DSA;

import java.util.*;

/**
 * LeetCode #1496 - Path Crossing
 * Track visited coordinates in a Set.
 * Time: O(n)  Space: O(n)
 */
public class PathCrossing {
    public boolean isPathCrossing(String path) {
        Set<Long> seen = new HashSet<>();
        int x = 0, y = 0;
        seen.add(0L);
        for (char c : path.toCharArray()) {
            if (c == 'N') y++;
            else if (c == 'S') y--;
            else if (c == 'E') x++;
            else x--;
            long key = ((long) x << 32) | (y & 0xffffffffL);
            if (!seen.add(key)) return true;
        }
        return false;
    }
}
