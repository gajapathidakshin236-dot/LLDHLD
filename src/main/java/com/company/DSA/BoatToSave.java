package com.company.DSA;

import java.util.Arrays;

public class BoatToSave {
//[3,1,2,2] -> [1,2,2,3]

    public int numRescueBoats(int[] people, int limit) {
        Arrays.sort(people);
        int count=0;
        int left=0,right=people.length-1;

        while (left<=right) {
            if(people[right] > limit) {
                right--;
            } else if (people[right] == limit) {
                count++;
                right--;
            } else if (people[left] + people[right] <= limit) {
                count++;
                left++;
                right--;
            } else {
                right--;
            }
        }

        return count;
    }

}
