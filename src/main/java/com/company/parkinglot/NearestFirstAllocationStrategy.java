package com.company.parkinglot;

import java.util.List;

/**
 * Scans floors in order (floor 0 first) and returns the first fitting
 * free spot. Simplest sensible policy.
 */
public class NearestFirstAllocationStrategy implements SpotAllocationStrategy {
    @Override
    public ParkingSpot findSpot(List<ParkingFloor> floors, Vehicle vehicle) {
        for (ParkingFloor floor : floors) {
            ParkingSpot spot = floor.findAvailableSpot(vehicle.getType());
            if (spot != null) {
                return spot;
            }
        }
        return null;   // lot full (for this vehicle type)
    }
}
