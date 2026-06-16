package com.company.patterns.decorator;

/**
 * DECORATOR PATTERN
 *
 * Intent: Add behavior to an object dynamically by WRAPPING it in another
 *         object that shares the same interface. Avoids subclass explosion.
 *
 * Key idea: the decorator IMPLEMENTS the same interface AND HOLDS an instance
 *           of it. Each layer delegates to the wrapped object and adds its bit.
 *
 * vs Inheritance: 6 toppings via subclassing = 2^6 = 64 classes.
 *                 Via decorator = 6 classes, stack any combination.
 *
 * Run: javac DecoratorDemo.java && java DecoratorDemo
 */
public class DecoratorDemo {
    public static void main(String[] args) {
        // Plain coffee
        Coffee c1 = new SimpleCoffee();
        print(c1);

        // Wrap it: coffee + milk
        Coffee c2 = new Milk(new SimpleCoffee());
        print(c2);

        // Stack many: coffee + milk + sugar + whip
        Coffee c3 = new Whip(new Sugar(new Milk(new SimpleCoffee())));
        print(c3);

        // Order matters for description; cost is additive either way
        Coffee c4 = new Milk(new Milk(new SimpleCoffee()));  // double milk
        print(c4);
    }

    private static void print(Coffee c) {
        System.out.println(c.getDescription() + " = " + c.getCost());
    }
}

// The shared interface.
interface Coffee {
    String getDescription();
    double getCost();
}

// The base concrete component.
class SimpleCoffee implements Coffee {
    public String getDescription() { return "Simple coffee"; }
    public double getCost()        { return 50; }
}

// Abstract decorator: IS-A Coffee and HAS-A Coffee (the wrapped one).
abstract class CoffeeDecorator implements Coffee {
    protected final Coffee inner;
    public CoffeeDecorator(Coffee inner) { this.inner = inner; }
}

// Each concrete decorator delegates to inner, then adds its own contribution.
class Milk extends CoffeeDecorator {
    public Milk(Coffee inner) { super(inner); }
    public String getDescription() { return inner.getDescription() + ", milk"; }
    public double getCost()        { return inner.getCost() + 20; }
}

class Sugar extends CoffeeDecorator {
    public Sugar(Coffee inner) { super(inner); }
    public String getDescription() { return inner.getDescription() + ", sugar"; }
    public double getCost()        { return inner.getCost() + 10; }
}

class Whip extends CoffeeDecorator {
    public Whip(Coffee inner) { super(inner); }
    public String getDescription() { return inner.getDescription() + ", whip"; }
    public double getCost()        { return inner.getCost() + 30; }
}
