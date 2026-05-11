package com.company.DSA;

import java.util.Stack;

public class ReverseStack {
//[2,3,1,4]

    public static void reverseStack(Stack<Integer> st) {


        if(st.isEmpty()) {
            return;
        }

        int temp = st.pop(); //2

        reverseStack(st);

        insertRev(st, temp);
    }

    public static void insertRev(Stack<Integer> st, Integer temp) {
            if(st.isEmpty()){st.push(temp);return;}

            //[ 4,1,3]  ===========
            int top = st.pop();//1
            insertRev(st,temp);//[3,2]
            st.push(top);//[4,1,3,2]


    }


    public static void main(String[] args) {
        // Create a sample stack
        Stack<Integer> st = new Stack<>();
        st.push(4);
        st.push(1);
        st.push(3);
        st.push(2);

        // Reverse the stack
        reverseStack(st);

        // Print the reversed stack
        System.out.print("Reversed Stack: ");
        while (!st.isEmpty()) {
            System.out.print(st.pop() + " ");
        }
        System.out.println();
    }
}
