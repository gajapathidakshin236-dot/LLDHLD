package com.company.abstractfactory;

public final class MacCheckbox implements Checkbox {
    @Override
    public void paint() {
        System.out.println("Render a Mac checkbox");
    }
}
