package com.company.elevator;

/**
 * A request made from OUTSIDE the elevator (on a floor in the hallway).
 * The person pressed UP or DOWN — the system knows the direction
 * but not the eventual destination.
 */
public class HallCall extends Request {
    private final Direction direction;

    public HallCall(int floor, Direction direction) {
        super(floor);
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "HallCall(floor=" + floor + ", dir=" + direction + ")";
    }
}
