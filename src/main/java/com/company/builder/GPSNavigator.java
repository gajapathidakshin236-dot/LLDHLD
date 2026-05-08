package com.company.builder;

public record GPSNavigator(String route) {
    public GPSNavigator() {
        this("221B Baker Street, London");
    }
}

