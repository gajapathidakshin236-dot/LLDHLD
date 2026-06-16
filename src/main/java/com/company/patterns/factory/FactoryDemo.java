package com.company.patterns.factory;

/**
 * FACTORY PATTERNS — all three, side by side.
 *
 * The interview trap is mixing these up. The KILLER TEST:
 *   Count the product TYPES being created.
 *     - 1 product type  -> Simple Factory OR Factory Method
 *         (switch in one class -> Simple; subclass decides -> Factory Method)
 *     - Multiple products that VARY TOGETHER -> Abstract Factory
 *
 * Run: javac FactoryDemo.java && java FactoryDemo
 */
public class FactoryDemo {
    public static void main(String[] args) {
        System.out.println("=== 1. SIMPLE FACTORY ===");
        // One class, switch on type, returns ONE product.
        PaymentFactory factory = new PaymentFactory();
        factory.create("UPI").pay(500);
        factory.create("CARD").pay(1200);

        System.out.println("\n=== 2. FACTORY METHOD ===");
        // Base class defines the flow; subclasses decide which product.
        CheckoutFlow upiFlow = new UPICheckout();
        upiFlow.checkout(750);
        CheckoutFlow cardFlow = new CardCheckout();
        cardFlow.checkout(999);

        System.out.println("\n=== 3. ABSTRACT FACTORY ===");
        // A factory that produces a matched FAMILY of products.
        UIFactory win = new WindowsUIFactory();
        win.createButton().render();
        win.createCheckbox().check();

        UIFactory mac = new MacUIFactory();
        mac.createButton().render();
        mac.createCheckbox().check();
    }
}

/* ===================== 1. SIMPLE FACTORY ===================== */

interface Payment {
    void pay(double amount);
}

class UPIPayment implements Payment {
    public void pay(double amount) { System.out.println("Paid " + amount + " via UPI"); }
}
class CardPayment implements Payment {
    public void pay(double amount) { System.out.println("Paid " + amount + " via Card"); }
}
class PayPalPayment implements Payment {
    public void pay(double amount) { System.out.println("Paid " + amount + " via PayPal"); }
}

// The "simple factory": one method, switches on a type string.
// Downside: adding a new type means MODIFYING this class (violates OCP).
class PaymentFactory {
    public Payment create(String type) {
        switch (type) {
            case "UPI":    return new UPIPayment();
            case "CARD":   return new CardPayment();
            case "PAYPAL": return new PayPalPayment();
            default: throw new IllegalArgumentException("Unknown payment: " + type);
        }
    }
}

/* ===================== 2. FACTORY METHOD ===================== */

// The base class defines the ALGORITHM (checkout). It doesn't know which
// concrete Payment to use — subclasses provide it via the factory method.
abstract class CheckoutFlow {
    public void checkout(double amount) {
        Payment p = createPayment();   // <-- the factory method
        p.pay(amount);
    }
    protected abstract Payment createPayment();   // subclasses decide
}

class UPICheckout extends CheckoutFlow {
    protected Payment createPayment() { return new UPIPayment(); }
}
class CardCheckout extends CheckoutFlow {
    protected Payment createPayment() { return new CardPayment(); }
}

/* ===================== 3. ABSTRACT FACTORY ===================== */

// Two product TYPES that must match (a Windows button shouldn't pair with
// a Mac checkbox). The abstract factory produces a consistent FAMILY.
interface Button   { void render(); }
interface Checkbox { void check(); }

class WindowsButton implements Button     { public void render() { System.out.println("Windows button"); } }
class WindowsCheckbox implements Checkbox { public void check()  { System.out.println("Windows checkbox check"); } }
class MacButton implements Button         { public void render() { System.out.println("Mac button"); } }
class MacCheckbox implements Checkbox     { public void check()  { System.out.println("Mac checkbox check"); } }

// The abstract factory: multiple create methods, one per product in the family.
interface UIFactory {
    Button createButton();
    Checkbox createCheckbox();
}

class WindowsUIFactory implements UIFactory {
    public Button createButton()     { return new WindowsButton(); }
    public Checkbox createCheckbox() { return new WindowsCheckbox(); }
}
class MacUIFactory implements UIFactory {
    public Button createButton()     { return new MacButton(); }
    public Checkbox createCheckbox() { return new MacCheckbox(); }
}
