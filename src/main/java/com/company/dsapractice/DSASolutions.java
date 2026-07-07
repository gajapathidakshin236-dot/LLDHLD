package com.company.dsapractice;
import java.util.*;

/** ANSWER KEY for DSAPractice. Run to see each approach working. Peek only after you try. */
public class DSASolutions {

    // ---- P1: array-backed singly linked list ----
    static class ArrayLinkedList implements IList {
        int[] val = new int[4], next = new int[4];
        int head = -1, size = 0, cap = 4, used = 0, freeHead = -1;
        private void grow() { cap *= 2; val = Arrays.copyOf(val, cap); next = Arrays.copyOf(next, cap); }
        private int newNode(int x) {
            int i;
            if (freeHead != -1) { i = freeHead; freeHead = next[freeHead]; }
            else { if (used == cap) grow(); i = used++; }
            val[i] = x; next[i] = -1; size++; return i;
        }
        public void addFirst(int x) { int i = newNode(x); next[i] = head; head = i; }
        public void addLast(int x) {
            int i = newNode(x);
            if (head == -1) { head = i; return; }
            int cur = head; while (next[cur] != -1) cur = next[cur]; next[cur] = i;
        }
        public int get(int index) { int cur = head; for (int s = 0; s < index; s++) cur = next[cur]; return val[cur]; }
        public void removeAt(int index) {
            if (index == 0) { int old = head; head = next[head]; next[old] = freeHead; freeHead = old; size--; return; }
            int prev = head; for (int s = 0; s < index - 1; s++) prev = next[prev];
            int rem = next[prev]; next[prev] = next[rem]; next[rem] = freeHead; freeHead = rem; size--;
        }
        public List<Integer> toList() { List<Integer> r = new ArrayList<>(); int cur = head; while (cur != -1) { r.add(val[cur]); cur = next[cur]; } return r; }
    }

    // ---- P2: multi-source BFS ----
    static int orangesRotting(int[][] g) {
        int R = g.length, C = g[0].length, fresh = 0;
        Deque<int[]> q = new ArrayDeque<>();
        for (int i = 0; i < R; i++) for (int j = 0; j < C; j++) {
            if (g[i][j] == 2) q.add(new int[]{i, j});
            else if (g[i][j] == 1) fresh++;
        }
        if (fresh == 0) return 0;
        int minutes = 0;
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
        while (!q.isEmpty()) {
            int sz = q.size(); boolean any = false;
            for (int s = 0; s < sz; s++) {
                int[] c = q.poll();
                for (int[] d : dirs) {
                    int ni = c[0] + d[0], nj = c[1] + d[1];
                    if (ni >= 0 && ni < R && nj >= 0 && nj < C && g[ni][nj] == 1) {
                        g[ni][nj] = 2; fresh--; q.add(new int[]{ni, nj}); any = true;
                    }
                }
            }
            if (any) minutes++;
        }
        return fresh == 0 ? minutes : -1;
    }

    // ---- P3: count then scan ----
    static char firstNonRepeating(String s) {
        int[] cnt = new int[128];
        for (int i = 0; i < s.length(); i++) cnt[s.charAt(i)]++;
        for (int i = 0; i < s.length(); i++) if (cnt[s.charAt(i)] == 1) return s.charAt(i);
        return '#';
    }

    // ---- P4: largest length-(n-k) substring ----
    static String kCommasMax(String s, int k) {
        int n = s.length(), L = n - k;
        String best = "";
        for (int i = 0; i + L <= n; i++) {
            String sub = s.substring(i, i + L);
            if (best.isEmpty() || sub.compareTo(best) > 0) best = sub;
        }
        return best;
    }

    // ---- P5: sliding window ----
    static int maxWindowSum(int[] a, int k) {
        int sum = 0; for (int i = 0; i < k; i++) sum += a[i];
        int max = sum;
        for (int i = k; i < a.length; i++) { sum += a[i] - a[i - k]; max = Math.max(max, sum); }
        return max;
    }

    public static void main(String[] args) {
        System.out.println("P1 runLL       -> " + DSAPractice.runLL(new ArrayLinkedList()));
        System.out.println("P2 rotten A    -> " + orangesRotting(new int[][]{{2,1,1},{1,1,0},{0,1,1}}));
        System.out.println("P2 rotten B    -> " + orangesRotting(new int[][]{{2,1,1},{0,1,1},{1,0,1}}));
        System.out.println("P3 leetcode    -> " + firstNonRepeating("leetcode"));
        System.out.println("P3 swiss       -> " + firstNonRepeating("swiss"));
        System.out.println("P4 (857,1)     -> " + kCommasMax("857", 1));
        System.out.println("P4 (1000,1)    -> " + kCommasMax("1000", 1));
        System.out.println("P5 win3        -> " + maxWindowSum(new int[]{2,1,5,1,3,2}, 3));
    }
}
