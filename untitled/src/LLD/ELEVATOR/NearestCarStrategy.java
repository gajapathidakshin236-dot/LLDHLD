package LLD.ELEVATOR;

import java.util.List;

/**
 * Nearest Car strategy: simply pick the non-maintenance elevator whose current
 * floor is closest to the requested floor, ignoring direction.
 *
 * Useful for demos and for buildings where directional scan gives no benefit
 * (e.g., 2-3 floor buildings, small fleets).
 */
public class NearestCarStrategy implements ElevatorSelectionStrategy {

    @Override
    public Elevator selectElevator(final List<Elevator> elevators, final Request request) {
        Elevator bestElevator = null;
        int      bestDistance = Integer.MAX_VALUE;

        for (final Elevator elevator : elevators) {
            if (elevator.getState() == ElevatorState.MAINTENANCE) {
                continue;
            }
            final int distance = Math.abs(elevator.getCurrentFloor() - request.getFloor());
            if (distance < bestDistance) {
                bestDistance = distance;
                bestElevator = elevator;
            }
        }
        return bestElevator;
    }
}
