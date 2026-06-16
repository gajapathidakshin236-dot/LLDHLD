package com.company.patterns.adapter;

/**
 * ADAPTER PATTERN
 *
 * Intent: Make an incompatible class fit a target interface by wrapping it
 *         and translating calls. Like a travel plug adapter between an
 *         Indian charger and a US wall socket.
 *
 * The adapter IMPLEMENTS the target interface and DELEGATES to the wrapped
 * (adaptee) class, translating method names / param order / units.
 *
 * vs Decorator: Decorator keeps the SAME interface and adds behavior.
 *               Adapter CHANGES the interface (incompatible -> compatible).
 *
 * Run: javac AdapterDemo.java && java AdapterDemo
 */
public class AdapterDemo {
    public static void main(String[] args) {
        // The legacy gateway we can't modify (imagine it's in a third-party JAR).
        OldPaymentGateway legacy = new OldPaymentGateway();

        // Wrap it so our new code can use it through the modern interface.
        PaymentProcessor processor = new PaymentGatewayAdapter(legacy);

        // New code only knows PaymentProcessor — doesn't care about the legacy API.
        processor.processPayment(500.0, "INR");
        processor.processPayment(75.50, "USD");
    }
}

// The interface our NEW code expects.
interface PaymentProcessor {
    void processPayment(double amount, String currency);
}

// The LEGACY class — different method name, different param order,
// different units (works in paise: 1 rupee = 100 paise). We cannot change it.
class OldPaymentGateway {
    public void doPayment(String currencyCode, double amountInSmallestUnit) {
        System.out.println("[LEGACY] Charged " + amountInSmallestUnit
                + " (smallest unit) in " + currencyCode);
    }
}

// The ADAPTER: implements the new interface, wraps the legacy class,
// and translates between the two worlds.
class PaymentGatewayAdapter implements PaymentProcessor {
    private final OldPaymentGateway legacy;

    public PaymentGatewayAdapter(OldPaymentGateway legacy) {
        this.legacy = legacy;
    }

    @Override
    public void processPayment(double amount, String currency) {
        // Translation: rupees/dollars -> smallest unit, and reorder args.
        double amountInSmallestUnit = amount * 100;
        legacy.doPayment(currency, amountInSmallestUnit);
    }
}
