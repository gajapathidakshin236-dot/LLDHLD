package com.company.parkinglot;

import java.util.ArrayList;
import java.util.List;

/**
 * The physical parking lot: a collection of floors.
 *
 * Design note: NOT a Singleton. We instantiate one and inject it where
 * needed (see ParkingLotDemo). This keeps it testable and would let the
 * company model multiple lots later — both impossible with a hard Singleton.
 */
public class ParkingLot {
    private final List<ParkingFloor> floors;

    public ParkingLot() {
        this.floors = new ArrayList<>();
    }

    public void addFloor(ParkingFloor floor) {
        floors.add(floor);
    }

    public List<ParkingFloor> getFloors() {
        return floors;
    }

    /** A simple factory helper: build a lot with N floors of given spot counts. */
    public static ParkingLot build(int numFloors, int bikeSpots, int compactSpots, int largeSpots) {
        ParkingLot lot = new ParkingLot();
        int spotId = 1;
        for (int f = 0; f < numFloors; f++) {
            ParkingFloor floor = new ParkingFloor(f);
            for (int i = 0; i < bikeSpots; i++)    floor.addSpot(new ParkingSpot(spotId++, SpotType.BIKE));
            for (int i = 0; i < compactSpots; i++) floor.addSpot(new ParkingSpot(spotId++, SpotType.COMPACT));
            for (int i = 0; i < largeSpots; i++)   floor.addSpot(new ParkingSpot(spotId++, SpotType.LARGE));
            lot.addFloor(floor);
        }
        return lot;
    }
}
