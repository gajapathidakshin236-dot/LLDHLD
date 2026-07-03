package LLD.ELEVATOR;

import java.util.TreeSet;

/**
 * A single elevator cabin.
 *
 * Motion model (simplified SCAN):
 *   - Owns a sorted set of target floors (from HallCalls + CarCalls it accepted).
 *   - On each tick move() advances one floor in the current direction.
 *   - If the current floor matches a target, we "stop" (log + remove target).
 *   - When there are no more targets in the current direction, updateDirection()
 *     flips (or goes IDLE).
 *
 * Kept intentionally minimal — the dispatcher (ElevatorController + selection
 * strategy) decides WHICH elevator handles which request; the elevator itself
 * only manages its own queue and physical motion.
 */
public class Elevator {

    private final int id;
    private int currentFloor;
    private ElevatorState state;
    private Direction direction;
    private final TreeSet<Integer> pendingTargetFloors;

    public Elevator(final int id) {
        this.id                  = id;
        this.currentFloor        = 0;
        this.state               = ElevatorState.IDLE;
        this.direction           = Direction.IDLE;
        this.pendingTargetFloors = new TreeSet<>();
    }

    public int getId() {
        return id;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public ElevatorState getState() {
        return state;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean hasPendingWork() {
        return !pendingTargetFloors.isEmpty();
    }

    public void setState(final ElevatorState state) {
        this.state = state;
    }

    /**
     * Enqueue a request. If the cabin was idle, it wakes up and picks an
     * initial direction toward the newly added floor.
     */
    public void addRequest(final Request request) {
        pendingTargetFloors.add(request.getFloor());

        if (state == ElevatorState.IDLE) {
            direction = (request.getFloor() >= currentFloor) ? Direction.UP : Direction.DOWN;
            state     = (direction == Direction.UP) ? ElevatorState.MOVING_UP : ElevatorState.MOVING_DOWN;
        }
    }

    /**
     * Advance one floor in the current direction. Idempotent when idle.
     * Prints a line each time the cabin stops.
     */
    public void move() {
        if (pendingTargetFloors.isEmpty()) {
            goIdle();
            return;
        }

        if (direction == Direction.UP) {
            currentFloor++;
        } else if (direction == Direction.DOWN) {
            currentFloor--;
        }

        if (pendingTargetFloors.remove(currentFloor)) {
            System.out.println("Elevator " + id + " stopped at floor " + currentFloor);
        }

        updateDirection();
    }

    /**
     * Decide whether the elevator should keep going, reverse, or go IDLE
     * based on the remaining targets.
     */
    public void updateDirection() {
        if (pendingTargetFloors.isEmpty()) {
            goIdle();
            return;
        }

        final Integer nextTargetAbove = pendingTargetFloors.ceiling(currentFloor + 1);
        final Integer nextTargetBelow = pendingTargetFloors.floor(currentFloor - 1);

        if (direction == Direction.UP) {
            direction = (nextTargetAbove != null) ? Direction.UP : Direction.DOWN;
        } else if (direction == Direction.DOWN) {
            direction = (nextTargetBelow != null) ? Direction.DOWN : Direction.UP;
        } else {
            // Was IDLE; pick nearest.
            direction = (nextTargetAbove != null) ? Direction.UP : Direction.DOWN;
        }

        state = (direction == Direction.UP) ? ElevatorState.MOVING_UP : ElevatorState.MOVING_DOWN;
    }

    private void goIdle() {
        state     = ElevatorState.IDLE;
        direction = Direction.IDLE;
    }

    @Override
    public String toString() {
        return "Elevator{id=" + id
                + ", floor=" + currentFloor
                + ", state=" + state
                + ", direction=" + direction
                + ", pending=" + pendingTargetFloors + "}";
    }
}
