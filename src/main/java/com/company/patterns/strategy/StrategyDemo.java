package com.company.patterns.strategy;

/**
 * STRATEGY — swappable algorithms behind one interface (see notes/03).
 *
 * This demo mirrors the note's core claims:
 *   1. algorithms behind one interface, injected (DIP)
 *   2. runtime swap — the "chess pawn promotion" insight: swap the BEHAVIOR
 *      on the same object instead of replacing the object.
 *
 * The pattern used in EVERY system in this repo: elevator dispatch, parking
 * spot allocation/fee/payment, rate-limiting algorithms, Kafka partitioners,
 * delivery agent assignment.
 */
public class StrategyDemo {
    public static void main(String[] args) {
        // 1) Different pricing algorithms, chosen at runtime
        Checkout checkout = new Checkout(new RegularPricing());
        System.out.println("regular:  " + checkout.total(100.0));

        checkout.setPricing(new FestivalPricing());          // runtime swap
        System.out.println("festival: " + checkout.total(100.0));

        checkout.setPricing(amount -> amount * 0.5);         // lambda = strategy too
        System.out.println("clearance:" + checkout.total(100.0));

        // 2) The chess insight: same piece object, new movement behavior
        Piece pawn = new Piece("e8-pawn", new PawnMovement());
        System.out.println(pawn.describeMove());
        pawn.setMovement(new QueenMovement());               // PROMOTION: swap behavior,
        System.out.println(pawn.describeMove());             // same identity
    }
}

/* ---- pricing example ---- */

interface PricingStrategy {
    double price(double amount);
}

class RegularPricing implements PricingStrategy {
    @Override public double price(double amount) { return amount; }
}

class FestivalPricing implements PricingStrategy {
    @Override public double price(double amount) { return amount * 0.8; }
}

class Checkout {
    private PricingStrategy pricing;                 // NOT final -> swappable

    Checkout(PricingStrategy pricing) { this.pricing = pricing; }

    void setPricing(PricingStrategy pricing) { this.pricing = pricing; }

    double total(double amount) { return pricing.price(amount); }
}

/* ---- chess-promotion example ---- */

interface MovementStrategy {
    String describe();
}

class PawnMovement implements MovementStrategy {
    @Override public String describe() { return "one square forward"; }
}

class QueenMovement implements MovementStrategy {
    @Override public String describe() { return "any direction, any distance"; }
}

class Piece {
    private final String id;                 // identity NEVER changes
    private MovementStrategy movement;       // behavior DOES

    Piece(String id, MovementStrategy movement) { this.id = id; this.movement = movement; }

    void setMovement(MovementStrategy m) { this.movement = m; }

    String describeMove() { return id + " moves: " + movement.describe(); }
}
