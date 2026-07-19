package com.company.abstractfactory;

public class Main {
    public static void main(String[] args) {
        String osName = System.getProperty("os.name", "").toLowerCase();

        // NOTE: this used pattern-matching-in-switch (case String s when ...),
        // which is Java 21+ (preview-only on Java 17). Rewritten 17-compatible
        // so the whole project builds on the ms-17 JDK. Same behaviour.
        GUIFactory factory;
        if (osName.contains("win")) {
            factory = new WindowsFactory();
        } else if (osName.contains("mac")) {
            factory = new MacFactory();
        } else {
            factory = new WindowsFactory(); // fallback
        }

        new Application(factory).render();
    }
}
