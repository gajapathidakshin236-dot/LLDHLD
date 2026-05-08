package com.company.singleton;

public final class Singleton {
    private static volatile Singleton instance;

    private Singleton() {
    }

    public static Singleton getInstance() {
        Singleton result = instance;
        if (result != null) return result;

        synchronized (Singleton.class) {
            result = instance;
            if (result == null) {
                result = new Singleton();
                instance = result;
            }
            return result;
        }
    }

    public String businessLogic() {
        return "Singleton@" + Integer.toHexString(System.identityHashCode(this));
    }
}

