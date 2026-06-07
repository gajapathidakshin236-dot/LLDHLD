package com.company.DSA;

public class CharacterReplacement {
    public static void main(String[] args) {

    }
//K=1
    public static int characterReplacement(String s, int k) {
        int[] freq = new int[26];
        int left = 0, right = 0;

        int maxCount = 0;//freqmax
        int maxLength = 0;

        //baaccccc
        while (right < s.length()) {

            freq[s.charAt(right)-'a']++;

            maxCount=Math.max(maxCount,freq[s.charAt(right)-'a']);// mc=4 a4 b1 c1
            //l=1 r=6
            while ( (right - left + 1) - maxCount > k) {
               //5-4
                freq[s.charAt(left)-'a']--;
                left++;
            }

            maxLength = Math.max(maxLength,right - left + 1);
            right++;

        }

        return maxLength;

    }

}
