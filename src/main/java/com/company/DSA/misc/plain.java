package com.company.DSA.misc;

public class plain {



    public static boolean palindrome(String str) {
        int left=0;
        int right=str.length()-1;
        while(left<right) {

            while(left<right && !isAlphaNumeric(str.charAt(left))) {
                left++;
            }

            while(left<right && !isAlphaNumeric(str.charAt(right))) {
                right--;
            }

            if( str.charAt(left)!=str.charAt(right)){
                return false;
            }
            left++;
            right--;
        }

        return true;

    }

    public static boolean isPalindrome(String str) {
        int left = 0;
        int right = str.length() - 1;

        while (left < right) {
            if (str.charAt(left) != str.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }


    public static boolean isAlphaNumeric(char c) {
        return (c >= 'a' && c <= 'z')   // lowercase letter
                || (c >= 'A' && c <= 'Z')   // uppercase letter
                || (c >= '0' && c <= '9');  // digit
    }
}
