package com.company.patterns.singleton;

/**
 * SINGLETON PATTERN
 *
 * Intent: Ensure a class has only ONE instance, and provide a global
 *         access point to it.
 *
 * This file shows the 3 variants worth knowing, plus a demo proving
 * each creates only one instance.
 *
 * Run: javac SingletonDemo.java && java SingletonDemo
 */
public class SingletonDemo {

    public static void main(String[] args) {
        System.out.println("=== Bill Pugh (recommended) ===");
        ConfigManager c1 = ConfigManager.getInstance();
        ConfigManager c2 = ConfigManager.getInstance();
        ConfigManager c3 = ConfigManager.getInstance();
        System.out.println("Same instance? " + (c1 == c2 && c2 == c3));
        System.out.println("Property: " + c1.getProperty("db.url"));

        System.out.println("\n=== Synchronized (simple, slower) ===");
        SyncLogger s1 = SyncLogger.getInstance();
        SyncLogger s2 = SyncLogger.getInstance();
        System.out.println("Same instance? " + (s1 == s2));

        System.out.println("\n=== Enum (bulletproof: reflection + serialization safe) ===");
        EnumConfig.INSTANCE.set("env", "prod");
        System.out.println("Value: " + EnumConfig.INSTANCE.get("env"));
    }
}

/**
 * VARIANT 1: Bill Pugh / Static Holder — the recommended default.
 *
 * Why it works:
 *  - The nested Holder class isn't loaded until getInstance() is first called,
 *    so initialization is LAZY (no cost if never used).
 *  - The JVM guarantees class initialization is thread-safe and runs exactly
 *    once, so we get thread-safety for FREE — no synchronized, no volatile.
 */
class ConfigManager {
    private ConfigManager() {
        System.out.println("Loading config...");   // prints once — proves single creation
    }

    private static class Holder {
        private static final ConfigManager INSTANCE = new ConfigManager();
    }

    public static ConfigManager getInstance() {
        return Holder.INSTANCE;
    }

    public String getProperty(String key) {
        return "value-for-" + key;
    }
}

/**
 * VARIANT 2: Synchronized method — simplest thread-safe version.
 *
 * Downside: locks on EVERY call, even after the instance exists.
 * Fine for low-frequency access (config at startup), bad for hot paths.
 */
class SyncLogger {
    private static SyncLogger instance;

    private SyncLogger() {}

    public static synchronized SyncLogger getInstance() {
        if (instance == null) {
            instance = new SyncLogger();
        }
        return instance;
    }
}

/**
 * VARIANT 3: Enum Singleton — Joshua Bloch's recommended approach.
 *
 * Advantages: thread-safe, serialization-safe, and reflection-proof
 * (you can't use Constructor.setAccessible to create a second instance).
 * Downside: can't extend a class.
 */
enum EnumConfig {
    INSTANCE;

    private final java.util.Map<String, String> data = new java.util.HashMap<>();

    public void set(String key, String value) { data.put(key, value); }
    public String get(String key)             { return data.get(key); }
}
