package com.company.abstractfactory;

public final class WindowsButton implements Button {
    @Override
    public void paint() {
        System.out.println("Render a Windows button");
    }
}
