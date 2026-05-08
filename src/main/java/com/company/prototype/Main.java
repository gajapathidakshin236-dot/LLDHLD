package com.company.prototype;

public class Main {
    public static void main(String[] args) {
        PrototypeRegistry registry = new PrototypeRegistry();

        Circle circle = new Circle();
        circle.setX(10);
        circle.setY(10);
        circle.setRadius(20);
        circle.setColor("red");
        registry.addItem("Big red circle", circle);

        Rectangle rectangle = new Rectangle();
        rectangle.setX(20);
        rectangle.setY(30);
        rectangle.setWidth(10);
        rectangle.setHeight(15);
        rectangle.setColor("blue");
        registry.addItem("Medium blue rectangle", rectangle);

        Shape clone1 = registry.getById("Big red circle");
        Shape clone2 = registry.getByColor("blue");

        System.out.println("clone1 equals original circle: " + clone1.equals(circle));
        System.out.println("clone1 same instance as original: " + (clone1 == circle));
        System.out.println("clone2 equals original rectangle: " + clone2.equals(rectangle));
        System.out.println("clone2 same instance as original: " + (clone2 == rectangle));
    }
}

