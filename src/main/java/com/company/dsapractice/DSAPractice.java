package com.company.dsapractice;
import java.util.*;
import java.util.function.Supplier;

/** Shared shape for the array-backed linked list (P1). Both your version and the key implement it. */
interface IList {
    void addLast(int x);
    void addFirst(int x);
    int get(int index);
    void removeAt(int index);
    List<Integer> toList();
}

/**
 * DSA PRACTICE — 5 classic interview problems.
 * Fill in each stub; the harness in main() grades you against DSASolutions (a verified key).
 * Run: java com.company.dsapractice.DSAPractice
 * See DSA_NOTES.md for the approach of each. Peek at DSASolutions only after you try.
 */
public class DSAPractice {

    // ============ P1: Linked list backed by ARRAYS (no java.util.LinkedList) ============
    // Idea: parallel arrays val[] (data) and next[] (index of next node, -1 = end),
    // an int head, and a free-list of reusable slots so removeAt can recycle space.
    static class ArrayLinkedList implements IList {
        // TODO: declare arrays + head + size + capacity + freeHead, grow() when full.
        public void addLast(int x)   { /* TODO */ }
        public void addFirst(int x)  { /* TODO */ }
        public int  get(int index)   { return -1; /* TODO */ }
        public void removeAt(int index) { /* TODO */ }
        public List<Integer> toList(){ return new ArrayList<>(); /* TODO */ }
    }

    // ============ P2: Rotten oranges (multi-source BFS) ============
    // grid values: 0 empty, 1 fresh, 2 rotten. Each minute, every rotten orange rots its
    // 4-directional fresh neighbours. Return minutes until none fresh, or -1 if impossible.
    static int orangesRotting(int[][] grid) { return -2; /* TODO */ }

    // ============ P3: First non-repeating character ============
    // Return the first character that occurs exactly once; '#' if there is none.
    static char firstNonRepeating(String s) { return '?'; /* TODO */ }

    // ============ P4: Insert k commas to MAXIMISE the number ============
    // Split s into k+1 non-empty contiguous parts; return the largest part (as a string).
    // e.g. ("857",1) -> "85"   ("999",2) -> "9"   ("1000",1) -> "100"
    static String kCommasMax(String s, int k) { return ""; /* TODO */ }

    // ============ P5: Maximum sum of any window of size k (sliding window) ============
    static int maxWindowSum(int[] a, int k) { return Integer.MIN_VALUE; /* TODO */ }

    // ==================== harness (do not edit) ====================
    static int total = 0, pass = 0;
    static void check(String name, Object expected, Supplier<Object> actual) {
        total++;
        Object a;
        try { a = actual.get(); } catch (Throwable t) { a = "ERROR:" + t.getClass().getSimpleName(); }
        boolean ok = Objects.equals(expected, a);
        if (ok) pass++;
        System.out.printf("[%s] %-20s expected=%s  actual=%s%n", ok ? "PASS" : "FAIL", name, expected, a);
    }
    static String runLL(IList ll) {
        ll.addLast(10); ll.addLast(20); ll.addLast(30); ll.addFirst(5); // [5,10,20,30]
        ll.removeAt(1);                                                  // remove the 10 -> [5,20,30]
        return ll.toList() + "|get2=" + ll.get(2);                       // -> [5, 20, 30]|get2=30
    }
    static int[][] copy(int[][] g) {
        int[][] c = new int[g.length][];
        for (int i = 0; i < g.length; i++) c[i] = g[i].clone();
        return c;
    }

    public static void main(String[] args) {
        int[][] gA = {{2,1,1},{1,1,0},{0,1,1}};   // -> 4
        int[][] gB = {{2,1,1},{0,1,1},{1,0,1}};   // -> -1 (bottom-left fresh is trapped)
        int[][] gC = {{0,2}};                      // -> 0 (no fresh)

        check("P1 arrayLinkedList", runLL(new DSASolutions.ArrayLinkedList()), () -> runLL(new ArrayLinkedList()));
        check("P2 rotten A",  DSASolutions.orangesRotting(copy(gA)), () -> orangesRotting(copy(gA)));
        check("P2 rotten B-1", DSASolutions.orangesRotting(copy(gB)), () -> orangesRotting(copy(gB)));
        check("P2 rotten C0",  DSASolutions.orangesRotting(copy(gC)), () -> orangesRotting(copy(gC)));
        check("P3 leetcode",  DSASolutions.firstNonRepeating("leetcode"), () -> firstNonRepeating("leetcode"));
        check("P3 swiss",     DSASolutions.firstNonRepeating("swiss"),    () -> firstNonRepeating("swiss"));
        check("P3 aabb #",    DSASolutions.firstNonRepeating("aabb"),     () -> firstNonRepeating("aabb"));
        check("P4 857,1",     DSASolutions.kCommasMax("857", 1),  () -> kCommasMax("857", 1));
        check("P4 999,2",     DSASolutions.kCommasMax("999", 2),  () -> kCommasMax("999", 2));
        check("P4 1000,1",    DSASolutions.kCommasMax("1000", 1), () -> kCommasMax("1000", 1));
        check("P5 win3",      DSASolutions.maxWindowSum(new int[]{2,1,5,1,3,2}, 3), () -> maxWindowSum(new int[]{2,1,5,1,3,2}, 3));
        check("P5 win2",      DSASolutions.maxWindowSum(new int[]{2,3,4,1,5}, 2),   () -> maxWindowSum(new int[]{2,3,4,1,5}, 2));
        check("P5 neg",       DSASolutions.maxWindowSum(new int[]{-1,-2,-3}, 2),    () -> maxWindowSum(new int[]{-1,-2,-3}, 2));

        System.out.println("--------------------------------------------------");
        System.out.println("SCORE: " + pass + " / " + total + (pass == total ? "  ALL GREEN" : "  keep going"));
    }
}
