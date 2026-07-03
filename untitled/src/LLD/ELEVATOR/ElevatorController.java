package LLD.ELEVATOR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Top-level façade for the elevator system.
 *
 * Responsibilities:
 *   - Owns the fleet of Elevator instances.
 *   - Uses an ElevatorSelectionStrategy (strategy pattern) to route each request.
 *   - Exposes a `step()` method that advances every elevator one tick (simulation).
 *
 * Thread-safety NOT addressed here — for a real system you'd guard requests
 * and step() with a lock or run each elevator on its own scheduler thread.
 */
public class ElevatorController {

    private final List<Elevator> elevatorFleet;
    private final ElevatorSelectionStrategy selectionStrategy;

    public ElevatorController(final List<Elevator> elevatorFleet,
                              final ElevatorSelectionStrategy selectionStrategy) {
        this.elevatorFleet     = elevatorFleet;
        this.selectionStrategy = selectionStrategy;
    }

    /** Route a request to the best elevator per the strategy. */
    public void submitRequest(final Request request) {
        final Elevator chosen = selectionStrategy.selectElevator(elevatorFleet, request);
        if (chosen == null) {
            System.out.println("No elevator available for " + request.getFloor());
            return;
        }
        chosen.addRequest(request);
        System.out.println("Assigned elevator " + chosen.getId() + " to serve request at floor " + request.getFloor());
    }

    /** One "tick" of the simulation — advance every elevator by one floor. */
    public void step() {
        for (final Elevator elevator : elevatorFleet) {
            elevator.move();
        }
    }

    public List<Elevator> getElevatorFleet() {
        return new ArrayList<>(elevatorFleet);
    }
}
