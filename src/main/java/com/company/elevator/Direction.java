/**
 * Direction of movement or request.
 * IDLE means "not heading anywhere" — used for both elevators at rest
 * and as a sentinel value.
 */
package com.company.elevator;

public enum Direction {
    UP,
    DOWN,
    IDLE
}
