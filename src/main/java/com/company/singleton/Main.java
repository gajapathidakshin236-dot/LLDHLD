package com.company.singleton;

public class Main {
    public static void main(String[] args) {
        Singleton a = Singleton.getInstance();
        Singleton b = Singleton.getInstance();

        System.out.println("Same instance: " + (a == b));
        System.out.println(a.businessLogic());
        System.out.println(b.businessLogic());
    }
}

