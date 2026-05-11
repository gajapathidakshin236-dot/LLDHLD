package com.company.DSA;

public class FindAge {

    static String[] days = {
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday"
    };

    static int[] months = {
            31,28,31,30,31,30,
            31,31,30,31,30,31
    };

    // Leap year check
    static boolean isLeap(int y) {

        if (y % 400 == 0)
            return true;

        if (y % 100 == 0)
            return false;

        return y % 4 == 0;
    }

    static void exactAge(int bd, int bm, int by,
                         int cd, int cm, int cy) {
        if(cd<bd) {
            cm--;

            int prevMonth = cm - 1;

            if (prevMonth < 0)
                prevMonth = 11;

            cd += months[prevMonth];


            if (prevMonth == 1 && isLeap(cy)) {
                cd++;
            }
        }

        if(cm<bm) {
            cy--;
            int prevYear=cy-1;
            cm+=12;

        }

        int days = cd - bd;
        int monthsAge = cm - bm;

        int years = cy - by;
        System.out.println(
                years + " years " +
                        monthsAge + " months " +
                        days + " days"
        );
    }

    public static void main(String[] args) {

        // DOB
        int bd = 10;
        int bm = 3;
        int by = 1998;

        // Current Date
        int cd = 11;
        int cm = 1;
        int cy = 2026;

        exactAge(bd, bm, by,
                cd, cm, cy);
    }
}
