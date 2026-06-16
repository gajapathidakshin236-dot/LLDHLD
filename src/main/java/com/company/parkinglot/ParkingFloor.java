package com.company.parkinglot;

import java.util.ArrayList;
import java.util.List;

/**
 * One floor of the parking lot. Holds a list of spots and can find a
 * free spot that fits a given vehicle type.
 */
public class ParkingFloor {
    private final int floorNumber;
    private final List<ParkingSpot> spots;

    public ParkingFloor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.spots = new ArrayList<>();
    }

    public void addSpot(ParkingSpot spot) {
        spots.add(spot);
    }

    /** First free spot on this floor that fits the vehicle type, or null. */
    public ParkingSpot findAvailableSpot(VehicleType type) {
        for (ParkingSpot spot : spots) {
            if (spot.isAvailable() && spot.canFit(type)) {
                return spot;
            }
        }
        return null;
    }

    public int getFloorNumber()        { return floorNumber; }
    public List<ParkingSpot> getSpots() { return spots; }

    public long availableCount() {
        return spots.stream().filter(ParkingSpot::isAvailable).count();
    }
}
