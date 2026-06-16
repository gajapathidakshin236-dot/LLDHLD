package com.company.parkinglot;

import java.util.List;

/**
 * STRATEGY #1: how to choose a spot across floors.
 * Swappable: nearest-floor-first, least-busy-floor, etc.
 */
public interface SpotAllocationStrategy {
    ParkingSpot findSpot(List<ParkingFloor> floors, Vehicle vehicle);
}
