package com.company.DSA;

public class findD {


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

        // Convert date into total days
        static int totalDays(int d, int m, int y) {

            int total = 0;

            // Add days from previous years
            for(int i = 0; i < y; i++) {

                if(isLeap(i))
                    total += 366;
                else
                    total += 365;
            }

            // Add days from previous months
            for(int i = 0; i < m - 1; i++) {
                total += months[i];
            }

            // Add leap day if current year is leap and month > Feb
            if(m > 2 && isLeap(y)) {
                total++;
            }

            // Add current day
            total += d;

            return total;
        }

        static String findDay(int d, int m, int y) {

            int total = totalDays(d, m, y);

            int index = total % 7;

            return days[index];
        }

        public static void main(String[] args) {

            int d = 26;
            int m = 3;
            int y = 2026;

            System.out.println(findDay(d, m, y));
        }

}
