package com.company.DSA;

/**
 * LeetCode #13 - Roman to Integer
 * Walk left to right; if cur < next, subtract cur else add.
 * Time: O(n)  Space: O(1)
 */
public class RomanToInteger {
    public int romanToInt(String s) {
        int[] v = new int[128];
        v['I']=1; v['V']=5; v['X']=10; v['L']=50; v['C']=100; v['D']=500; v['M']=1000;
        int res = 0, n = s.length();
        for (int i = 0; i < n; i++) {
            int cur = v[s.charAt(i)];
            int nxt = (i + 1 < n) ? v[s.charAt(i + 1)] : 0;
            res += (cur < nxt) ? -cur : cur;
        }
        return res;
    }
}
