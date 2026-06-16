package com.company.elevator;

/**
 * Abstract base for all elevator requests.
 *
 * A request is ALWAYS one of:
 *   - HallCall: pressed UP/DOWN from a floor (outside the car)
 *   - CarCall:  pressed a floor number from inside the car
 *
 * Marked abstract because a "generic request" makes no sense —
 * every real request is concretely a hall call or a car call.
 */
public abstract class Request {
    protected final int floor;

    public Request(int floor) {
        this.floor = floor;
    }

    public int getFloor() {
        return floor;
    }
}
