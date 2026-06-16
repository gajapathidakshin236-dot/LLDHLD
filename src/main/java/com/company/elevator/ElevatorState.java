/**
 * The operational state of an elevator car.
 * IDLE         — parked, no pending requests
 * MOVING_UP    — currently moving up to serve requests
 * MOVING_DOWN  — currently moving down to serve requests
 * MAINTENANCE  — out of service, won't accept requests
 */
package com.company.elevator;

public enum ElevatorState {
    IDLE,
    MOVING_UP,
    MOVING_DOWN,
    MAINTENANCE
}
