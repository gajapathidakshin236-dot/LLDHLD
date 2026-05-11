package com.company.DSA;

public class Date_bet {

    static int[] months = {
            31,28,31,30,31,30,
            31,31,30,31,30,31
    };

    // Check leap year
    static boolean isLeap(int year) {

        if (year % 400 == 0)
            return true;

        if (year % 100 == 0)
            return false;

        return year % 4 == 0;
    }
    public static int ldays(int y) {
        return  isLeap(y) ? 366: 365;
    }

public static int totalDays(int d, int m, int y ) {
         int days=0;
        for(int i=0;i<y;i++) {
            days+=ldays(i);
        }

    for(int i=0;i<m-1;i++) {

        days+=months[i];

        if(i==1 && isLeap(y)) {
            days+=1;
        }
    }

    days=days+d;

    return days;
}

    public static void main(String[] args) {

        // Date 1
        int d1 = 26, m1 = 3, y1 = 2026;

        // Date 2
        int d2 = 26, m2 = 3, y2 = 2027;

        int total1 = totalDays(d1, m1, y1);
        int total2 = totalDays(d2, m2, y2);

        int diff = Math.abs(total2 - total1);

        System.out.println("Days between dates = " + diff);
    }


}
