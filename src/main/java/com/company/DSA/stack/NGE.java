package com.company.DSA.stack;
import java.util.*;
public class NGE {


        public int[] nextGreater(int[] nums) {

            Stack<Integer> st = new Stack<>();


            int n = nums.length;
            int[] res = new int[n];


            for (int i = n - 1; i >= 0; i--) {

                while (!st.isEmpty() && st.peek() <= nums[i]) {
                    st.pop();
                }

                if (st.isEmpty()){ res[i] = -1;}


                else { res[i] = st.peek();}


                st.push(nums[i]);
            }


            return res;
        }

    public int[] nextGreater2(int[] nums) {

        Stack<Integer> st = new Stack<>();


        int n = nums.length;
        int[] res = new int[n];

        Arrays.fill(res, -1);

        //[1, 2, 1][1, 2, 1]5%3=2
        for (int i = 2*n-1; i >= 0; i--) {

            int pos =i % n;


            while (!st.isEmpty() && st.peek() <= nums[pos]) {
                st.pop();
            }

            if (st.isEmpty()){ res[pos] = -1;}


            else {res[pos] = st.peek();}


            st.push(nums[pos]);

        }
        return res;
    }
    }

