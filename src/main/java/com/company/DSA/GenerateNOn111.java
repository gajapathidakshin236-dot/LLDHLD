package com.company.DSA;

import java.util.ArrayList;
import java.util.List;

public class GenerateNOn111 {

        public static void generate(int n ,String ans ,List<String> res) {
            if(ans.length()==n) {
                res.add(ans);
                return;
            }

            generate(n,ans+"0",res);
           boolean cond1=  ans.charAt(ans.length()-1)!='1';
            if( ans.isEmpty() || cond1) {
                generate(n,ans+"1",res);
            }

        }


        public static void main(String[] args) {
            // Input length n
            int n = 3;

            // List to store results
            List<String> result = new ArrayList<>();

            // Start recursion with empty string
            generate(n, "", result);

            // Print results
            for (String s : result) {
                System.out.print(s + " ");
            }
            System.out.println();
        }

}
