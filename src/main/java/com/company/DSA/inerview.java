package com.company.DSA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class inerview {

/*
* String s = "12AB98VC7NP61";
* "A12"

Output 1 : [12 , 98 ,7,61]
Output 2 : 61*/

    public static void main(String[] args) {
        answer("12AB98VC7NP61");
    }
    public static void answer(String s) {
        int n = s.length();

        StringBuilder sb = new StringBuilder();//12
        List<Integer> list = new ArrayList<>();

        for(int i=0 ; i< n ;i++) {
          char c = s.charAt(i);


          if(c >='0' && c<='9') {
              sb.append(c);
          } else {
              if(!sb.isEmpty()) {
                  list.add(Integer.parseInt(sb.toString()));
              }
              sb.setLength(0);
          }
        }

        System.out.println(list);

       n=list.size();

        int largest = Integer.MIN_VALUE;//5
        int largest2 = Integer.MIN_VALUE;//3
        //[5,3,4]// 3,4,5   4>3 and 4<5
        //l=5 l2=min

       for(int i=0 ; i< n ;i++) {

           if (list.get(i)>largest2 &&  list.get(i)<largest) {
             largest2=list.get(i);
           } else if(list.get(i)>largest) {
               largest2=largest;
               largest = list.get(i);
           }

       }

        if (largest2 == Integer.MIN_VALUE) {
            if (largest == Integer.MIN_VALUE) {
                System.out.println("Notfound");
            }else {
                System.out.println(largest);
            }
        } else {
            System.out.println(largest2);

        }
    }


/*
* Input : 1, 2 ,4 , 10, 11 ,12, 12, 12, 12 , 15
target : 12

Output : 5 , 8*/

    public static void answer2(List<Integer> list , int target) {

        int startpos=Integer.MAX_VALUE;
        int endpos=Integer.MIN_VALUE;
        int n = list.size();

        boolean found = false;

        for(int i=0 ; i<n ;i++) {

        }

    }






























}
