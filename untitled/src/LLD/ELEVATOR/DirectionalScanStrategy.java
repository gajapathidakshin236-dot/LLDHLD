package LLD.ELEVATOR;

import java.util.List;

/**
 * "Look" / directional scan policy.
 *
 * Preference (in order):
 *   1. An IDLE elevator, closest to the requested floor.
 *   2. An elevator currently moving in the SAME direction as the request AND whose
 *      current floor is "on the way" (below the request when going UP, above when
 *      going DOWN) — pick the closest such one.
 *   3. Fallback: closest live elevator by absolute distance, regardless of state.
 *
 * MAINTENANCE elevators are skipped entirely.
 *
 * For CarCalls (no direction) we skip step 2 and rely on 1 + 3.
 */
public class DirectionalScanStrategy implements ElevatorSelectionStrategy {

    @Override
    public Elevator selectElevator(final List<Elevator> elevators, final Request request) {
        final int requestFloor = request.getFloor();
        final Direction requestedDirection = (request instanceof HallCall)
                ? ((HallCall) request).getDirection()
                : null;

        final Elevator preferred = choosePreferred(elevators, requestFloor, requestedDirection);
        if (preferred != null) {
            return preferred;
        }
        return closestAvailable(elevators, requestFloor);
    }

    private Elevator choosePreferred(final List<Elevator> elevators,
                                     final int requestFloor,
                                     final Direction requestedDirection) {
        Elevator bestSoFar     = null;
        int      bestDistance  = Integer.MAX_VALUE;

        for (final Elevator elevator : elevators) {
            if (elevator.getState() == ElevatorState.MAINTENANCE) {
                continue;
            }
            final int distance = Math.abs(elevator.getCurrentFloor() - requestFloor);
            final boolean isIdle = elevator.getState() == ElevatorState.IDLE;
            final boolean isOnTheWay = requestedDirection != null
                    && elevator.getDirection() == requestedDirection
                    && isOnTheWay(elevator, requestFloor, requestedDirection);

            if ((isIdle || isOnTheWay) && distance < bestDistance) {
                bestDistance = distance;
                bestSoFar    = elevator;
            }
        }
        return bestSoFar;
    }

    private Elevator closestAvailable(final List<Elevator> elevators, final int requestFloor) {
        Elevator bestSoFar     = null;
        int      bestDistance  = Integer.MAX_VALUE;

        for (final Elevator elevator : elevators) {
            if (elevator.getState() == ElevatorState.MAINTENANCE) {
                continue;
            }
            final int distance = Math.abs(elevator.getCurrentFloor() - requestFloor);
            if (distance < bestDistance) {
                bestDistance = distance;
                bestSoFar    = elevator;
            }
        }
        return bestSoFar;
    }

    private boolean isOnTheWay(final Elevator elevator, final int requestFloor, final Direction requestedDirection) {
        if (requestedDirection == Direction.UP) {
            return elevator.getCurrentFloor() <= requestFloor;
        }
        return elevator.getCurrentFloor() >= requestFloor;
    }
}
