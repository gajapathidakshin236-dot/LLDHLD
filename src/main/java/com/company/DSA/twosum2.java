package com.company.DSA;

import java.security.Key;
import java.util.HashMap;

public class twosum2 {

    public int[] twoSum(int[] numbers, int target) {
        HashMap<Integer,Integer> map =new HashMap<>();

        for(int i =0;  i< numbers.length;i++) {
            int need =  target-numbers[i];
            if(map.containsKey(need)) {
                return new int[]{i, map.get(need)};
            }
            map.put(numbers[i],i);
        }
        return new int[]{-1,-1};
    }
//[1,2,3,4]

    public int[] twoSumss(int[] numbers, int target) {

        int l=0 ,  r=numbers.length-1;

        while (l<r) {
            int sum = numbers[l] + numbers[r];
            if(sum==target) {
                return new int[]{l,r};
            } else if (sum>target) {
                r--;
            } else {
                l--;
            }
        }
        return new int[]{-1,-1};
    }

}
