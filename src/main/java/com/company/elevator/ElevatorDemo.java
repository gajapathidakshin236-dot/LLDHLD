package com.company.elevator;

/**
 * Demo: 3 elevators serving a mix of hall calls and car calls.
 *
 * Run with:
 *   javac *.java
 *   java ElevatorDemo
 */
public class ElevatorDemo {

    public static void main(String[] args) {
        // Wire up the system
        ElevatorSelectionStrategy strategy = new DirectionalScanStrategy();
        ElevatorController controller = new ElevatorController(3, strategy);

        System.out.println("=== Initial state ===");
        controller.printStatus();

        // Submit some requests
        System.out.println("\n=== Submitting requests ===");
        controller.handleRequest(new HallCall(5, Direction.UP));
        controller.handleRequest(new HallCall(2, Direction.UP));
        controller.handleRequest(new CarCall(8));

        System.out.println("\n=== After requests ===");
        controller.printStatus();

        // Run the simulation until all elevators finish their work
        System.out.println("\n=== Running simulation ===");
        int tick = 0;
        while (!controller.allIdle() && tick < 20) {
            tick++;
            System.out.println("\n-- Tick " + tick + " --");
            controller.step();
            controller.printStatus();
        }

        System.out.println("\n=== Simulation complete in " + tick + " ticks ===");
    }
}
