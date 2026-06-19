package com.company.DSA.strings;

import java.util.*;

class WordPattern {

    public boolean wordPattern(String pattern, String s) {

        String[] words = s.split(" ");

        if (pattern.length() != words.length) return false;

        Map<Character, String> map = new HashMap<>();
        Map<String, Character> reverse = new HashMap<>();

        for (int i = 0; i < pattern.length(); i++) {

            char c = pattern.charAt(i);
            String w = words[i];
            if (map.containsKey(c) && !map.get(c).equals(w))
                return false;

            if (reverse.containsKey(w) && reverse.get(w) != c)
                return false;
            map.put(c, w);
            reverse.put(w, c);
        }

        return true;
    }
}
