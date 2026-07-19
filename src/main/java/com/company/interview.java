package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class interview {


    // 2 4 6 -> 2,8,48
    //          48,24,6

             // 24,12,8

    //24 12 8

    public List<Integer> res(int [] a ) {

        int prefix[]= new int[a.length];

        int k=1;
        int n=a.length;

        int val=1;

        for(int i=0;i<n;i++) {
            val= val*a[i];//2
            prefix[i]=val;
        }

        int suffix[] = new int[a.length];
        Arrays.fill(suffix,-1);

        int val2=1;
        for(int j=n-1;j>=0;j--) {
            val2=val2*a[j]; // 6*4=24
            suffix[j]=val2;
        }

        // [completed so the file compiles] product-of-array-except-self:
        // answer[i] = product of everything left of i * everything right of i
        // e.g. [2,4,6] -> [24,12,8], matching the comments above.
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int left  = (i > 0)     ? prefix[i - 1] : 1;
            int right = (i < n - 1) ? suffix[i + 1] : 1;
            result.add(left * right);
        }
        return result;






    /*
    * /Message -> post called -> LB-> service -> RateLimiting logic-> Redis
    * { ipAddress or unique value Map  <String, Queue<Long>>> ->
                                         user ,    [ ,1,2,2,2,2,]
                                         *
       for each user request
       *
       *
       *
       *
       * Map  <String, Queue<Long>>> map = new map();
       * public void rateLimit(String UserId ) {
       *
       * int currFrame = System.convertToMilli(getSeconds(int  hours ,int min , int sec ));
       * int maxCapacity=5;
       *
       *
       * int now = system.currentmillis();
       *
       * Queue<Long> queue = map.get(userId);
       *
       * while(Math.abs(now-queue.peek())=>currentFrame) {
       *     queue.poll();
       * }
       *
       * if(maxCapacity<map.get(userid).size()) {
       *  throw LimitException();
       * } else {
       *  Queue<Long> queue=   queue.get(userId);
       *     queue.add(now);
       *     map.put(userId,queue);
       * }
       *
       * }
       *
       *
       *
       *
       * public getSeconds(int  hours ,int min , int sec) {
       *
       * int a = hours*60  ; // hours to //min
       * int b = a*60 + min*60; // min to sec
       *
       *
       *   return sec + b;
       *
       * }
       *
    * }
    *
    *
    * */


}
}
