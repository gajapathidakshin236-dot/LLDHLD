package com.company.abstractfactory;

public class Main {
    public static void main(String[] args) {
        String osName = System.getProperty("os.name", "").toLowerCase();

        GUIFactory factory = switch (osName) {
            case String s when s.contains("win") -> new WindowsFactory();
            case String s when s.contains("mac") -> new MacFactory();
            default -> new WindowsFactory(); // fallback
        };

        new Application(factory).render();
    }
}
