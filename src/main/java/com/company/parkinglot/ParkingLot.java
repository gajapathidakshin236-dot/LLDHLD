package com.company.parkinglot;

public final class ParkingLot {

    private static volatile ParkingLot instance;

    private ParkingLot() {
    }

    public static ParkingLot getInstance() {
        if (instance == null) {
            synchronized (ParkingLot.class) {
                if (instance == null) {
                    instance = new ParkingLot();
                }
            }
        }
        return instance;
    }
}

