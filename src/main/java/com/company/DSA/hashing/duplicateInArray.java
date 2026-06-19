package com.company.DSA.hashing;

import java.util.Arrays;
import java.util.List;

public class duplicateInArray {
    public static void dup( int[] arr){
        Arrays.sort(arr);

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] == arr[i - 1]) {
                System.out.println(arr[i]);
            }
        }
    }
}
