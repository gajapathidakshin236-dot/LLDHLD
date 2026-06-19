package com.company.DSA.strings;

import java.util.*;

public class GroupShiftedStrings {

    public List<List<String>> groupStrings(String[] strings) {

        Map<String, List<String>> map = new HashMap<>();

        for (String s : strings) {

            String key = getKey(s);

            map.computeIfAbsent(key, k -> new ArrayList<>())
                    .add(s);
        }

        return new ArrayList<>(map.values());
    }

    private String getKey(String s) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {

            int diff = (s.charAt(i) - s.charAt(0) + 26) % 26;
            sb.append(diff).append(",");
        }

        return sb.toString();
    }
}
