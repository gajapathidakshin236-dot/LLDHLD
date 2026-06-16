package com.company.elevator;

/**
 * A request made from INSIDE the elevator (someone pressed a floor button).
 * Only carries the target floor — no direction needed, the car already
 * knows where it is and can figure out which way to go.
 */
public class CarCall extends Request {

    public CarCall(int floor) {
        super(floor);
    }

    @Override
    public String toString() {
        return "CarCall(floor=" + floor + ")";
    }
}
