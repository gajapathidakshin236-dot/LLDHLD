package com.company.parkinglot;

/**
 * STRATEGY #3: how to COLLECT the money (separate from computing it).
 */
public interface PaymentStrategy {
    void pay(double amount);
}

class UPIPayment implements PaymentStrategy {
    public void pay(double amount) { System.out.println("  Paid " + amount + " via UPI"); }
}

class CardPayment implements PaymentStrategy {
    public void pay(double amount) { System.out.println("  Paid " + amount + " via Card"); }
}

class CashPayment implements PaymentStrategy {
    public void pay(double amount) { System.out.println("  Paid " + amount + " in Cash"); }
}
