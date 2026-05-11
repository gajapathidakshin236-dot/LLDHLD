package com.company.DSA;

import java.util.HashMap;

public class Cns {
// Count Number of Substrings
//
//
//19
//
//Problem Statement: You are given a string s and a positive integer k.
//Return the number of substrings that contain exactly k distinct characters.


  /*  Example 1:
    Input:
    s = "pqpqs", k = 2
    Output:
            7
    Explanation:
    All substrings with exactly 2 distinct characters:
            "pq", "pqp", "pqpq", "qp", "qpq", "pqs", "qs"
    Total = 7.

    Example 2:
    Input:
    s = "abcbaa", k = 3
    Output:
            5
    Explanation:
    All substrings with exactly 3 distinct characters:
            "abc", "abcb", "abcba", "bcba", "cbaa"
    Total = 5.*/
  public int atMostKDistinct(String s, int k) {

      int left = 0;
      int count = 0;

      HashMap<Character, Integer> map = new HashMap<>();

      for (int right = 0; right < s.length(); right++) {

          char ch = s.charAt(right);

          map.put(ch, map.getOrDefault(ch, 0) + 1);


          while (map.size() > k) {

              char leftChar = s.charAt(left);

              map.put(leftChar, map.get(leftChar) - 1);

              if (map.get(leftChar) == 0) {
                  map.remove(leftChar);
              }

              left++;
          }


          count += (right - left + 1);

}
      return count;
  }

    public int substrCount(String s, int k) {

        return atMostKDistinct(s, k)
                - atMostKDistinct(s, k - 1);
    }


}
