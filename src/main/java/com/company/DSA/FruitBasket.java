package com.company.DSA;

import java.util.HashMap;
import java.util.List;

public class FruitBasket {
//   l     r
// 1,2,2,2,3
    public static int ans(List<Integer> nums,int k ) {
        int l = 0, r = 0, maxlen = 0;
        HashMap<Integer, Integer> map = new HashMap<>();
        while (r < nums.size()) {
            map.put(nums.get(r),map.getOrDefault(nums.get(r),0)+1);

            while (map.size()>k) {

                  int lval=map.getOrDefault(nums.get(l),0);  // returns 1

                  lval=lval-1;
                  map.put(nums.get(l), lval);
                  if(lval<=0) {
                      map.remove(nums.get(l));
                  } else {

                  }
                l++;
            }
            maxlen=Math.max(r-l+1,maxlen);
            r++;
        }


        return maxlen;
    }


    public static int ans(List<Integer> nums) {

        int l = 0;
        int maxlen = 0;

        HashMap<Integer, Integer> map = new HashMap<>();

        for (int r = 0; r < nums.size(); r++) {

            map.put(nums.get(r),
                    map.getOrDefault(nums.get(r), 0) + 1);

            // only one shrink needed
            if (map.size() > 2) {

                int left = nums.get(l);

                map.put(left, map.get(left) - 1);

                if (map.get(left) == 0) {
                    map.remove(left);
                }

                l++;
            }

            maxlen = Math.max(maxlen, r - l + 1);
        }

        return maxlen;
    }

}
