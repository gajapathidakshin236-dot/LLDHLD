package com.company.elevator;

import java.util.ArrayList;
import java.util.List;

/**
 * Coordinates all elevators in a bank.
 *
 * Responsibilities:
 *   - Hold the elevators
 *   - Accept requests and delegate elevator selection to the strategy
 *   - Advance the simulation one tick at a time
 *
 * The strategy field is mutable (no `final`) so it can be swapped at
 * runtime — e.g., switch to a "least busy" strategy during peak hours.
 */
public class ElevatorController {

    private final List<Elevator> elevators;
    private ElevatorSelectionStrategy strategy;

    public ElevatorController(int numberOfElevators,
                              ElevatorSelectionStrategy strategy) {
        this.strategy = strategy;
        this.elevators = new ArrayList<>();
        for (int i = 1; i <= numberOfElevators; i++) {
            elevators.add(new Elevator(i));
        }
    }

    /** Hot-swap the dispatch algorithm at runtime. */
    public void setStrategy(ElevatorSelectionStrategy newStrategy) {
        this.strategy = newStrategy;
    }

    /**
     * Accept a new request. Use the strategy to pick an elevator and
     * hand it off. If no elevator is available (e.g., all in maintenance),
     * log and drop.
     */
    public void handleRequest(Request request) {
        Elevator chosen = strategy.selectElevator(elevators, request);
        if (chosen == null) {
            System.out.println("No elevator available for " + request);
            return;
        }
        System.out.println("Assigning Elevator " + chosen.getId()
                + " to " + request);
        chosen.addRequest(request);
    }

    /**
     * Advance the simulation by one tick. Each non-idle elevator moves
     * one floor. In a real system, this would be driven by a clock.
     */
    public void step() {
        for (Elevator e : elevators) {
            if (!e.isIdle()) {
                e.move();
            }
        }
    }

    /** Print a snapshot of all elevators for demo visibility. */
    public void printStatus() {
        System.out.println("---- Status ----");
        for (Elevator e : elevators) {
            System.out.println("Elevator " + e.getId()
                    + " | Floor: " + e.getCurrentFloor()
                    + " | State: " + e.getState()
                    + " | Pending: " + e.pendingRequests());
        }
    }

    /** Check if all elevators have completed all requests. */
    public boolean allIdle() {
        for (Elevator e : elevators) {
            if (!e.isIdle()) return false;
        }
        return true;
    }
}
