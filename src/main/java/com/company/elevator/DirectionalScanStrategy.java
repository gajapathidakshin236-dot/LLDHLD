package com.company.elevator;

import java.util.List;

/**
 * SCAN/LOOK-style dispatch strategy.
 *
 * Preference order:
 *   1. An idle elevator nearest the requesting floor
 *   2. An elevator already moving in the same direction as the request
 *      AND hasn't passed the requesting floor yet
 *
 * Fallback (prevents starvation):
 *   If no ideal candidate exists, pick the nearest non-maintenance
 *   elevator. It will finish its current work and then serve this request.
 */
public class DirectionalScanStrategy implements ElevatorSelectionStrategy {

    @Override
    public Elevator selectElevator(List<Elevator> elevators, Request request) {
        Elevator best = null;
        int bestDistance = Integer.MAX_VALUE;

        int requestFloor = request.getFloor();

        // Hall calls carry a direction; car calls don't (we only care
        // about same-direction matching for hall calls).
        Direction requestDirection = null;
        if (request instanceof HallCall) {
            requestDirection = ((HallCall) request).getDirection();
        }

        // Pass 1: find an ideal candidate
        for (Elevator e : elevators) {
            if (e.getState() == ElevatorState.MAINTENANCE) continue;

            int distance = Math.abs(e.getCurrentFloor() - requestFloor);

            boolean isIdle = e.isIdle();
            boolean sameDirectionAndOnTheWay =
                    requestDirection != null
                    && e.getDirection() == requestDirection
                    && isOnTheWay(e, requestFloor, requestDirection);

            if (isIdle || sameDirectionAndOnTheWay) {
                if (distance < bestDistance) {
                    bestDistance = distance;
                    best = e;
                }
            }
        }

        // Pass 2: fallback to nearest available — prevents starvation
        if (best == null) {
            for (Elevator e : elevators) {
                if (e.getState() == ElevatorState.MAINTENANCE) continue;
                int distance = Math.abs(e.getCurrentFloor() - requestFloor);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    best = e;
                }
            }
        }

        return best;
    }

    /**
     * An elevator is "on the way" to a request if it hasn't passed
     * the requesting floor yet in its current direction.
     *   Going UP   on floor 2, request at floor 5 going UP   → YES (still below)
     *   Going UP   on floor 7, request at floor 5 going UP   → NO  (already past)
     */
    private boolean isOnTheWay(Elevator e, int requestFloor, Direction requestDir) {
        if (requestDir == Direction.UP) {
            return e.getCurrentFloor() <= requestFloor;
        } else {
            return e.getCurrentFloor() >= requestFloor;
        }
    }
}
