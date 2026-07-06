package com.company.patterns.state;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * STATE — behavior changes with internal state. BOTH implementations from
 * notes/03, side by side, because choosing between them is the senior signal:
 *
 *  A) Enum + allowed-transitions map — when states mainly GATE TRANSITIONS.
 *  B) Class-per-state (full GoF State) — when each state has RICH, DIFFERENT
 *     behavior, not just legality rules.
 */
public class StateDemo {
    public static void main(String[] args) {
        System.out.println("--- A) guarded enum state machine ---");
        Order order = new Order();
        order.transitionTo(OrderStatus.CONFIRMED);
        order.transitionTo(OrderStatus.PREPARING);
        try {
            order.transitionTo(OrderStatus.PLACED);       // illegal: going back
        } catch (IllegalStateException e) {
            System.out.println("blocked: " + e.getMessage());
        }
        try {
            order.transitionTo(OrderStatus.DELIVERED);    // illegal: skipping
        } catch (IllegalStateException e) {
            System.out.println("blocked: " + e.getMessage());
        }

        System.out.println();
        System.out.println("--- B) class-per-state ---");
        DeliveryOrder d = new DeliveryOrder();
        d.next();                        // preparing -> out for delivery
        d.cancel();                      // too late: rule lives INSIDE the state
        d.next();                        // out for delivery -> delivered
    }
}

/* ===== Variant A: enum + transition map (guarded state machine) ===== */

enum OrderStatus {
    PLACED, CONFIRMED, PREPARING, OUT_FOR_DELIVERY, DELIVERED, CANCELLED;

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED =
            new EnumMap<>(OrderStatus.class);

    static {   // the legal graph, wired once — ONE hop at a time
        ALLOWED.put(PLACED,           EnumSet.of(CONFIRMED, CANCELLED));
        ALLOWED.put(CONFIRMED,        EnumSet.of(PREPARING, CANCELLED));
        ALLOWED.put(PREPARING,        EnumSet.of(OUT_FOR_DELIVERY));
        ALLOWED.put(OUT_FOR_DELIVERY, EnumSet.of(DELIVERED));
        ALLOWED.put(DELIVERED,        EnumSet.noneOf(OrderStatus.class)); // terminal
        ALLOWED.put(CANCELLED,        EnumSet.noneOf(OrderStatus.class)); // terminal
    }

    boolean canTransitionTo(OrderStatus next) {
        return ALLOWED.get(this).contains(next);
    }
}

class Order {
    private OrderStatus status = OrderStatus.PLACED;

    void transitionTo(OrderStatus next) {
        if (!status.canTransitionTo(next)) {
            throw new IllegalStateException(status + " -> " + next + " not allowed");
        }
        System.out.println(status + " -> " + next);
        this.status = next;
    }
}

/* ===== Variant B: full State pattern (class per state) ===== */

interface OrderState {
    void next(DeliveryOrder o);
    void cancel(DeliveryOrder o);
}

class PreparingState implements OrderState {
    @Override public void next(DeliveryOrder o) {
        System.out.println("food ready -> out for delivery");
        o.setState(new OutForDeliveryState());
    }
    @Override public void cancel(DeliveryOrder o) {
        System.out.println("cancelled while preparing (refund in full)");
        o.setState(new CancelledState());
    }
}

class OutForDeliveryState implements OrderState {
    @Override public void next(DeliveryOrder o) {
        System.out.println("handed to customer -> delivered");
        o.setState(new DeliveredState());
    }
    @Override public void cancel(DeliveryOrder o) {
        // The rule lives HERE, not in a central if-else:
        System.out.println("cannot cancel: courier already on the way");
    }
}

class DeliveredState implements OrderState {
    @Override public void next(DeliveryOrder o)   { System.out.println("already delivered"); }
    @Override public void cancel(DeliveryOrder o) { System.out.println("cannot cancel: delivered"); }
}

class CancelledState implements OrderState {
    @Override public void next(DeliveryOrder o)   { System.out.println("order is cancelled"); }
    @Override public void cancel(DeliveryOrder o) { System.out.println("already cancelled"); }
}

class DeliveryOrder {
    private OrderState state = new PreparingState();

    void setState(OrderState s) { this.state = s; }
    void next()   { state.next(this); }
    void cancel() { state.cancel(this); }
}
