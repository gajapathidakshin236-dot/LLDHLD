package com.company.singleton;

public class singlton2 {

    private static volatile singlton2 instance;

    private singlton2() {

    }

    public static  singlton2 getInstance() {
        if (instance == null) {                  // 1st check, no lock
            synchronized (singlton2.class) {
                if (instance == null) {          // 2nd check, under lock
                    instance = new singlton2();
                }
            }
        }
        return instance;
    }
}
