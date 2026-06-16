package com.company.elevator;

import java.util.TreeSet;

/**
 * A single elevator car.
 *
 * Holds its current floor, state, direction, and the set of floors
 * it still needs to visit. Uses a TreeSet for target floors so they
 * stay sorted automatically — this enables the SCAN/LOOK algorithm
 * (serve floors in order rather than in request-arrival order).
 *
 * Concurrency note: methods are NOT thread-safe. In a multi-threaded
 * setting, guard mutating methods (addRequest, move) with synchronized
 * or a per-elevator lock.
 */
public class Elevator {

    private final int id;
    private int currentFloor;
    private ElevatorState state;
    private Direction direction;

    // Pending floors to visit, kept sorted.
    // TreeSet gives us:
    //   - automatic sorted order (serve floors in sequence)
    //   - no duplicates (multiple presses of '5' = one entry)
    //   - O(log n) ceiling/floor lookups for "next target above/below"
    private final TreeSet<Integer> targetFloors;

    public Elevator(int id) {
        this.id = id;
        this.currentFloor = 0;
        this.state = ElevatorState.IDLE;
        this.direction = Direction.IDLE;
        this.targetFloors = new TreeSet<>();
    }

    /**
     * Accept a new request. The elevator only stores the FLOOR — once
     * committed to going somewhere, hall vs. car distinction doesn't matter.
     * The first request on an idle elevator sets its initial direction.
     */
    public void addRequest(Request request) {
        targetFloors.add(request.getFloor());

        if (state == ElevatorState.IDLE) {
            updateDirection();
            state = (direction == Direction.UP)
                    ? ElevatorState.MOVING_UP
                    : ElevatorState.MOVING_DOWN;
        }
    }

    /**
     * Advance one floor toward the next target. Called once per "tick"
     * by the controller. If the car arrives at a target floor, it's
     * served (removed from the set) and we recompute direction.
     */
    public void move() {
        if (targetFloors.isEmpty()) {
            state = ElevatorState.IDLE;
            direction = Direction.IDLE;
            return;
        }

        if (direction == Direction.UP) {
            currentFloor++;
        } else if (direction == Direction.DOWN) {
            currentFloor--;
        }

        // Arrived at a requested floor?
        if (targetFloors.contains(currentFloor)) {
            targetFloors.remove(currentFloor);
            System.out.println("Elevator " + id + " stopped at floor " + currentFloor);
        }

        // Decide where to go next (may flip direction if nothing left this way)
        updateDirection();
    }

    /**
     * SCAN/LOOK algorithm core: keep going the same direction while
     * targets remain that way; flip when nothing's left.
     *
     * ceiling(currentFloor) → nearest target at or above us
     * floor(currentFloor)   → nearest target at or below us
     * (TreeSet method names "ceiling" and "floor" are unrelated to
     *  elevator floors — just an unfortunate name collision.)
     */
    private void updateDirection() {
        if (targetFloors.isEmpty()) {
            direction = Direction.IDLE;
            state = ElevatorState.IDLE;
            return;
        }

        Integer above = targetFloors.ceiling(currentFloor);
        Integer below = targetFloors.floor(currentFloor);

        if (direction == Direction.UP) {
            direction = (above != null) ? Direction.UP : Direction.DOWN;
        } else {
            direction = (below != null) ? Direction.DOWN : Direction.UP;
        }

        state = (direction == Direction.UP)
                ? ElevatorState.MOVING_UP
                : ElevatorState.MOVING_DOWN;
    }

    // ===== Getters used by dispatch strategies =====
    public int getId()              { return id; }
    public int getCurrentFloor()    { return currentFloor; }
    public ElevatorState getState() { return state; }
    public Direction getDirection() { return direction; }
    public boolean isIdle()         { return state == ElevatorState.IDLE; }
    public int pendingRequests()    { return targetFloors.size(); }
}
