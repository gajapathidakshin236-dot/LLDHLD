package com.company.abstractfactory;

public final class WindowsCheckbox implements Checkbox {
    @Override
    public void paint() {
        System.out.println("Render a Windows checkbox");
    }
}
