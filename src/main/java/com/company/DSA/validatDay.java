package com.company.DSA;

public class validatDay {
    static boolean isLeap(int y) {

        if (y % 400 == 0)
            return true;

        if (y % 100 == 0)
            return false;

        return y % 4 == 0;
    }

    boolean isValid(int d, int m, int y){
        if(m < 1 || m > 12) return false;
        int[] days = {31,28,31,30,31,30,31,31,30,31,30,31};
        if(isLeap(y)) days[1] = 29;
        return d >= 1 && d <= days[m-1];
    }
}
