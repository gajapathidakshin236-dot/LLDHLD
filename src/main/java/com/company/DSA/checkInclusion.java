package com.company.DSA;

import java.util.Arrays;

public class checkInclusion {


    public boolean checkInclusionn(String s1, String s2) {
        if (s1.length() > s2.length()) return false;

        int[] s1freq = new int[26];
        int[] s2freq = new int[26];

        // fill s1 freq
        for (char c : s1.toCharArray()) s1freq[c - 'a']++;

        int left = 0;
        for (int right = 0; right < s2.length(); right++) {
            // add right char to window
            s2freq[s2.charAt(right) - 'a']++;

            if(right>=s1.length()) {
                s2freq[s2.charAt(left) - 'a']--;
                left++;
            }

            if (Arrays.equals(s1freq, s2freq)) return true;
        }
        return false;
    }
}
