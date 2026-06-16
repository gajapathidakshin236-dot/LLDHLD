package com.company.parkinglot;

/**
 * Spot sizes. A vehicle can park in a spot of its own size or larger:
 *   BIKE  -> can use BIKE, COMPACT, LARGE
 *   CAR   -> can use COMPACT, LARGE
 *   TRUCK -> can use LARGE only
 * (The fit logic lives in ParkingSpot.canFit.)
 */
public enum SpotType {
    BIKE, COMPACT, LARGE
}
