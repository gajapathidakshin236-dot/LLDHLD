package com.company.DSA;

public class findDay {



        // Days of week
        static String[] days = {
                "Sunday",
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday"
        };

        public static void main(String[] args) {

            String currentDay = "Thursday";

            // Find index of current day
            int index = 0;

            for (int i = 0; i < days.length; i++) {
                if (days[i].equals(currentDay)) {
                    index = i;
                    break;
                }
            }

            // Standard year => shift by 1
            int shift = 365 % 7;

            int newIndex = (index + shift) % 7;

            System.out.println("26 March 2027 is " + days[newIndex]);
        }
    }

