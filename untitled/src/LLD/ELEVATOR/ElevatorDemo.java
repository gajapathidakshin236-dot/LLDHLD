package LLD.ELEVATOR;

import java.util.Arrays;
import java.util.List;

/**
 * Runnable walkthrough of the elevator LLD.
 *
 * Sets up a 3-elevator fleet, submits a few hall + car calls, and steps the
 * simulation until every elevator goes idle.
 */
public class ElevatorDemo {

    private static final int MAX_SIMULATION_TICKS = 30;

    public static void main(final String[] args) {
        final List<Elevator> fleet = Arrays.asList(
                new Elevator(1),
                new Elevator(2),
                new Elevator(3)
        );

        final ElevatorController controller = new ElevatorController(fleet, new DirectionalScanStrategy());

        // A few hall calls
        controller.submitRequest(new HallCall(5, Direction.UP));
        controller.submitRequest(new HallCall(3, Direction.UP));
        controller.submitRequest(new HallCall(9, Direction.DOWN));

        // A car call — someone inside cabin 1 wants floor 7.
        controller.submitRequest(new CarCall(7));

        // Simulate until every elevator is idle (or we hit the tick cap).
        for (int tick = 1; tick <= MAX_SIMULATION_TICKS; tick++) {
            System.out.println("---- tick " + tick + " ----");
            controller.step();

            if (allIdle(fleet)) {
                System.out.println("All elevators idle. Done.");
                break;
            }
        }
    }

    private static boolean allIdle(final List<Elevator> fleet) {
        for (final Elevator elevator : fleet) {
            if (elevator.hasPendingWork()) {
                return false;
            }
        }
        return true;
    }
}
