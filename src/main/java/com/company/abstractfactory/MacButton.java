package com.company.abstractfactory;

public final class MacButton implements Button {
    @Override
    public void paint() {
        System.out.println("Render a Mac button");
    }
}
