package com.company.DSA.intervals;

import java.util.ArrayList;
import java.util.List;

/*
*Given an integer array nums, return an array output where output[i] is the product of all the elements of nums except nums[i].

Each product is guaranteed to fit in a 32-bit integer.

Input: nums =  [1,2,4,6,8]
Output:  [48, 48, 24, 6,64]
* */
public class inter {

    public static void main(String[] args) {

        List<Integer> Arr = new ArrayList<>();
        Arr.add(1);
        Arr.add(2);
        Arr.add(4);
        Arr.add(6);
        Arr.add(8);

        System.out.println(solve(Arr));  ;
    }

    public static List<Integer> solve(List<Integer> Arr) { // [1,2,4,6]

        List<Integer> prefix = new ArrayList<>();
        List<Integer> suffix = new ArrayList<>();
        List<Integer> ans = new ArrayList<>();


        int n = Arr.size();

        prefix.add(1);//1
        for(int i = 1; i < n; i++){

            prefix.add(prefix.get(i-1)*Arr.get(i-1)); // [1,2,8,48]
        }

    // suffix =6
        // [1,2,4,6]


        // [48, 48, 24, 6]
        suffix.add(1);//6

        for(int i = n - 1; i >= 0; i--) {

            suffix.add(Arr.get(i-1)*suffix.get(n-i-1));
        }


        for(int  i=0;i< n;i++){
            ans.add(prefix.get(i)*suffix.get(n-i-1));
        }

        return ans;
    }
}
