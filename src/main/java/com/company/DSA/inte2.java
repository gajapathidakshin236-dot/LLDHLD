package com.company.DSA;

import java.util.ArrayList;
import java.util.List;

public class inte2 {


    public static String encode(List<String> strs) {

        StringBuilder sb = new StringBuilder();

        for(String str : strs) {
            int n = str.length();
            if (n==0){
                sb.append(str);
            } else {
                sb.append(n);//5
                sb.append("#");
                sb.append(str);
            }

        }
        return sb.toString();

    }

    public static List<String> decode(String s) {

      List<String> list = new ArrayList<>();

      //22#
        int i = 0;
        while (i < s.length()) {
            int j = i;
            while (s.charAt(j) != '#') j++;        // find separator
            int len = Integer.parseInt(s.substring(i, j));  // read length
            list.add(s.substring(j+1, j+1+len)); // read exact content
            i = j + 1 + len;                       // jump to next
        }
        return list;
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("hello");
        list.add("world");
        System.out.println(encode(list));
        System.out.println(decode(encode(list)));
    }


}
